<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span>角色管理</span>
        <el-button v-if="menus.indexOf('sys:role:update') !== -1" style="float: right" @click="clickAddOrUpdate(-1)">新增</el-button>
        <el-button v-else style="float: right" disabled>新增</el-button>
      </div>
    </template>
    <div class="settingList">
      <div class="tableBox">
        <el-table :data="tableData.list" stripe style="width: 100%">
          <el-table-column prop="rid" width="80" label="#"/>
          <el-table-column prop="roleName" label="角色名" />
          <el-table-column prop="roleCode" label="唯一角色标识" />
          <el-table-column prop="roleDesc" label="角色描述" />
          <el-table-column prop="status" label="状态">
            <template #default="scope">
              <el-tag type="success" v-if="scope.row.status === 0">
                可用
              </el-tag>
              <el-tag v-else type="danger" >
                禁用
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button v-if="menus.indexOf('sys:role:update') !== -1"  type="text" @click="clickUpdateMenu(scope.row.rid)">分配权限</el-button>
              <el-button v-else type="text" disabled>分配权限</el-button>
              <el-button v-if="menus.indexOf('sys:role:update') !== -1" type="text" @click="clickAddOrUpdate(scope.row.rid)">修改</el-button>
              <el-button v-else disabled type="primary">修改</el-button>
              <el-button v-if="menus.indexOf('sys:role:delete') === -1" disabled  type="text">删除</el-button>
              <el-popconfirm v-else @confirm="clickDeleteRole(scope.row.rid)" title="确认删除¿" >
                <template #reference>
<!--                  <el-button  type="danger">删除</el-button>-->
                  <el-button  type="text">删除</el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <!--   修改角色菜单的弹窗-->
    <el-dialog
        width="400px"
        v-model="menuDialog.isVisible">
      <el-tree
          ref="treeRef"
          :data="menuDialog.menuList"
          show-checkbox
          node-key="id"
      />
      <div style="width: fit-content;margin: 30px auto 0;">
        <el-button @click="submitUpdateMenu" :loading="menuDialog.isLoading" type="primary">提交</el-button>
      </div>
    </el-dialog>

<!--    新增角色弹窗-->
    <el-dialog
        @closed="closeSaveOrUpdateDialog"
        width="400px"
        :title="saveOrUpdate.title"
        v-model="saveOrUpdate.isVisible">
      <el-form :model="saveOrUpdate" label-width="120px" :rules="rules" ref="formRef">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="saveOrUpdate.roleName" />
        </el-form-item>
        <el-form-item label="角色唯一标识" prop="roleCode">
          <el-input v-model="saveOrUpdate.roleCode" />
        </el-form-item>
        <el-form-item label="角色描述" prop="roleDesc">
          <el-input v-model="saveOrUpdate.roleDesc" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="saveOrUpdate.status">
            <el-option
                v-for="item in statusArr"
                :value="item.id"
                :label="item.name"
                :key="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button @click="submitSaveOrUpdate(formRef)">
            提交
          </el-button>
        </el-form-item>
      </el-form>

    </el-dialog>
  </el-card>
</template>

<script setup lang="ts">

import {onMounted, reactive, ref} from "vue";
import {deleteRole, getAllRoleList, saveOrUpdateRole, updateRoleMenu} from "../../../axios/network/role";
import stores from "../../../stores/stores";
import {getTree} from "../../../utils/utils";
import {getMenuByRid} from "../../../axios/network/menu";
import {ElMessage} from "element-plus";
const menus = JSON.stringify(stores.getters.getMenus)
const saveOrUpdate = reactive({
  rid:-1,
  title: '新增角色',
  roleName: '',
  roleCode: '',
  roleDesc: '',
  status: 0,
  isVisible:false,
})
const formRef = ref<any>(null)

const closeSaveOrUpdateDialog = () => {
  formRef.value.clearValidate(['roleName','roleCode','roleDesc','status'])
}

