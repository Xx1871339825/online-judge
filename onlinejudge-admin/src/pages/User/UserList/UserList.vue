<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span>用户管理</span>
      </div>
    </template>
    <div class="settingList">
      <div class="searchBox">
        <div class="itemBox">
          <div class="item" style="width: 400px">
            <el-input v-model="searchBox.search" placeholder="输入用户名、邮箱、账号或用户id" clearable />
          </div>
          <div class="item">
            <el-select v-model="searchBox.banFlag"  placeholder="选择类型">
              <el-option
                v-for="item in banFlagArr"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </div>
          <div class="item">
            <el-button type="primary" @click="clickSearch" >搜索</el-button>
            <el-button type="danger" @click="clickBanBtn">封禁/解封</el-button>
          </div>
        </div>
      </div>
      <div class="tableBox">
        <el-table
            ref="multipleTableRef"
            @selection-change="handleSelectionChange"
            :data="tableData.list" stripe style="width: 100%">
          <el-table-column type="selection" width="55" />
          <el-table-column prop="uid" width="80" label="#"/>
          <el-table-column prop="nickname" label="用户昵称" />
          <el-table-column prop="email" label="邮箱" />
          <el-table-column prop="account" label="账号" />
          <el-table-column prop="status" label="状态">
            <template #default="scope">
              <!--              {{scope.row.status == 0? "可用":"禁用"}}-->
              <el-tag v-if="scope.row.banFlag === 1" type="danger">禁用</el-tag>
              <el-tag v-else type="success">可用</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="adminId" label="用户类型">
            <template #default="scope">
<!--              <el-tag v-if="scope.row.roleList.length === 0" type="success">普通用户</el-tag>-->
              <el-tag v-if="scope.row.adminId === null">
                普通用户
              </el-tag>
              <el-tag v-else type="success">
                管理员
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="roleList" label="用户拥有角色">
              <template #default="scope">
                <el-tag style="margin-left: 10px;margin-bottom: 10px"
                        type="success"
                  v-for="item in scope.row.roleList"
                        :key="item.id">
                  {{item.roleName}}
                </el-tag>
              </template>
          </el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-popover
                  v-if="scope.row.adminId !== null"
                  placement="bottom"
                  title="分配角色"
                  :width="660"
                  trigger="click"
              >
                <template #reference>
                  <el-button v-if="menus.indexOf('sys:user:addRole') !== -1" @click="clickUpdateRole(scope.row.roleList)" class="button-new-tag ml-1" size="small">
                    分配角色
                  </el-button>
                  <el-button v-else disabled class="button-new-tag ml-1" size="small">
                    分配角色
                  </el-button>
                </template>
                <el-transfer
                    v-model="roleForm.ridArr"
                    style="text-align: left; display: inline-block"
                    filterable
                    :render-content="renderFunc"
                    :titles="['未选择', '已选择']"
                    :button-texts="['To left', 'To right']"
                    :format="{
                    noChecked: '${total}',
                    hasChecked: '${checked}/${total}',
                  }"
                    :data="roleForm.data"
                >
                </el-transfer>
                <div style="width: fit-content;margin: 0 auto">
                  <el-button @click="clickUpdateUserRole(scope.row.uid)">提交</el-button>
                </div>
              </el-popover>
              <div v-else>
                <el-button @click="clickAddAdmin(scope.row.uid)" v-if="menus.indexOf('sys:user:addRole') !== -1" size="small">转变为管理员</el-button>
                <el-button v-else disabled size="small">转变为管理员</el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </el-card>
  <div class="pagination">
    <el-pagination v-model:current-page="searchBox.current" :page-size="searchBox.size" background layout="prev, pager, next" :total="tableData.total">
    </el-pagination>
  </div>

<!-- 转变为管理员的弹窗-->
  <el-dialog @closed="closeDialog" title="添加角色" :width="750" v-model="addAdminForm.isVisible">
    <el-form>
      <el-form-item>
        <el-transfer
            v-model="addAdminForm.ridArr"
            style="margin: 0 auto;text-align: left; display: inline-block"
            filterable
            :render-content="renderFunc"
            :titles="['未选择', '已选择']"
            :button-texts="['To left', 'To right']"
            :format="{
                    noChecked: '${total}',
                    hasChecked: '${checked}/${total}',
                  }"
            :data="roleForm.data"
        >
        </el-transfer>
        <div style="width: fit-content;margin: 0 auto">
          <el-button @click="subAddAdmin">提交</el-button>
        </div>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script setup lang="ts">
