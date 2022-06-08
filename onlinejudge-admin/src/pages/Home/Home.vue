<template>
  <el-container class="layout-container-demo">
    <el-header style="height: 60px; line-height: 60px">
      CrowOj后台管理
      <el-button @click="clickSignOut" style="float: right;margin-top: 15px" type="danger">注销</el-button>
    </el-header>
    <el-container>
      <el-aside>
        <div @click="clickCollapseBtn" class="collapseBtn">
          <el-icon>
            <expand v-if="isCollapse"/>
            <fold v-else/>
          </el-icon>
        </div>
        <el-menu
            default-active="2"
            class="el-menu-vertical-demo"
            :collapse="isCollapse"
        >
          <TreeMenuItem  :list="list"/>
        </el-menu>
      </el-aside>
      <el-main><RouterView /></el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
  import TreeMenuItem from "../../components/TreeMenuItem/TreeMenuItem.vue";
  import {ref} from "vue";
  import {useStore} from "vuex";
  import {getTree} from "../../utils/utils";
  const stores = useStore()
  const menus = stores.getters.getMenus
  const list = getTree(menus)
  const isCollapse = ref(false)
  const clickCollapseBtn = () => {
    isCollapse.value = !isCollapse.value
  }

  const clickSignOut = () =>{
    stores.commit('signOut')
    window.location.reload()
  }


</script>

<style lang="scss" scoped>
@import "Home.scss";
</style>
