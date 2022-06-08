<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span>问题管理</span>
      </div>
    </template>
    <div class="settingList">
      <div class="searchBox">
        <div class="itemBox">
          <div class="item" style="width: 240px;">
            <el-input v-model="searchBox.search" placeholder="输入标题、标签、id或创建者" clearable />
          </div>
          <div class="item">
            <el-select v-model="searchBox.problemType"  placeholder="选择类型">
              <el-option
                v-for="item in option.optionList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </div>
          <div class="item">
            <el-button type="primary" @click="clickSearch" >搜索</el-button>
          </div>
        </div>
      </div>
      <div class="tableBox">
        <el-table :data="tableData.list" stripe style="width: 100%">
          <el-table-column prop="pid" width="80" label="#"/>
          <el-table-column prop="nickname" label="创建的用户" />
          <el-table-column prop="title" label="标题" />
          <el-table-column prop="problemType" label="题目类型">
            <template #default="scope">
              <span v-if="scope.row.problemType === 0">
                默认(开放提交)
              </span>
              <span v-else>
                比赛专用
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="通过率">
            <template #default="scope">
              <el-tag type="success">
                {{isNaN(scope.row.acceptedCount/scope.row.tryCount)? '0.00%':(scope.row.acceptedCount/scope.row.tryCount * 100).toFixed(2) + '%'}}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="level" label="难度">
            <template #default="scope">
              <el-tag effect="dark" type="success" v-if="scope.row.level === 0">简单</el-tag>
              <el-tag effect="dark" type="warning" v-else-if="scope.row.level === 1">普通</el-tag>
              <el-tag effect="dark" type="danger" v-else>困难</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="tag" label="算法标签">
            <template #default="scope">
              <el-tag
                  style="margin-left: 10px;margin-bottom: 10px"
                  effect="dark"
                  type="success"
                  v-for="(item,index) in scope.row.tag"
                  :key="index"
              >
                {{item}}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button @click="clickUpdate(scope.row.pid)" v-if="menus.indexOf('sys:problem:update') !== -1"  type="primary">修改</el-button>
              <el-button v-else disabled type="primary" >修改</el-button>
              <el-popconfirm title="确认删除¿" >
                <template #reference>
                  <el-button v-if="menus.indexOf('sys:problem:delete') !== -1" type="danger">删除</el-button>
                  <el-button v-else disabled type="danger">删除</el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <div class="pagination">
      <el-pagination v-model:current-page="searchBox.current" :page-size="searchBox.size" background layout="prev, pager, next" :total="tableData.total">
      </el-pagination>
    </div>
  </el-card>
  <el-dialog
    title="修改题目"
    v-model="dialogVisible.updateDialog"
  >
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
        v-model="dialogVisible.testCaseDialog"
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
            @close="deleteTestCase(index)"
            style="margin-right: 10px;margin-bottom: 10px"
            closable
            v-for="(item,index) in problemForm.testCase"
            :key="index">
          测试数据{{item}}
        </el-tag>
        <el-button @click="dialogVisible.testCaseDialog = !dialogVisible.testCaseDialog" class="button-new-tag ml-1" size="small" >
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
      <el-button @click="clickSubmitUpdate"  type="primary">修改</el-button>
    </div>

  </el-dialog>
</template>

<script setup lang="ts">
import {onMounted, reactive, ref, watch} from "vue";
import {onBeforeRouteUpdate, useRouter} from "vue-router";
import {getProblemInfo, getProblemList, saveOrUpdateProblem} from "../../../axios/network/problem";
import MdEditor from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';
import stores from "../../../stores/stores";
import {uploadFile} from "../../../axios/network/user";
import {ElMessage} from "element-plus";
const menus = JSON.stringify(stores.getters.getMenus)
//弹窗表单相关
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
const dialogVisible = reactive({
  testCaseDialog:false,
  updateDialog:false
})
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
const deleteTestCase = (index:number) => {
  problemForm.testCase.splice(index,1)
}
const testCaseForm = reactive({
  stdin:'',
  stdout:''
})
const clickAddTestCase = () => {
  problemForm.testCase.push({
    stdin: testCaseForm.stdin,
    stdout:testCaseForm.stdout
  })
  testCaseForm.stdin = ''
  testCaseForm.stdout = ''
  dialogVisible.testCaseDialog = !dialogVisible.testCaseDialog
}
const clickSubmitUpdate = () => {
  saveOrUpdateProblem(problemForm).then(res => {
    ElMessage.success("修改成功")
  })
}
const problemForm = reactive<any>({
  pid: -1,
  uid: -1,
  title:'',
  level:0,
  timeLimit:0,
  memoryLimit:0,
  stackLimit:0,
  problemType:0,
  description: '',
  testCase: []
})
const clickUpdate = (pid:number) => {
  dialogVisible.updateDialog = !dialogVisible.updateDialog
  getProblemInfo(pid).then(res => {
    const {data} = res.data
    problemForm.pid = data.pid
    problemForm.uid = data.uid
    problemForm.title=data.title
    problemForm.level=data.level
    problemForm.timeLimit=data.timeLimit
    problemForm.memoryLimit=data.memoryLimit
    problemForm.stackLimit=data.stackLimit
    problemForm.problemType=data.problemType
    problemForm.description=data.description
    let {testCase} = data
    testCase.forEach((i:any) => {
        delete i.id
        delete i.pid
    })
    problemForm.testCase = testCase
  })
}

//本页面
  const option = reactive({
    optionList: [
      {
        id:0,
        name: '默认',
      },
      {
        id:1,
        name:'比赛专用'
      }
    ]
  })
  const tableData = reactive({
    list:[],
    total:0
  })
  const router = useRouter()
  const {search,problemType,current} = router.currentRoute.value.query
  const searchBox = reactive({
    search:search,
    problemType: isNaN(Number(problemType))?0:Number(problemType),
    current: isNaN(Number(current))?0:Number(current),
    size:10
  })
  const initData = () => {
    getProblemList(searchBox).then(res => {
      let {data} = res.data
      let {records,total} = data
      for (let i = 0; i < records.length; i++) {
        if (records[i].tag == null){
          continue
        }
        let tagList = records[i].tag.split(',')
        let map = new Map<String,number>()
        tagList.forEach((item:any,index:any) => {
          map.set(item,index)
        })
        let mapToArr = [...map]
        tagList = []
        mapToArr.forEach(item => {
          tagList.push(item[0])
        })
        records[i].tag = tagList
      }
      tableData.list = records
      tableData.total = total
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
        problemType:searchBox.problemType,
        current:searchBox.current
      }
    })
  }
  watch(()=>searchBox.current,()=>{
    clickSearch()
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
