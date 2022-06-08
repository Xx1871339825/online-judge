<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span>竞赛列表</span>
      </div>
    </template>
    <div class="settingList">
      <div class="searchBox">
        <div class="itemBox">
          <div class="item" style="width: 240px;">
            <el-input v-model="searchBox.search" placeholder="比赛名称、id或创建用户的昵称" clearable />
          </div>
          <div class="item">
            <el-select v-model="searchBox.status"  placeholder="状态">
              <el-option
                v-for="item in statusArr"
                :label="item.name"
                :value="item.id"
                :key="item.id"
              />
            </el-select>
          </div>

          <div class="item">
            <el-select v-model="searchBox.type" placeholder="比赛类型">
              <el-option
                  v-for="item in authArr"
                  :label="item.name"
                  :value="item.id"
                  :key="item.id"
              />
            </el-select>
          </div>

          <div class="item">
            <el-button @click="clickSearch" >搜索</el-button>
          </div>
        </div>
      </div>
      <div class="tableBox">
        <el-table :data="tableData.list" stripe style="width: 100%">
          <el-table-column prop="nickname" label="创建的用户" />
          <el-table-column prop="title" label="标题" />
          <el-table-column prop="auth" label="比赛类型">
            <template #default="scope">
              <span v-if="scope.row.auth === 0">公开赛</span>
              <span v-else>私有赛</span>
            </template>
          </el-table-column>
          <el-table-column prop="startTime" label="开始时间">
          </el-table-column>
          <el-table-column prop="endTime" label="结束时间">
          </el-table-column>

          <el-table-column prop="option" label="操作">
            <template #default="scope">
              <el-button @click="clickInfo(scope.row.id)" type="text">查看</el-button>
              <el-button @click="clickUpdate(scope.row)" type="text">编辑</el-button>
              <el-popconfirm @confirm="clickDel(scope.row.id)" title="确定要删除吗?">
                <template #reference>
                  <el-button type="text">删除</el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <div class="pagination">
      <el-pagination v-model:current-page="searchBox.current" :total="tableData.total" background layout="prev, pager, next" >
      </el-pagination>
    </div>

    <el-dialog width="900px" v-model="updateReactive.isVisible" title="修改比赛">
      <competition-update-form :update-success="updateSuccess" :obj="updateReactive.competitionObj"  />
    </el-dialog>

    <el-dialog width="1500px" v-model="infoReactive.isVisible" title="比赛详情">
      <CompetitionInfo :cid="infoReactive.cid" />
    </el-dialog>

  </el-card>
</template>

<script setup lang="ts">
import {onMounted, reactive, ref, watch} from "vue";
import {onBeforeRouteUpdate, useRouter} from "vue-router";
import {deleteCompetition, getCompetitionPage, getProblemListByCid} from "../../../axios/network/competition";
import {ElMessage} from "element-plus";
import CompetitionUpdateForm from "../../../components/CompetitionUpdateForm/CompetitionUpdateForm.vue";
import CompetitionInfo from "../../../components/CompetitionInfo/CompetitionInfo.vue"

const updateReactive = reactive<any>({
  isVisible:false,
  competitionObj:{}
})

const infoReactive = reactive<any>({
  isVisible:false,
  cid:-1
})


const clickInfo = (cid:any) => {
  infoReactive.isVisible = !infoReactive.isVisible
  infoReactive.cid = cid
}

const clickUpdate = (obj:any) => {
  getProblemListByCid(obj.id).then(res => {
    let {data} = res.data
    obj.problemList = data
    updateReactive.competitionObj = obj

  })
  updateReactive.isVisible = !updateReactive.isVisible
}

const updateSuccess = () => {
  updateReactive.isVisible = !updateReactive.isVisible
  initData()
}

const clickDel = (cid:any) => {
  deleteCompetition(cid).then(() => {
    ElMessage.success('删除成功')
    initData()
  })
}

const statusArr = ref([
  {
    id: -1,
    name: '全部'
  },
  {
    id: 0,
    name: '未开始'
  },
  {
    id: 1,
    name: '已开始'
  },
  {
    id: 2,
    name: '已结束'
  }
])
const authArr = ref([
  {
    id: -1,
    name: '全部'
  },
  {
    id: 0,
    name: '公开赛'
  },
  {
    id: 1,
    name: '私有赛'
  }
])
const router = useRouter()
const {search,current,status,type} = router.currentRoute.value.query
const tableData = reactive({
  list: [],
  total: 0,
})
const searchBox = reactive({
  search: search,
  size:10,
  status: isNaN(Number(status))? -1:Number(status),
  type: isNaN(Number(type))? -1: Number(type),
  current: isNaN(Number(current))?0:Number(current)
})
const initData = () => {
  getCompetitionPage(searchBox).then(res => {
    const {data} = res.data
    tableData.list = data.records
    tableData.total = data.total
  })
}
onMounted(() => {
  initData()
})
onBeforeRouteUpdate(() => {
  initData()
})
const clickSearch = () => {
  router.push({
    path: router.currentRoute.value.path,
    query: {
      search: searchBox.search,
      current: searchBox.current,
      status: searchBox.status,
      type: searchBox.type
    }
  })
}
watch(() => searchBox.current,() => {
  clickSearch()
})
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
