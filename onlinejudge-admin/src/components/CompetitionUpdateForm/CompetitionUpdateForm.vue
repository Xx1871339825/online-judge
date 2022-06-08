<template>
  <el-form
      ref="updateRef"
      :rules="updateCompetitionRules"
      :model="updateCompetitionForm"
      label-width="100px">
    <el-form-item label="标题" prop="title">
      <el-input v-model="updateCompetitionForm.title" />
    </el-form-item>
    <el-form-item label="描述" prop="description">
      <md-editor v-model="updateCompetitionForm.description" @onUploadImg="onUploadImg" :toolbars="toolBar" />

    </el-form-item>
    <el-form-item label="时间" prop="time">
      <el-date-picker
          v-model="updateCompetitionForm.time"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
      />
    </el-form-item>
    <el-form-item label="题目" prop="problemList">
      <el-tag
          style="margin-right: 10px;margin-bottom: 10px"
          closable
          @close="removeProblemTag(index)"
          v-for="(item,index) in updateCompetitionForm.problemList"
          :key="index">
        题目Id:{{item.pid}}
      </el-tag>
      <el-button @click="updateProblemDialog.isVisible = !updateProblemDialog.isVisible">添加题目</el-button>
    </el-form-item>
    <el-form-item label="比赛类型" prop="auth">
      <el-select v-model="updateCompetitionForm.auth" placeholder="选择比赛类型">
        <el-option
            v-for="item in authType"
            :value="item.id"
            :label="item.name"
            :key="item.id"
        />
      </el-select>
    </el-form-item>

    <el-form-item v-if="updateCompetitionForm.auth !==0" label="密码" prop="password">
      <span style="width: 80%"><el-input placeholder="建议使用生成的密码，以保证密码强度" v-model="updateCompetitionForm.password" /></span><el-button @click="generatePwd">生成</el-button>
    </el-form-item>

    <el-form-item>
      <el-button @click="clickSubmit(updateRef)">提交</el-button>
    </el-form-item>
  </el-form>
  <el-dialog title="添加题目" v-model="updateProblemDialog.isVisible">
    <el-input v-model="updateProblemDialog.search" @input="onChangeSearch" />
    <el-table @selection-change="handleSelection" :data="updateProblemDialog.tableData">
      <el-table-column type="selection" />
      <el-table-column label="#" prop="pid"/>
      <el-table-column label="用户名" prop="nickname"/>
      <el-table-column label="标题" prop="title"/>
      <el-table-column prop="level" label="难度">
        <template #default="scope">
          <el-tag effect="dark" type="success" v-if="scope.row.level === 0">简单</el-tag>
          <el-tag effect="dark" type="warning" v-else-if="scope.row.level === 1">普通</el-tag>
          <el-tag effect="dark" type="danger" v-else>困难</el-tag>
        </template>
      </el-table-column>
    </el-table>
    <div style="width: fit-content;margin: 10px auto 0">
      <el-button @click="onClickDialogSubmit">添加</el-button>
    </div>
  </el-dialog>
</template>

<script setup lang="ts">
import {onMounted, reactive, ref, watch} from "vue";
import MdEditor from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
import {toolBar} from '../../assets/ts/const';
import {uploadFile} from "../../axios/network/user";
import {ElMessage} from "element-plus";
import {getSearchProblemList} from "../../axios/network/problem";
import {UUID} from 'uuid-generator-ts';
import {updateCompetition} from "../../axios/network/competition";



const props = defineProps({
  obj:{
    required:true,
    type: Object
  },
  updateSuccess:{
    required:true,
    type:Function
  }
})

watch(()=>props.obj,()=>{
  initData()
  console.log(props.obj)
})

const initData = () =>{
  updateCompetitionForm.id = props.obj.id
  updateCompetitionForm.title= props.obj.title
  updateCompetitionForm.description = props.obj.description
  updateCompetitionForm.problemList = props.obj.problemList
  updateCompetitionForm.auth = props.obj.auth
  updateCompetitionForm.password = props.obj.password
  updateCompetitionForm.time = [new Date(props.obj.startTime),new Date(props.obj.endTime)]
}

onMounted(() => {
  initData()

  getSearchProblemList({search:updateProblemDialog.search})
      .then(res => {
        const {data} = res.data
        updateProblemDialog.tableData = data
      })
})
const generatePwd = ()=>{
  updateCompetitionForm.password = new UUID().getDashFreeUUID()
}