import {onMounted, reactive, ref, VNode, VNodeProps, watch} from "vue";
import {banUser, getUserList} from "../../../axios/network/user";
import {onBeforeRouteUpdate, useRouter} from "vue-router";
import type { ElTable } from 'element-plus'
import {getAllRoleList, updateUserRole} from "../../../axios/network/role";
import stores from "../../../stores/stores";
import {ElMessage} from "element-plus";
const router = useRouter()
const {search,banFlag,current} = router.currentRoute.value.query
const menus = JSON.stringify(stores.getters.getMenus)
const searchBox = reactive({
  search: search,
  banFlag: isNaN(Number(banFlag))?-1:Number(banFlag),
  current: isNaN(Number(current))?0:Number(current),
  size: 10
})

const roleForm = reactive<any>({
  ridArr:[],
  data:[]
})
const renderFunc = (
    h: (type: string, props: VNodeProps | null, children?: string) => VNode,
    option:any
) => {
  return h('span', null, option.key+'. '+option.label)
}

const clickUpdateRole = (roleList:any[]) => {
  getAllRoleList().then(res => {
    let {data} = res.data;
    let optData:any[] = []
    data.forEach((item:any) => {
        let obj = {
          key:item.rid,
          disabled: false,
          label:item.roleName
        }
        if (item.rid == 1){
          obj.disabled = true
        }
        optData.push(obj)
    })
    roleForm.data = optData
    console.log("roleList:=",roleList)
    roleList.forEach((item:any) => {
      roleForm.ridArr.push(Number(item.rid))
    })

  })
}


const multipleSelection = ref<any[]>([])


const handleSelectionChange = (val: any[]) => {
  multipleSelection.value = val
}

const clickBanBtn = () =>{
  if(multipleSelection.value == null || multipleSelection.value.length === 0){
    ElMessage.error('您还没选择用户呢')
  }else {
    banUser(multipleSelection.value).then(res => {
      initData()
      ElMessage.success('操作成功')
    })
  }
}

const tableData = reactive({
  list:[],
  total:0
})

const banFlagArr = ref([
  {
    id:-1,
    name: '全部'
  },{
    id: 0,
    name: '正常',
  },{
    id: 1,
    name: '封禁'
  }
])

const initData = () => {
  getUserList(searchBox).then(res => {
    const {data} = res.data
    tableData.total = data.total
    let {records} = data
    records.forEach((item:any) => {
      let {roleName,rid} = item
      if (roleName == null){
        delete item.roleName
        delete item.rid
        item.roleList = []
      }else {
        let roleNameList = roleName.split(",")
        let ridList = rid.split(",")
        let roleList = []
        for (let i = 0; i < roleNameList.length; i++) {
          roleList.push({
            roleName: roleNameList[i],
            rid:ridList[i]
          })
        }
        delete item.roleName
        delete item.rid
        item.roleList = roleList
      }
    })
    console.log('records:=',records)
    tableData.list = records
  })
}

onMounted(()=>{
  initData()
})
onBeforeRouteUpdate(()=>{
  console.log('监听到')
  initData()
})
const clickSearch = () => {
  router.push({
    path:router.currentRoute.value.path,
    query:{
      search:searchBox.search,
      banFlag:searchBox.banFlag,
      current:searchBox.current
    }
  })
}
watch(()=>searchBox.current,()=>{
  clickSearch()
})

const clickUpdateUserRole = (uid:number) => {
  updateUserRole({
    uid: uid,
    ridArr: roleForm.ridArr
  }).then(res => {
    if (res.data.data){
      ElMessage.success('添加成功')
      initData()
    }else {
      ElMessage.error('添加失败')
    }
  })
}

const addAdminForm = reactive({
  uid:-1,
  ridArr:[],
  isVisible: false
})
const clickAddAdmin = (uid:number) => {
  addAdminForm.uid = uid
  addAdminForm.isVisible = !addAdminForm.isVisible
  getAllRoleList().then(res => {
    let {data} = res.data;
    let optData:any[] = []
    data.forEach((item:any) => {
      let obj = {
        key:item.rid,
        disabled: false,
        label:item.roleName
      }
      if (item.rid == 1){
        obj.disabled = true
      }
      optData.push(obj)
    })
    roleForm.data = optData
  })
}
const subAddAdmin = () => {
  updateUserRole(addAdminForm).then(res => {
    const {data} = res.data
    if (data){
      ElMessage.success('添加成功')
    }else {
      ElMessage.error('添加失败')
    }
    initData()
    closeDialog()
  })
}
const closeDialog = () => {
  addAdminForm.uid = -1
  addAdminForm.ridArr = []
  addAdminForm.isVisible = false
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
