<template>
  <div class="competitionInfo">
    <div class="titleBox">
      <div class="item">
        <span style="font-weight: bolder;font-size: 1.5em">{{dataReactive.competition.title}}</span>
      </div>
      <div class="item">
        <el-tag
            :color="statusObj().color"
            effect="dark"
            >
          {{statusObj().title}}
        </el-tag>
      </div>
    </div>
    <div class="infoBox">
      <span class="infoItem">参与人数: {{dataReactive.competition.registerCount}}</span>
      <span class="infoItem">比赛时间：{{dataReactive.competition.startTime}} 至 {{dataReactive.competition.endTime}}</span>
      <span v-if="statusObj().id === 1" class="infoItem">倒计时: {{timeStr}}</span>
    </div>

    <el-menu
        default-active="1"
        class="el-menu-demo"
        mode="horizontal"
    >
      <el-menu-item index="1" @click="onclickMenuItem(1)">
        详情
      </el-menu-item>
      <el-menu-item index="2" @click="onclickMenuItem(2)">
        题目列表
      </el-menu-item>
      <el-menu-item index="3" @click="onclickMenuItem(3)">
        提交状态
      </el-menu-item>
      <el-menu-item index="4" @click="onclickMenuItem(4)">
        排行
      </el-menu-item>
    </el-menu>
    <div>
      <Description :cid="props.cid" v-if="menuItem === 1"/>
      <ProblemSet :cid="props.cid" v-if="menuItem === 2"/>
      <StatusList :cid="props.cid" v-if="menuItem === 3"/>
      <RankList :cid="props.cid" v-if="menuItem === 4"/>
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed, onMounted, reactive, ref, watch} from "vue";
import {getCompetitionInfoDTO} from "../../axios/network/competition";
import {getTimeLeft2ByMss} from "../../utils/utils";
import Description from "./Description.vue";
import ProblemSet from "./ProblemSet.vue";
import StatusList from "./StatusList.vue";
import RankList from "./RankList.vue";

const menuItem = ref(1)

const onclickMenuItem = (key: number) => {
  menuItem.value = key
}

const props = defineProps({
  cid: {
    required: true,
    type: Number
  }
})
const dataReactive = reactive<any>({
  competition:{},
  countDown:0
})
onMounted(()=>{
  getCompetitionInfoDTO(props.cid)
  .then(res => {
    const {data} = res.data
    dataReactive.competition = data
    const startTime = new Date(data.startTime).getTime()
    const endTime = new Date(data.endTime).getTime()
    const current = new Date().getTime()
    if (current >= startTime && current <= endTime){
      dataReactive.countDown = endTime - current
    }
  })
})
watch(()=>props.cid,()=>{
  getCompetitionInfoDTO(props.cid)
      .then(res => {
        const {data} = res.data
        dataReactive.competition = data
        const startTime = new Date(data.startTime).getTime()
        const endTime = new Date(data.endTime).getTime()
        const current = new Date().getTime()
        if (current >= startTime && current <= endTime){
          dataReactive.countDown = endTime - current
        }
      })
})

let timer = setInterval(()=>{
  if (dataReactive.countDown > 0){
    dataReactive.countDown = dataReactive.countDown - 1000
  }else {
    clearInterval(timer)
  }
},1000)

const timeStr = computed(() => {
  return getTimeLeft2ByMss(dataReactive.countDown)
})



const statusObj = () => {
  const startTime = new Date(dataReactive.competition.startTime).getTime()
  const endTime = new Date(dataReactive.competition.endTime).getTime()
  const current = new Date().getTime()
  if(startTime > current){
    return {
      id: 0,
      color:'#FF9900',
      title: '未开始'
    }
  }if (current > startTime && current < endTime){
    return {
      id: 1,
      color: '#19BE6B',
      title: '进行中'
    }
  }else {
    return {
      id: 2,
      color: '#ED3F14',
      title: '已结束'
    }
  }
}

</script>

<style scoped lang="scss">
.competitionInfo{
  .titleBox{
    display: flex;
    flex: auto;
    flex-direction: row;
    height: 50px;
    line-height: 50px;
    .item{
      margin-right: 10px;
      width: fit-content;
      height: fit-content;
    }
  }
  .infoBox{
    line-height: 30px;
    color: #696969;
    font-size: 1.2em;
    font-weight: 550;
  }
  .infoItem{
    margin-right: 20px;
  }
}
</style>

