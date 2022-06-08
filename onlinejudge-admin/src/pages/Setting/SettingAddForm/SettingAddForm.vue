<template>

  <div class="settingFormItem">
    <div class="title">
        标题
    </div>
    <div class="content">
      <el-input v-model="settingFormReactive.title"/>
    </div>
  </div>

  <div class="settingFormItem">
    <div class="title">
      类型
    </div>
    <div class="content">
      <el-select v-model="settingFormReactive.settingType" placeholder="选择类型">
        <el-option
            v-for="item in typeArr"
            :key="item.id"
            :value="item.name"
            :label="item.name"
        />
      </el-select>
    </div>
  </div>

  <div class="settingFormItem">
    <div class="title">
      排序
    </div>
    <div class="content">
      <el-input-number v-model="settingFormReactive.sort" />
    </div>
  </div>

  <div v-if="settingFormReactive.settingType === '轮播图'" class="settingFormItem">
    <div class="title">
      图片
    </div>
    <div class="content">
      <el-upload
          class="image-uploader"
          action="#"
          :show-file-list="false"
          :before-upload="beforeUpload"
          :http-request="uploadHttp"
      >
        <img width="180" height="180" v-if="imageUrl" :src="imageUrl" class="avatar" />
        <el-icon v-else class="image-uploader-icon"><plus /></el-icon>
      </el-upload>
    </div>
  </div>
  <div v-else class="settingFormItem">
    <div class="title">
      内容
    </div>
    <div class="content">
      <md-editor @onUploadImg="onUploadImg" v-model="settingFormReactive.content" :toolbars="toolBar" />
    </div>
  </div>

  <div class="settingFormItem">
    <div class="title">
      状态
    </div>
    <div class="content">
      <el-select v-model="settingFormReactive.status" placeholder="选择状态">
        <el-option
            v-for="item in statusArr"
            :key="item.id"
            :value="item.name"
            :label="item.name"
        />
      </el-select>
    </div>
  </div>

  <el-button :loading="settingFormReactive.isLoading" @click="clickAdd" type="primary">添加</el-button>

</template>

<script lang="ts" setup>
import MdEditor from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
import 'element-plus/es/components/message/style/css'

const toolBar = ['bold',
  'underline',
  'italic',
  '-',
  'strikeThrough',
  'sub',
  'sup',
  'quote',
  'unorderedList',
  'orderedList',
  '-',
  'codeRow',
  'code',
  'link',
  'image',
  'table',
  'mermaid',
  'katex',
  '-',
  'pageFullscreen',
  'preview',
  'htmlPreview',
  'catalog',
]




import {reactive, ref} from "vue";
import { settingType, statusArr} from "../../../assets/ts/const";
import {ElMessage, UploadRequestOptions} from "element-plus";
import {uploadFile} from "../../../axios/network/user";
import {addSetting, getSetting, updateSetting} from "../../../axios/network/setting";
const typeArr = [settingType[1],settingType[2]]
const imageUrl = ref('')


const settingFormReactive = reactive({
  settingId: -1,
  title:'',
  settingType:'轮播图',
  sort:1,
  content: '',
  status:'启用',
  isLoading: false
})




const emits = defineEmits(['optStatus'])

const beforeUpload = (file:any) =>{
  console.log("拦截：",file)
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isJpgOrPng) {
    ElMessage.error('图片只能是Jpg或者Png')
  }
  if (!isLt10M) {
    ElMessage.error('图片最大只能10Mb')
  }
  return isJpgOrPng && isLt10M
}
const uploadHttp = (options: UploadRequestOptions) =>{
  const {file} = options
  uploadFile(file).then(res => {
    const {data} = res.data
    console.log('返回到的data',data)
    imageUrl.value = data
    settingFormReactive.content = data
  })
}
const clickAdd = () => {
  if (settingFormReactive.title == '' || settingFormReactive.title.length > 50){
    ElMessage.error("标题不能为空且长度不能超过50个字符")
    return
  }
  if (settingFormReactive.content == ''){
    ElMessage.error("图片或内容不能为空")
    return;
  }
  settingFormReactive.isLoading = true
    addSetting(settingFormReactive).then(() => {
      settingFormReactive.settingType = '轮播图'
      settingFormReactive.title = ''
      settingFormReactive.sort = 1
      settingFormReactive.content = ''
      settingFormReactive.status = '启用'
      imageUrl.value = ''
      emits('optStatus',true)
      ElMessage.success('提交成功')
    }).finally(()=>{
      settingFormReactive.isLoading = false
    })
}


async function onUploadImg(files: FileList, callback: (urls: string[]) => void) {
  console.log('开始调用:=',files)
  const res = await Promise.all(
      Array.from(files).map((file) => {
        return new Promise((rev, rej) => {
          uploadFile(file).then((res) => rev(res))
              .catch(error => rej(error))
        });
      })
  );
  console.log('接收到的resList',res)

  callback(res.map((item: any) => item.data.data));
}




</script>

<style scoped lang="scss">

.settingFormItem{
  .title{
    font-size: 1.2em;
  }
  .content{
    margin-top: 10px;
  }
  margin-bottom: 10px;
}


.image-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.image-uploader .el-upload:hover {
  border-color: #409eff;
}
.el-icon.image-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 180px;
  height: 180px;
  text-align: center;
}
.image {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
