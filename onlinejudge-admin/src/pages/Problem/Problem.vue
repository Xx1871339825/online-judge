<template>
  <RouterView />
<!--  <el-card class="box-card">-->
<!--    <template #header>-->
<!--      <div class="card-header">-->
<!--        <span>问题管理</span>-->
<!--      </div>-->
<!--    </template>-->
<!--    <div class="settingList">-->
<!--      <div class="searchBox">-->
<!--        <div class="itemBox">-->
<!--          <div class="item" style="width: 240px;">-->
<!--            <el-input v-model="searchBox.search" placeholder="输入标题、标签、id或创建者" clearable />-->
<!--          </div>-->
<!--          <div class="item">-->
<!--            <el-select v-model="searchBox.problemType"  placeholder="选择类型">-->
<!--              <el-option-->
<!--                v-for="item in option.optionList"-->
<!--                :key="item.id"-->
<!--                :label="item.name"-->
<!--                :value="item.id"-->
<!--              />-->
<!--            </el-select>-->
<!--          </div>-->
<!--          <div class="item">-->
<!--            <el-button type="primary" @click="clickSearch" >搜索</el-button>-->
<!--          </div>-->
<!--          <div class="item">-->
<!--            <el-button @click="clickDialog(1,-1)" v-if="menus.indexOf('sys:problem:add') !== -1 " type="success">新增</el-button>-->
<!--            <el-button v-else disabled type="success">新增</el-button>-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--      <div class="tableBox">-->
<!--        <el-table :data="tableData.list" stripe style="width: 100%">-->
<!--          <el-table-column prop="pid" width="80" label="#"/>-->
<!--          <el-table-column prop="nickname" label="创建的用户" />-->
<!--          <el-table-column prop="title" label="标题" />-->
<!--          <el-table-column prop="problemType" label="题目类型">-->
<!--            <template #default="scope">-->
<!--              <span v-if="scope.row.problemType === 0">-->
<!--                默认(开放提交)-->
<!--              </span>-->
<!--              <span v-else>-->
<!--                比赛专用-->
<!--              </span>-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column prop="status" label="通过率">-->
<!--            <template #default="scope">-->
<!--              <el-tag type="success">-->
<!--                {{(scope.row.acceptedCount/scope.row.tryCount * 100).toFixed(2) + '%'}}-->
<!--              </el-tag>-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column prop="tag" label="算法标签">-->
<!--            <template #default="scope">-->
<!--              <el-tag-->
<!--                  style="margin-left: 10px;margin-bottom: 10px"-->
<!--                  effect="dark"-->
<!--                  type="success"-->
<!--                  v-for="(item,index) in scope.row.tag"-->
<!--                  :key="index"-->
<!--              >-->
<!--                {{item}}-->
<!--              </el-tag>-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--          <el-table-column label="操作">-->
<!--            <template #default="scope">-->
<!--              <el-button @click="clickDialog(2,scope.row.pid)" v-if="menus.indexOf('sys:problem:update') !== -1"  type="primary">修改</el-button>-->
<!--              <el-button v-else disabled type="primary">修改</el-button>-->
<!--              <el-popconfirm title="确认删除¿" >-->
<!--                <template #reference>-->
<!--                  <el-button v-if="menus.indexOf('sys:problem:delete') !== -1" type="danger">删除</el-button>-->
<!--                  <el-button v-else disabled type="danger">删除</el-button>-->
<!--                </template>-->
<!--              </el-popconfirm>-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--        </el-table>-->
<!--      </div>-->
<!--    </div>-->
<!--    <div class="pagination">-->
<!--      <el-pagination v-model:current-page="searchBox.current" :page-size="searchBox.size" background layout="prev, pager, next" :total="tableData.total">-->
<!--      </el-pagination>-->
<!--    </div>-->

<!--&lt;!&ndash;    弹窗&ndash;&gt;-->
<!--    <el-dialog v-model="dialogFormReactive.visible" :title="dialogFormReactive.title">-->
<!--      <problem-add-form  v-if="dialogFormReactive.type === 1" />-->
<!--      <problem-update-form v-else />-->
<!--    </el-dialog>-->
<!--  </el-card>-->
</template>

<script setup lang="ts">
// import {onMounted, reactive, ref, watch} from "vue";
// import {onBeforeRouteUpdate, useRouter} from "vue-router";
// import {getProblemList} from "../../axios/network/problem";
// import stores from "../../stores/stores";
// import ProblemAddForm from "./ProblemAddForm/ProblemAddForm.vue";
// import ProblemUpdateForm from "./ProblemUpdateForm/ProblemUpdateForm.vue";
// const menus = JSON.stringify(stores.getters.getMenus)
// //弹窗表单相关
// const dialogFormReactive = reactive({
//   title:'新增题目',
//   type:1,
//   visible:false
// })
// const sidRef = ref(-1)
//
// const clickDialog = (type:number,pid:number) => {
//   if (type == 1){
//     dialogFormReactive.visible = !dialogFormReactive.visible
//     dialogFormReactive.title = '新增题目'
//     dialogFormReactive.type = 1
//     // sidRef.value = -1
//   }else if (type == 2){
//     dialogFormReactive.visible = !dialogFormReactive.visible
//     dialogFormReactive.title = '修改题目'
//     dialogFormReactive.type = 2
//     // sidRef.value = sid
//   }
// }
//
// //本页面
//   const option = reactive({
//     optionList: [
//       {
//         id:0,
//         name: '默认',
//       },
//       {
//         id:1,
//         name:'比赛专用'
//       }
//     ]
//   })
//   const tableData = reactive({
//     list:[],
//     total:0
//   })
//   const router = useRouter()
//   const {search,problemType,current} = router.currentRoute.value.query
//   const searchBox = reactive({
//     search:search,
//     problemType: isNaN(Number(problemType))?0:Number(problemType),
//     current: isNaN(Number(current))?0:Number(current),
//     size:10
//   })
//   const initData = () => {
//     getProblemList(searchBox).then(res => {
//       let {data} = res.data
//       let {records,total} = data
//       for (let i = 0; i < records.length; i++) {
//         let tagList = records[i].tag.split(',')
//         let map = new Map<String,number>()
//         tagList.forEach((item:any,index:any) => {
//           map.set(item,index)
//         })
//         let mapToArr = [...map]
//         tagList = []
//         mapToArr.forEach(item => {
//           tagList.push(item[0])
//         })
//         records[i].tag = tagList
//       }
//       tableData.list = records
//       tableData.total = total
//     })
//   }
//   onMounted(()=>{
//     initData()
//   })
//   onBeforeRouteUpdate(()=>{
//     console.log('监听到')
//     initData()
//   })
//   const clickSearch = () => {
//     router.push({
//       path:router.currentRoute.value.path,
//       query:{
//         search:searchBox.search,
//         problemType:searchBox.problemType,
//         current:searchBox.current
//       }
//     })
//   }
//   watch(()=>searchBox.current,()=>{
//     clickSearch()
//   })
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
