<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span>标签管理</span>
      </div>
    </template>
    <div class="settingList">
      <div class="searchBox">
        <div class="itemBox">
          <div class="item">
            <el-input v-model="searchBox.search" placeholder="输入标签名称或者创建者" clearable />
          </div>
          <div class="item">
            <el-button type="primary" @click="clickSearch" >搜索</el-button>
          </div>
          <div class="item">
            <el-button @click="clickDialog(1,-1)" v-if="menus.indexOf('sys:tag:add') !== -1" type="success">新增</el-button>
            <el-button v-else disabled type="success">新增</el-button>
          </div>
        </div>
      </div>
      <div class="tableBox">
        <el-table :data="tableData.list" stripe style="width: 100%">
          <el-table-column prop="tid" width="80" label="#"/>
          <el-table-column prop="nickname" label="创建的用户" />
          <el-table-column prop="tagName" label="标题" />
          <el-table-column prop="problemCounts" label="题目数" />
          <el-table-column label="操作">
            <template #default="scope">
              <el-button v-if="menus.indexOf('sys:tag:update') !== -1" type="primary" @click="clickDialog(2,scope.row.tid)">修改</el-button>
              <el-button v-else disabled type="primary">修改</el-button>
              <el-popconfirm @confirm="clickDel(scope.row.tid)" title="确认删除¿" >
                <template #reference>
                  <el-button v-if="menus.indexOf('sys:tag:delete') !== -1" type="danger">删除</el-button>
                  <el-button v-else disabled type="danger">删除</el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <div class="pagination">
      <el-pagination v-model:current-page="searchBox.current" :page-size="searchBox.size" background layout="prev, pager, next" :total="tableData.total">
      </el-pagination>
    </div>
    <!--    弹窗-->
    <el-dialog v-model="dialogFormReactive.visible" :title="dialogFormReactive.title">
      <tag-add-form :close-dialog="closeDialog" v-if="dialogFormReactive.type === 1" />
      <tag-update-form :tid="tidRef" :close-dialog="closeDialog" ：tid="" v-else />
    </el-dialog>
  </el-card>
</template>

<script lang="ts" setup>
import {onMounted, reactive, ref, watch} from "vue";
import {onBeforeRouteUpdate, useRouter} from "vue-router";
import {deleteTag, getTagList} from "../../axios/network/tag";
import stores from "../../stores/stores";
import TagAddForm from './TagAddForm/TagAddForm.vue';
import TagUpdateForm from "./TagUpdateForm/TagUpdateForm.vue";
import {ElMessage} from "element-plus";
const menus = JSON.stringify(stores.getters.getMenus)

const dialogFormReactive = reactive({
  title:'新增tag',
  type:1,
  visible:false
})
const tidRef = ref(-1)

const closeDialog = () => {
    dialogFormReactive.visible = !dialogFormReactive.visible
    initData()
}

const clickDialog = (type:number,tid:number) => {
  if (type == 1){
    dialogFormReactive.visible = !dialogFormReactive.visible
    dialogFormReactive.title = '新增tag'
    dialogFormReactive.type = 1
    tidRef.value = -1
  }else if (type == 2){
    dialogFormReactive.visible = !dialogFormReactive.visible
    dialogFormReactive.title = '修改tag'
    dialogFormReactive.type = 2
    tidRef.value = tid
  }
}

const tableData = reactive({
  list:[],
  total:0
})
const router = useRouter()
const {search,current} = router.currentRoute.value.query
const searchBox = reactive({
  search:search,
  current: isNaN(Number(current))?0:Number(current),
  size:10
})
const initData = ()=>{
  getTagList(searchBox).then(res => {
    const {data} = res.data
    tableData.list = data.records
    tableData.total = data.total
  })
}
onMounted(()=>{
  initData()
})
onBeforeRouteUpdate(()=>{
  initData()
})
const clickSearch = () => {
  router.push({
    path:router.currentRoute.value.path,
    query:{
      search:searchBox.search,
      current:searchBox.current
    }
  })
}
watch(()=>searchBox.current,()=>{
  clickSearch()
})

const clickDel = (tid:number) => {
  deleteTag(tid).then(()=>{
    ElMessage.success('删除成功')
    initData()
  })
}




</script>

<style scoped lang="scss">
@import "src/assets/css/const";
.settingList{
  overflow-x: auto;
  .searchBox{
    line-height: 60px;
    width: 100%;
    min-width: 1000px;
    margin-bottom: 15px;
    border: 1px solid $lineGrey;
    border-radius: 5px;
    .itemBox{
      margin: 0 auto;
      display: flex;
      flex-direction: row;
      flex-wrap: wrap;
      width: fit-content;
      .item{
        margin-left: 10px;
      }
    }

  }
  .tableBox{
    width: 100%;
    min-width: 1000px;
  }
}
.pagination{
  width: fit-content;
  margin: 20px auto;
}
</style>
