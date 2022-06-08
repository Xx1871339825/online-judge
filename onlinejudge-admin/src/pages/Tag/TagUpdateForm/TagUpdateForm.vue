<template>
  <div class="settingFormItem">
    <div class="title">
      标题
    </div>
    <div class="content">
      <el-input v-model="tagForm.title"/>
    </div>
  </div>

  <div class="settingFormItem">
    <div class="title">
    </div>
    <div class="content">
      <el-popover
          placement="bottom"
          title="选择题目"
          :width="660"
          trigger="click"
      >
        <template #reference>
          <el-button class="button-new-tag ml-1" size="small">
            添加题目
          </el-button>
        </template>
        <el-transfer
            v-model="tagForm.pidArr"
            style="text-align: left; display: inline-block"
            filterable
            :render-content="renderFunc"
            :titles="['未选择', '已选择']"
            :button-texts="['To left', 'To right']"
            :format="{
                noChecked: '${total}',
                hasChecked: '${checked}/${total}',
             }"
            :data="data"
        >
        </el-transfer>
      </el-popover>
    </div>
  </div>
  <el-button @click="clickUpdate" type="primary">提交</el-button>
</template>

<script setup lang="ts">
import {onMounted, reactive, ref, watch} from 'vue'
import type { VNode, VNodeProps } from 'vue'
import {getAllProblemList} from "../../../axios/network/problem";
import {ElMessage} from "element-plus";
import {getTag, updateTag} from "../../../axios/network/tag";

const props = defineProps({
  closeDialog:{
    type:Function,
    required:true
  },
  tid:{
    type:Number,
    required: true,
    default:()=>-1
  }
})

const reactiveProps = reactive(props)


interface Option {
  key: number
  label: string
  disabled: boolean
}

const data =  ref<Option[]>([])

onMounted(()=>{
  tagForm.tid = reactiveProps.tid
  const optData: Option[] = []
  getAllProblemList().then(res => {
    const list = res.data.data
    list.forEach((item:any) => {
      optData.push({
        key: item.pid,
        label: item.title,
        disabled:false
      })
    })
    data.value = optData
  })

  getTag(reactiveProps.tid).then(res => {
    const {data} = res.data
    if (data.pid != null&& data.pid.size != 0){
      let arr = data.pid.split(',')
      for (let i = 0; i < arr.length; i++) {
        arr[i] = Number(arr[i])
      }
      tagForm.pidArr = arr
    }

    tagForm.title = data.tagName
  })

})
const tagForm = reactive({
  tid:-1,
  pidArr:[],
  title:''
})

const renderFunc = (
    h: (type: string, props: VNodeProps | null, children?: string) => VNode,
    option: Option
) => {
  return h('span', null, option.key+'. '+option.label)
}

const clickUpdate = () =>{
  if (tagForm.title === '' || tagForm.title.length > 10){
    ElMessage.error('标签不能为空，且长度不能超过10个字符')
    return
  }
  updateTag(tagForm).then(() => {
    ElMessage.success('提交成功')
    props.closeDialog()
  })
}

watch(()=>reactiveProps.tid,()=>{
  if (reactiveProps.tid !== -1){
    tagForm.tid = reactiveProps.tid
    getTag(reactiveProps.tid).then(res => {
      const {data} = res.data
      if (data.pid != null&& data.pid.size != 0){
        let arr = data.pid.split(',')
        for (let i = 0; i < arr.length; i++) {
          arr[i] = Number(arr[i])
        }
        tagForm.pidArr = arr
      }
      tagForm.title = data.tagName
    })
  }
})

</script>

<style scoped lang="scss">
@import "src/assets/css/const";
.settingFormItem{
  .title{
    font-size: 1.2em;
  }
  .content{
    margin-top: 10px;
  }
  margin-bottom: 10px;
}
.searchBox{
  line-height: 60px;
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
</style>
