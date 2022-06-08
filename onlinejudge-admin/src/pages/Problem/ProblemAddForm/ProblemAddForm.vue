<template>

  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span>添加题目</span>
      </div>
    </template>

    <div class="problemFormItem">
      <div class="title">
        标题
      </div>
      <div class="content">
        <el-input v-model="problemForm.title" />
      </div>
    </div>

    <div class="problemFormItem">
      <div class="title">
        等级
      </div>
      <div class="content">
        <el-select v-model="problemForm.level" placeholder="选择等级">
          <el-option
              v-for="item in optObj.levelArr"
              :key="item.id"
              :label="item.name"
              :value="item.id"
          />
        </el-select>
      </div>
    </div>
    <div class="problemFormItem">
      <div class="title">
        时间限制(ms)
      </div>
      <div class="content">
        <el-input-number :min="0" v-model="problemForm.timeLimit" />
      </div>
    </div>
    <div class="problemFormItem">
      <div class="title">
        内存限制(kb)
      </div>
      <div class="content">
        <el-input-number :min="0" v-model="problemForm.memoryLimit" />
      </div>
    </div>
    <div class="problemFormItem">
      <div class="title">
        栈内存限制(kb)
      </div>
      <div class="content">
        <el-input-number :min="0" v-model="problemForm.stackLimit" />
      </div>
    </div>

    <div class="problemFormItem">
      <div class="title">
        题目类型
      </div>
      <div class="content">
        <el-select v-model="problemForm.problemType" placeholder="选择题目类型">
          <el-option
              v-for="item in optObj.pTypeArr"
              :key="item.id"
              :label="item.name"
              :value="item.id"
          />
        </el-select>
      </div>
    </div>

    <el-dialog
      v-model="dialogVisible"
      width="480px"
      title="编辑测试数据"
    >
      <el-form
          label-width="100px"
          style="max-width: 460px"
      >
        <el-form-item label="标准输入">
          <el-input
              v-model="testCaseForm.stdin"
              type="textarea"
              placeholder="Please input"
          />
        </el-form-item>
        <el-form-item label="标准输出">
          <el-input
              v-model="testCaseForm.stdout"
              type="textarea"
              placeholder="Please input"
          />
        </el-form-item>
        <el-form-item label="">
          <el-button @click="clickAddTestCase">提交</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <div class="problemFormItem">
      <div class="title">
        测试数据
      </div>
      <div class="content">
        <el-tag
            style="margin-right: 10px;margin-bottom: 10px"
            closable
            @close="deleteTestCase(index)"
            v-for="(item,index) in problemForm.testCase"
            :key="index">
          测试数据{{item}}
        </el-tag>
        <el-button @click="dialogVisible = !dialogVisible" class="button-new-tag ml-1" size="small" >
          添加
        </el-button>
      </div>
    </div>

    <div class="problemFormItem">
      <div class="title">
        题面
      </div>
      <div class="content">
        <md-editor v-model="problemForm.description" @onUploadImg="onUploadImg" :toolbars="toolBar" />
      </div>
    </div>



    <div>
      <el-button @click="clickAddProblem" type="primary">添加</el-button>
    </div>

  </el-card>
</template>

<script lang="ts" setup>
import MdEditor from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
import {toolBar} from '../../../assets/ts/const';
import 'element-plus/es/components/message/style/css'
import {reactive, ref} from "vue";
import {uploadFile} from "../../../axios/network/user";
import {ElMessage} from "element-plus";
import {saveOrUpdateProblem} from "../../../axios/network/problem";




const dialogVisible = ref(false)

const clickAddProblem = () => {
  saveOrUpdateProblem(problemForm).then(res => {
    ElMessage.success("添加成功")
  })
}

const testCaseForm = reactive({
  stdin:'',
  stdout:''
})

const deleteTestCase = (index:number) => {
  problemForm.testCase.splice(index,1)
}

const clickAddTestCase = () => {
  problemForm.testCase.push({
    stdin: testCaseForm.stdin,
    stdout:testCaseForm.stdout
  })
  testCaseForm.stdin = ''
  testCaseForm.stdout = ''
  dialogVisible.value = !dialogVisible.value
}


const deBugReactive = reactive({
  isVisible:false,
  code: '',
  cmOptions: {
    mode: "text/javascript", // 语言模式
    theme: "default", // 主题
    lineNumbers: true, // 显示行号
    smartIndent: true, // 智能缩进
    indentUnit: 2, // 智能缩进单位为4个空格长度
    foldGutter: true, // 启用行槽中的代码折叠
    styleActiveLine: true, // 显示选中行的样式
  }
})


const problemForm = reactive<any>({
  title:'',
  level:0,
  timeLimit:0,
  memoryLimit:0,
  stackLimit:0,
  problemType:0,
  description: `
建议使用此模板来编写题面，这一块为题目描述

## 输入格式

这一部分介绍输入格式

## 输出格式

这一部分介绍输出格式

## 数据范围

这一部分介绍题目使用的测试数据的范围

## 输入样例

> 这一部位为标准输入的样例

## 输出样例

> 这一部位为标准输出样例`,
  testCase: []
})

const optObj = reactive({
  levelArr:[
    {
      id:0,
      name:'简单'
    },
    {
      id:1,
      name:'普通'
    },
    {
      id:2,
      name:'困难'
    }
  ],
  pTypeArr:[
    {
      id:0,
      name:'默认(开放提交)'
    },
    {
      id:1,
      name:'不开放提交(比赛专用)'
    }
  ]
})

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
.problemFormItem{
  .title{
    font-size: 1.2em;
  }
  .content{
    margin-top: 10px;
  }
  margin-bottom: 10px;

}

</style>
