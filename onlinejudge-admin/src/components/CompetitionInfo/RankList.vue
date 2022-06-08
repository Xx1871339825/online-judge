<template>
 <div class="rankListTableBox">
   <el-table :cell-class-name="tableCellClass" :data="tableData" border>
     <el-table-column align="center" prop="rank" label="排名" width="80" />
     <el-table-column align="center" prop="nickname" label="用户" width="200" />
     <el-table-column align="center" prop="totalScore" label="总分" width="80" />
     <el-table-column

         align="center"
         width="80"
         v-for="item in column"
         :prop="'p'+item.pid"
         :key="'p'+item.pid"
         :label="'p'+item.pid"
     >
       <template #default="scope">
         {{scope.row.submissionInfo['p'+item.pid].score}}
       </template>
     </el-table-column>
   </el-table>
 </div>
</template>

<script setup lang="ts">

import {onMounted, ref, watch} from "vue";
import {getCRankList, getProblemInfoListByCid} from "../../axios/network/competition";

const props = defineProps({
  cid: {
    required: true,
    type: Number
  }
})

const tableCellClass = (obj:any) => {
  let {submissionInfo} = obj.row
  let {property} = obj.column
  let temp = submissionInfo[property]
  if (temp == null)
    return ''

  console.log(temp)
  if (temp.status === 1){
    return 'ac'
  }else if (temp.status === 2 && temp.score > 0){
    return 'naAc'
  }else if (temp.status === 2 && temp.score === 0){
    return 'wa'
  }
  return ''
}


onMounted(()=>{
  initData()
})
watch(()=>props.cid,()=>{
  initData()
})
const column = ref([])
const tableData = ref([])
const initData = () => {
  getProblemInfoListByCid(props.cid).then(res => {
    const {data} = res.data
    column.value = data
    getCRankList(props.cid).then(res => {
      tableData.value = res.data.data
    })
  })

}
</script>

<style lang="scss">
.rankListTableBox{
  margin: 10px auto;
  width: fit-content;
  .ac{
    background-color: #19BE6B;
  }

  .el-table tbody .ac:hover>td{
    background-color: #19BE6B !important;
  }
  .ac, .ac:hover > td {
    background-color: #19BE6B !important;
  }

  .naAc{
    background-color: #FF9900;
  }
  .el-table tbody .naAc:hover>td{
    background-color: #FF9900 !important;
  }
  .naAc, .naAc:hover > td {
    background-color: #FF9900 !important;
  }


  .wa{
    background-color: #ED3F14;
  }

  .el-table tbody .wa:hover>td{
    background-color: #ED3F14 !important;
  }
  .wa, .wa:hover > td {
    background-color: #ED3F14 !important;
  }
}


</style>
