<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span>配置管理</span>
      </div>
    </template>
    <div class="settingList">
      <div class="searchBox">
        <div class="itemBox">
          <div class="item">
            <el-input v-model="searchReactive.search" placeholder="输入标题或者创建者" clearable />
          </div>
          <div class="item">
            <el-select v-model="searchReactive.type" placeholder="选择类型">
              <el-option
                  v-for="item in settingType"
                  :key="item.id"
                  :value="item.name"
                  :label="item.name"
              >
              </el-option>
            </el-select>
          </div>
          <div class="item">
            <el-button type="primary" @click="clickSearch">搜索</el-button>
          </div>
          <div class="item">
            <span v-if="menus.indexOf('sys:setting:add') !== -1"><el-button @click="clickDialog(1,-1)" type="success">新增</el-button></span>
            <span v-else><el-button disabled type="success">新增</el-button></span>
          </div>
        </div>
      </div>
      <div class="tableBox">
        <el-table :data="tableData.list" stripe style="width: 100%">
          <el-table-column prop="settingId" width="80" label="#"/>
          <el-table-column prop="nickname" label="创建的用户" />
          <el-table-column prop="title" label="标题" />
          <el-table-column prop="settingType" width="80" label="设置类型">
            <template #default="scope">
              {{scope.row.settingType == 0? "轮播图":"公告"}}
            </template>
          </el-table-column>
          <el-table-column width="60" prop="sort" label="排序"/>
          <el-table-column prop="createTime" label="创建时间"/>
          <el-table-column prop="updateTime" label="更新时间" />
          <el-table-column prop="status" label="状态">
            <template #default="scope">
<!--              {{scope.row.status == 0? "可用":"禁用"}}-->
              <el-tag v-if="scope.row.status == 0" type="success">启用</el-tag>
              <el-tag v-else type="danger">禁用</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button v-if="menus.indexOf('sys:setting:update') !== -1" @click="clickDialog(2,scope.row.settingId)" type="primary">修改</el-button>
              <el-button disabled v-else type="primary">修改</el-button>
              <el-popconfirm title="确认删除¿" @confirm="clickDel(scope.row.settingId)">
                <template #reference>
                  <el-button  v-if="menus.indexOf('sys:setting:update') !== -1" type="danger">删除</el-button>
                  <el-button disabled v-else type="danger">删除</el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <div class="pagination">
      <el-pagination v-model:current-page="searchReactive.current" :page-size="searchReactive.size" background layout="prev, pager, next" :total="tableData.total">
      </el-pagination>
    </div>
    <el-dialog v-model="dialogFormReactive.visible" :title="dialogFormReactive.title">
<!--      <setting-add-form :sid="sidRef" @optStatus="optStatus" v-if="dialogFormReactive.type === 1" />-->
      <setting-add-form @optStatus="optStatus" v-if="dialogFormReactive.type === 1" />
      <setting-update-form @optStatus="optStatus" :sid="sidRef" v-else />
    </el-dialog>
  </el-card>
</template>

<script setup lang="ts">
import {onMounted, reactive, ref, watch} from "vue";
import {settingType} from "../../assets/ts/const";
import {deleteSetting, getSettingList} from "../../axios/network/setting";
import {onBeforeRouteUpdate, useRouter} from "vue-router";
import stores from "../../stores/stores";
import SettingAddForm from "./SettingAddForm/SettingAddForm.vue";
import SettingUpdateForm from "./SettingUpdateForm/SettingUpdateForm.vue";

const sidRef = ref(-1)

const router = useRouter()
console.log(router.currentRoute.value.query.search)
const {search,type,current} = router.currentRoute.value.query
const menus = JSON.stringify(stores.getters.getMenus)

const dialogFormReactive = reactive({
  title:'新增设置',
  type:1,
  visible:false
})

const searchReactive= reactive({
    search: search,
    type: type,
    size:10,
    current: current
  })
  const tableData = reactive({
      list:[],
      total:0
  })
  onMounted(()=>{
    getSettingList(searchReactive).then(res=>{
      const {data} = res.data
      tableData.list = data.records
      tableData.total = data.total
    })
  })
onBeforeRouteUpdate(()=>{
  getSettingList(searchReactive).then(res=>{
    const {data} = res.data
    tableData.list = data.records
    tableData.total = data.total
  })
})

watch(()=>searchReactive.current,() => {
  console.log('监听到改变')
  clickSearch()
})

const clickDialog = (type:number,sid:number) => {
  if (type == 1){
    dialogFormReactive.visible = !dialogFormReactive.visible
    dialogFormReactive.title = '新增设置'
    dialogFormReactive.type = 1
    sidRef.value = -1
  }else if (type == 2){
    dialogFormReactive.visible = !dialogFormReactive.visible
    dialogFormReactive.title = '修改设置'
    dialogFormReactive.type = 2
    sidRef.value = sid
  }
}




const clickSearch = () =>{
  router.push({
    path: router.currentRoute.value.path,
    query:{
      search: searchReactive.search,
      type: searchReactive.type,
      current: searchReactive.current
    }
  })
}

const optStatus = (value:any) => {
  console.log(value)
  if (value){
    getSettingList(searchReactive).then(res=>{
      const {data} = res.data
      tableData.list = data.records
      tableData.total = data.total
    })
    dialogFormReactive.visible = false
  }
}
const clickDel = (sid:number) => {
  console.log(sid)
  deleteSetting(sid).then(res => {
    getSettingList(searchReactive).then(res=>{
      const {data} = res.data
      tableData.list = data.records
      tableData.total = data.total
    })
    console.log('删除成功！')
  })
}
</script>

<style lang="scss" scoped>
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
