<template>
  <div v-for="item in props.list" :key="item.meta.mid">
    <el-menu-item
      v-if="item.children.length === 0"
      :index="item.path"
      @click="clickItem(item.path)"
    >
      <el-icon><component :is="item.meta.icon" /></el-icon>
      <template #title>
          <span>{{item.meta.name}}</span>
      </template>
    </el-menu-item>

    <el-sub-menu v-else :index="item.path">
      <template #title>
        <el-icon><component :is="item.meta.icon" /></el-icon>
        <span>{{item.meta.name}}</span>
      </template>
      <tree-menu-item v-bind="$attrs" :list="item.children"></tree-menu-item>
    </el-sub-menu>
  </div>
</template>

<script setup lang="ts">
import {defineProps} from "vue";
import {useRouter} from "vue-router";
const props = defineProps({
    list: {
      type:Array as () => Array<any>,
      required: true,
      default: () => []
    }
})
const router = useRouter()
const clickItem = (path:string) => {
  router.push(path)
}



</script>

<style lang="scss" scoped>

</style>
