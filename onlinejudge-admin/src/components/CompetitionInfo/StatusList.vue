<template>
  <div class="statusList">
    <div class="topBarBox">
      <div class="topBar">
        <div style="width: 100px;text-align: center" class="tbText">
          <span style="font-size: 1.2em;font-weight: bolder">状态</span>
        </div>
        <div class="tbText">
          <el-input placeholder="题目编号" v-model="searchBox.pid" />
        </div>
        <div class="tbText">
          <el-input placeholder="用户" v-model="searchBox.nickname"/>
        </div>
        <div class="tbText">
          <el-select v-model="searchBox.status">
            <el-option :value="null">All</el-option>
            <el-option
                v-for="item in statusArr"
                :key="item.id"
                :value="item.id"
                :label="item.name"
            />
          </el-select>
        </div>
        <div class="tbText" style="width: 120px;">
          <el-button @click="onSearch">搜索</el-button>
        </div>
      </div>
    </div>
    <div class="statusTable">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="problemId" label="题目id" width="70" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :color="getStatusObj(scope.row.status).bgColor" type="dark">{{getStatusObj(scope.row.status).name}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="score" label="分数"/>
        <el-table-column prop="time" label="运行时间(ms)" width="120"/>
        <el-table-column prop="memory" label="运行内存(kb)" width="120"/>
        <el-table-column prop="language" label="语言">
          <template #default="scope">
            <el-button @click="clickInfo(scope.row)" type="text">{{getLanguage(scope.row.language)}}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="nickname" label="用户" />
        <el-table-column prop="submitTime" label="提交时间" />
      </el-table>
    </div>
    <div class="pWrap">
      <div class="pagination">
        <el-pagination @current-change="onCurrentChange" background layout="prev, pager, next" :total="total" />
      </div>
    </div>
    <el-dialog
        title="提交详情"
        width="1000px" v-model="infoDialog.isVisible">
        <StatusInfo
            :sid="infoDialog.sid"
            :statusNo = "infoDialog.statusNo"
            :time="infoDialog.time"
            :memory="infoDialog.memory"
            :language="infoDialog.language"
            :nickname="infoDialog.nickname"
            :message="infoDialog.message"
            :score="infoDialog.score"
            :pid="infoDialog.pid"
            :uid="infoDialog.uid"
        />
    </el-dialog>
  </div>

</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watch} from "vue";
import {getCSubmitList} from "../../axios/network/competition";
import {ElMessage} from "element-plus";
import StatusInfo from "./StatusInfo.vue";

const infoDialog = reactive({
  sid:-1,
  statusNo:-1,
  time:-1,
  memory:-1,
  nickname:'',
  language:'',
  message:'',
  score:-1,
  pid:-1,
  uid:-1,
  isVisible:false
})
const clickInfo = (obj:any) => {
  console.log(obj)
  infoDialog.sid = obj.id
  infoDialog.statusNo = obj.status
  infoDialog.time = obj.time
  infoDialog.memory = obj.memory
  infoDialog.nickname = obj.nickname
  infoDialog.language = getLanguage(obj.language)
  infoDialog.message = obj.errorMessage
  infoDialog.score = obj.score
  infoDialog.pid = obj.problemId
  infoDialog.uid = obj.userId
  infoDialog.isVisible = !infoDialog.isVisible

}



const getLanguage = (id:number) => {
  if (id == 0) return 'cpp'
  if (id == 1) return 'java'
  else return ''
}
const getStatusObj = (statusNo:number) => {
  for (let valueElement of statusArr.value) {
    if (valueElement.id == statusNo)
      return valueElement
  }
}


const searchBox = reactive({
  pid:'',
  nickname:'',
  status:null,
  current:1,
  size:10
})

const props = defineProps({
  cid: {
    required: true,
    type: Number
  }
})
const tableData = ref([])
const total = ref(0)

const statusArr = ref([
  {
    id:0,
    name: 'Accepted',
    bgColor: '#19BE6B'
  },
  {
    id: 1,
    bgColor: '#ED3F14',
    name: 'WrongAnswer'
  },
  {
    id: 2,
    bgColor: '#FF9900',
    name: 'NotAllAccepted'
  },
  {
    id: 3,
    bgColor: '#ED3F14',
    name: 'RuntimeError'
  },
  {
    id: 4,
    bgColor: '#ED3F14',
    name: 'TimeLimitExceeded'
  },
  {
    id: 5,
    bgColor: '#ED3F14',
    name: 'MemoryLimitExceeded'
  },
  {
    id: 6,
    bgColor: '#FF9900',
    name: 'Pending'
  },
  {
    id: 7,
    bgColor: '#19BE6B',
    name: 'Compiling'
  },
  {
    id: 8,
    bgColor: '#2D8CF0',
    name: 'Judging'
  },
  {
    id: 9,
    bgColor: '#FF9900',
    name: 'CompileError'
  }
])

onMounted(() => {
  initData()
})

const onSearch = () => {
  initData()
}



watch(()=> props.cid,()=>{
  initData()
})

const onCurrentChange = (value:number) => {
  searchBox.current = value
  initData()
}

const initData = () => {
  if (searchBox.pid != ''&& isNaN(Number(searchBox.pid))){
    ElMessage.error('题目id请输入数字')
    return
  }
  getCSubmitList(props.cid,searchBox).then(res => {
    const {data} = res.data
    tableData.value = data.records
    total.value = data.total
  })
}
</script>

<style scoped lang="scss">
@import "src/assets/css/const";
.statusList{
  margin: 0 10px;
  padding: 20px;
  height: 100%;
  border-radius: 10px;
  background-color: white;
  .topBarBox{
    width: fit-content;
    margin: 0 auto;
    border-bottom: 1px solid $lineGrey;
    .topBar{
      display: flex;
      flex-wrap:wrap;
      flex-direction: row;
      .tbText{
        line-height: 50px;
        margin-right: 10px;
        width: 150px;
      }
    }
  }
  .statusTable{
    overflow-x: auto;
  }
  .pWrap{
    margin-top: 10px;
    .pagination{
      margin: 0 auto;
      width: 50%;
    }
  }
}
</style>
