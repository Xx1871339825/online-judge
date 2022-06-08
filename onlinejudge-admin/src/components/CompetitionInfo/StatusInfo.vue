<template>
  <div class="statusInfo">
    <div :style="{backgroundColor:status.bgColor}" class="statusInfoHeader">
      <div class="iconBox">
        <el-icon>
          <component :is="icon" />
        </el-icon>
      </div>
      <div class="statusBox">
<!--        <Title style={{color:'white'}} level={2}>{status.name}</Title>-->
        <h2 style="color: white">{{status.name}} </h2>
        <div class="info">
          <div class="spanBox">
            <span>题目id: {{props.pid}}</span>
            <span>运行时间: {{props.time}}ms</span>
            <span>运行内存: {{props.memory}}kb</span>
            <span>语言: {{props.language}}</span>
            <span>用户: {{props.nickname}}</span>
            <span>分数: {{props.score}}</span>
            <span><el-button @click="isShowCode = !isShowCode" type="text" style="color: white">查看提交的代码</el-button></span>
          </div>
        </div>
        <el-dialog title="源代码" width="900px" v-model="isShowCode">
          <md-editor previewOnly v-model="statusInfo.code"/>
        </el-dialog>
      </div>
      <div style="color: white;padding: 10px">
        {{props.message}}
      </div>
    </div>
    <div v-if="judgeCaseList.length !== 0">
      <div class="caseBox">
        <div class="title">
          测试点详情
        </div>
        <div class="caseItemBox">

            <div :key="index" v-for="(item,index) in judgeCaseList">
              <el-tooltip
                  class="box-item"
                  effect="light"
                  placement="top"
              >
              <template #content>
                标准输入：<md-editor previewOnly v-model="item.stdin"/>
                标准输出：<md-editor previewOnly v-model="item.stdout"/>
              </template>
              <div :style="{borderColor:item.color,color:item.color}"  class="caseItem">
            <div class="no">
              Test #{{index}}
            </div>
            <div class="statusStr">
              {{item.status}}
            </div>
            <div class="timeAndMemory">
              {{item.time}}ms/{{item.memory}}kb
            </div>
          </div>
              </el-tooltip>
            </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed, onMounted, ref, watch} from "vue";
import {getStatusInfo} from "../../axios/network/problem";
import MdEditor from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
interface IProps{
  sid:number
  statusNo?:number
  time?:number,
  memory?:number,
  language?:number,
  nickname?:string,
  message?:string,
  score?:number,
  pid?: number,
  uid?: number
}
const props = defineProps<IProps>()

const isShowCode = ref(false)

onMounted(()=>{
  initData()
})

watch(()=>props.sid,()=>{
  initData()
})

let status = computed(() => {
  for (let valueElement of statusArr.value) {
    if (props.statusNo === valueElement.id){
      return valueElement
    }
  }
})

let icon = computed(()=>{
  if (props.statusNo === 1||props.statusNo === 3||props.statusNo === 4||props.statusNo === 5||props.statusNo === 9) {
    // icon = <CloseCircleOutlined />
    return 'CircleClose'
  }else if (props.statusNo === 2){
    // icon = <ExclamationCircleOutlined />
    return 'Warning'
  }else if (props.statusNo === 6||props.statusNo === 7||props.statusNo === 8){
    // icon = <SyncOutlined spin />
    return 'Refresh'
  }else {
    return 'CircleCheck'
  }
})

const statusInfo = ref<any>({})
const judgeCaseList = ref([])
const initData = () => {
  getStatusInfo(props.sid).then(res => {
    const {data} = res.data
    let caseList = data.judgeCaseList
    let caseArr:any = []
    for (let caseListElement of caseList){
      let status:any = statusArr.value.find(item => caseListElement.status === item.id)
      caseArr.push({
        ...caseListElement,
        status:status.name,
        color: status.bgColor
      })
    }
    statusInfo.value = data
    judgeCaseList.value = caseArr
  })
}



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

</script>

<style scoped lang="scss">
@import "src/assets/css/const";
.statusInfo{
  margin: 20px 10px;
  height: 100%;
  border-radius: 10px;
  overflow-x: auto;


  .statusInfoHeader{
    width: 100%;
    height: fit-content;

    overflow: hidden;
    border-radius: 10px;
    .spanBox{
      span{
        margin-right: 10px;
      }
    }
    .iconBox{
      line-height: 100px;
      color: white;
      width: 80px;
      text-align: center;
      font-size: 50px;
      float: left;
    }
    .statusBox{
      height: 100%;
      h2{
        margin-top: 10px;
        margin-bottom: 0;
      }
      line-height: 50px;
      padding-left: 10px;
      float: right;
      width: calc(100% - 90px);
      .info{
        overflow-x: auto;
        color: white;
        a{
          color: white;
        }
        a:hover{
          color: #409EFF;
        }
      }
    }
  }
  .caseBox{
    border-radius: 10px;
    background-color: white;
    margin-top: 10px;
    .title{
      padding-left: 20px;
      height: 60px;
      line-height: 60px;
      font-size: 1.5em;
      font-weight: normal;
      color: #409EFF;
      border-bottom: 1px solid $lineGrey;
    }
    .caseItemBox{
      padding: 20px;
      display: flex;
      flex-wrap:wrap;
      flex-direction: row;
      .caseItem{
        padding: 10px;
        margin: 10px;
        width: 240px;
        height: 120px;
        border: 1px solid;
        border-left: 3px solid;
        border-radius: 5px;
        .statusStr{
          text-align: center;
          font-size: 1.5em;
          font-weight: bold;
        }
        .timeAndMemory{
          text-align: center;
        }
      }
    }
  }
}

</style>
