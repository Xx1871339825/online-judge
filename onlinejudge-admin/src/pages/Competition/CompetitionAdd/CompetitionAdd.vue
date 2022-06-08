<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span>添加比赛</span>
      </div>
    </template>
    <el-form
        ref="addCompetitionRef"
        :rules="addCompetitionRules"
        :model="addCompetitionForm"
        label-width="120px">
      <el-form-item label="标题" prop="title">
        <el-input v-model="addCompetitionForm.title" />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <md-editor v-model="addCompetitionForm.description" @onUploadImg="onUploadImg" :toolbars="toolBar" />

      </el-form-item>
      <el-form-item label="时间" prop="time">
        <el-date-picker
            v-model="addCompetitionForm.time"
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
            v-for="(item,index) in addCompetitionForm.problemList"
            :key="index">
          题目Id:{{item.pid}}
        </el-tag>
        <el-button @click="addProblemDialog.isVisible = !addProblemDialog.isVisible">添加题目</el-button>
      </el-form-item>
      <el-form-item label="比赛类型" prop="auth">
        <el-select v-model="addCompetitionForm.auth" placeholder="选择比赛类型">
          <el-option
            v-for="item in authType"
            :value="item.id"
            :label="item.name"
            :key="item.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item v-if="addCompetitionForm.auth !==0" label="密码" prop="password">
        <span style="width: 80%"><el-input placeholder="建议使用生成的密码，以保证密码的强度" v-model="addCompetitionForm.password" /></span><el-button @click="generatePwd">生成</el-button>
      </el-form-item>

      <el-form-item>
        <el-button @click="clickSubmit(addCompetitionRef)">提交</el-button>
      </el-form-item>
    </el-form>
  </el-card>

  <el-dialog title="添加题目" v-model="addProblemDialog.isVisible">
      <el-input v-model="addProblemDialog.search" @input="onChangeSearch" />
      <el-table @selection-change="handleSelection" :data="addProblemDialog.tableData">
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
import {onMounted, reactive, ref} from "vue";
  import MdEditor from 'md-editor-v3';
  import 'md-editor-v3/lib/style.css';
  import {toolBar} from '../../../assets/ts/const';
  import {uploadFile} from "../../../axios/network/user";
import {ElMessage} from "element-plus";
import {getSearchProblemList} from "../../../axios/network/problem";
import {UUID} from 'uuid-generator-ts';
import moment from "moment";
import {addCompetition} from "../../../axios/network/competition";



onMounted(() => {
  getSearchProblemList({search:addProblemDialog.search})
      .then(res => {
        const {data} = res.data
        addProblemDialog.tableData = data
      })
})
  const generatePwd = ()=>{
    addCompetitionForm.password = new UUID().getDashFreeUUID()
  }

  const onClickDialogSubmit = () => {
    if (selectionRef.value.length != 0){
        selectionRef.value.forEach((item:any) => {
          let obj = addCompetitionForm.problemList.find((i:any) => i.pid == item.pid)
          if(obj == null){
            addCompetitionForm.problemList.push(item)
          }
        })
    }
    console.log(addCompetitionForm.problemList)
  }
  const removeProblemTag = (index:number) => {
    addCompetitionForm.problemList.splice(index,1)
  }
  const selectionRef = ref([])
  const handleSelection = (val:any) => {
    selectionRef.value = val
  }
  const addCompetitionForm = reactive<any>({
    title: '',
    description: '',
    time:[],
    problemList:[],
    auth:0,
    password: ''
  })
  const addCompetitionRef = ref<any>(null)
  const clickSubmit = (ref:any) => {
    ref.validate((valid:any) => {
      if (valid){
        addCompetition(addCompetitionForm).then(() => {
          ElMessage.success('添加成功')
        })
      }
    })
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
  const addCompetitionRules = reactive({
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


  const addProblemDialog = reactive({
    isVisible: false,
    search: '',
    tableData: []
  })

  const onChangeSearch = () => {
    getSearchProblemList({search:addProblemDialog.search})
    .then(res => {
      const {data} = res.data
      addProblemDialog.tableData = data
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