const onClickDialogSubmit = () => {
  if (selectionRef.value.length != 0){
    selectionRef.value.forEach((item:any) => {
      let obj = updateCompetitionForm.problemList.find((i:any) => i.pid == item.pid)
      if(obj == null){
        updateCompetitionForm.problemList.push(item)
      }
    })
  }
  console.log(updateCompetitionForm.problemList)
}
const removeProblemTag = (index:number) => {
  updateCompetitionForm.problemList.splice(index,1)
}
const selectionRef = ref([])
const handleSelection = (val:any) => {
  selectionRef.value = val
}
const updateCompetitionForm = reactive<any>({
  id:-1,
  title: '',
  description: '',
  time:[],
  problemList:[],
  auth:0,
  password: ''
})
const updateRef = ref<any>(null)
const clickSubmit = (ref:any) => {
  ref.validate()
  console.log()
  if (updateCompetitionForm.id !== -1 &&
      updateCompetitionForm.title !== ''&&
      updateCompetitionForm.description !== '' &&
      updateCompetitionForm.time.length !== 0 &&
      updateCompetitionForm.problemList.length !== 0
  ){
    if (updateCompetitionForm.auth === 0){
      let [startDate,endDate] = updateCompetitionForm.time
      let currentDate = new Date(new Date().toLocaleDateString()).getTime();
      if (endDate.getTime()<currentDate){
        console.log('结束日期必须为今日或今日之后')
      }else {
        if (endDate.getTime()-startDate.getTime() < 0){
          console.log('结束日期必须在开始日期之后')
        }else {
          updateCompetition(updateCompetitionForm).then(res => {
            ElMessage.success('修改成功')
            props.updateSuccess()
          })
        }
      }
    }else {
      if (updateCompetitionForm.password !== ''){
        let [startDate,endDate] = updateCompetitionForm.time
        let currentDate = new Date(new Date().toLocaleDateString()).getTime();
        if (endDate.getTime()<currentDate){
          console.log('结束日期必须为今日或今日之后')
        }else {
          if (endDate.getTime()-startDate.getTime() < 0){
            console.log('结束日期必须在开始日期之后')
          }else {
            updateCompetition(updateCompetitionForm).then(res => {
              ElMessage.success('修改成功')
              props.updateSuccess()
            })
          }
        }
      }
    }
  }
}

const validateTime = (rule:any,value:any,callback:any) => {
  if (value == null ||value.length == 0){
    callback(new Error('请选择时间范围'))
  }else {
    let timeObj = value[1]
    let currentDate = new Date(new Date().toLocaleDateString()).getTime();

    if (timeObj.getTime()<currentDate){
      callback(new Error('结束日期必须为今日或今日之后'))
    }else {
      let startObj = value[0]
      let endObj = value[1]

      if (endObj.getTime()-startObj.getTime() < 0){
        callback(new Error('结束日期必须在开始日期之后'))
      }else {
        callback()
      }
    }
  }
}
const updateCompetitionRules = reactive({
  title:[{ required: true, message: '请输入标题', trigger: 'blur' },
    { min: 3, max: 20, message: '长度最多3到20', trigger: 'blur' },],
  description:[{ required: true, message: '描述不能为空', trigger: 'blur' }],
  time:[{validator:validateTime,trigger:'blur'}],
  problemList: [
    {
      type: 'array',
      required: true,
      message: '比赛不能没有题目',
      trigger: 'change',
    }
  ],
  password:[
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
})


const updateProblemDialog = reactive({
  isVisible: false,
  search: '',
  tableData: []
})

const onChangeSearch = () => {
  getSearchProblemList({search:updateProblemDialog.search})
      .then(res => {
        const {data} = res.data
        updateProblemDialog.tableData = data
      })
}

const authType = reactive([
  {
    id: 0,
    name: '公开赛'
  },
  {
    id: 1,
    name: '私有赛'
  }
])

async function onUploadImg(files: FileList, callback: (urls: string[]) => void) {
  console.log('开始调用:=',files)
  const res = await Promise.all(
      Array.from(files).map((file) => {
        return new Promise((rev, rej) => {
          uploadFile(file).then((res) => rev(res))
              .catch(error => rej(error))
        });
      })
  );
  console.log('接收到的resList',res)

  callback(res.map((item: any) => item.data.data));
}

</script>

<style scoped lang="scss">

</style>