const submitSaveOrUpdate = (formEl: any) => {
  formEl.validate((valid:any) => {
    if (valid){
      saveOrUpdateRole(saveOrUpdate).then(() => {
        getAllRoleList().then(res => {
          const {data} = res.data
          tableData.list = data
        })
        ElMessage.success('添加成功')
      })
    }else {
      ElMessage.error('有必填项未填')
    }
  })
}

const clickDeleteRole = (rid:number) => {
  deleteRole(rid).then(res => {
    ElMessage.success('删除成功')
    getAllRoleList().then(res => {
      const {data} = res.data
      tableData.list = data
    })
  })
}

const rules = reactive({
  roleName: [{
    required: true, message: '角色名不能为空', trigger: 'blur'
  }],
  roleCode: [{
    required: true, message: '角色标识不能为空', trigger: 'blur'
  }],
  roleDesc: [{
    required: true, message: '角色名不能为空', trigger: 'blur'
  }],
  status: [{
    required: true, message: '角色名不能为空', trigger: 'blur'
  }]
})


const statusArr = ref([
  {
    id: 0,
    name: '可用'
  },{
    id: 1,
    name: '禁用'
  }
])

const clickAddOrUpdate = (rid:number) => {
  if (rid != -1){
    const obj:any = tableData.list.find((item:any) => item.rid === rid)
    saveOrUpdate.title = '修改角色'
    saveOrUpdate.rid = obj.rid
    saveOrUpdate.roleName = obj.roleName
    saveOrUpdate.roleCode = obj.roleCode
    saveOrUpdate.roleDesc = obj.roleDesc
    saveOrUpdate.status = obj.status
    saveOrUpdate.isVisible = !saveOrUpdate.isVisible
  }else {
    saveOrUpdate.title = '新增角色'
    saveOrUpdate.rid = -1
    saveOrUpdate.roleName = ''
    saveOrUpdate.roleCode = ''
    saveOrUpdate.roleDesc = ''
    saveOrUpdate.status = 0
    saveOrUpdate.isVisible = !saveOrUpdate.isVisible
  }
}


const menuDialog = reactive<any>({
  isVisible:false,
  isLoading:false,
  menuList:[],
  rid:-1
})
const treeRef = ref<any>(null)

const menuToTree = (menus:any[]) =>{
  let map = new Map<number,any>()
  menus.forEach(item => map.set(item.mid, {
    id:item.mid,
    label: item.menuName,
    children:[],
    parentId:item.parentId,
    type:item.menuType
  }))
  menus.forEach((item:any) => {
    let i = map.get(item.parentId)
    if (i != null && item.parentId !== 0){
      let parent = map.get(item.parentId)
      parent.children.push(map.get(item.mid))
    }
  })
  let mapToList = [...map]
  let treeList:any[] = [];
  mapToList.forEach(item => {
    if (item[1].parentId == 0){
      treeList.push(item[1])
    }
  })
  return treeList
}

const clickUpdateMenu = (rid:number) => {
  //获取到菜单
  const menus = stores.getters.getMenus
  menuDialog.menuList = menuToTree(menus);
  menuDialog.rid = rid
  //获取到已经拥有的
  getMenuByRid(rid).then(res => {
    const {data} = res.data
    let checkIdArr:any[] = []
    data.forEach((item:any) => {
      checkIdArr.push(item.mid)
    })
    treeRef.value.setCheckedKeys(checkIdArr)
  })
  menuDialog.isVisible = !menuDialog.isVisible
}
const submitUpdateMenu = () =>{
  menuDialog.isLoading = !menuDialog.isLoading
  updateRoleMenu({
    rid:menuDialog.rid,
    midArr: treeRef.value.getCheckedKeys()
  }).then(res => {
    const {data} = res.data
    if (data){
      ElMessage.success('提交成功')
    }else {
      ElMessage.error('提交失败')
    }
  }).finally(()=>{
    menuDialog.isLoading = !menuDialog.isLoading
  })
}

const tableData = reactive({
  list:[]
})
onMounted(()=>{
  getAllRoleList().then(res => {
    const {data} = res.data
    tableData.list = data
  })
})
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
.pagination{
  width: fit-content;
  margin: 20px auto;
}
</style>
