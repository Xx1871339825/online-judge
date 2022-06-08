<template>
  <el-table :data="tableData" style="width: 100%">
    <el-table-column prop="pid" label="#" width="180" />
    <el-table-column prop="title" label="标题" width="180" />
    <el-table-column prop="status" label="通过率">
      <template #default="scope">
        <el-tag type="success">
          {{isNaN(scope.row.acceptedCount/scope.row.tryCount)? '0.00%':(scope.row.acceptedCount/scope.row.tryCount * 100).toFixed(2) + '%'}}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="level" label="难度">
      <template #default="scope">
        <el-tag effect="dark" type="success" v-if="scope.row.level === 0">简单</el-tag>
        <el-tag effect="dark" type="warning" v-else-if="scope.row.level === 1">普通</el-tag>
        <el-tag effect="dark" type="danger" v-else>困难</el-tag>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup lang="ts">
import {onMounted, ref, watch} from "vue";
import {getProblemInfoListByCid} from "../../axios/network/competition";

const props = defineProps({
  cid: {
    required: true,
    type: Number
  }
})
const tableData = ref([])
onMounted(() => {
  getProblemInfoListByCid(props.cid).then(res => {
    const {data} = res.data
    tableData.value = data
  })
})

watch(()=> props.cid,()=>{
  getProblemInfoListByCid(props.cid).then(res => {
    const {data} = res.data
    tableData.value = data
  })
})

</script>

<style scoped lang="scss">

</style>
