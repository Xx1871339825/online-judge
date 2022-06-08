<template>
  <div class="in-coder-panel">
    <textarea ref="textarea" v-model="code"></textarea>
  </div>
</template>

<script>
// 引入全局实例
import _CodeMirror from 'codemirror/lib/codemirror'

// 核心样式
import 'codemirror/lib/codemirror.css'
// 引入主题后还需要在 options 中指定主题才会生效
// 需要引入具体的语法高亮库才会有对应的语法高亮效果, 目前已动态引入
// import 'codemirror/theme/midnight.css'
// 主题样式

//ctrl+空格代码提示补全
import 'codemirror/addon/hint/show-hint.css';
import 'codemirror/addon/hint/show-hint';
import 'codemirror/addon/hint/anyword-hint.js';
//代码高亮
import 'codemirror/addon/selection/active-line';
//折叠代码
import 'codemirror/addon/fold/foldgutter.css';
import 'codemirror/addon/fold/foldcode.js';
import 'codemirror/addon/fold/foldgutter.js';
import 'codemirror/addon/fold/brace-fold.js';
import 'codemirror/addon/fold/comment-fold.js';
import 'codemirror/addon/edit/closebrackets.js';
import 'codemirror/addon/edit/matchbrackets.js';

import { reactive, defineComponent, toRefs, getCurrentInstance, onMounted, onBeforeUnmount } from 'vue'

// codemirror 官方其实支持通过 /addon/mode/loadmode.js 和 /mode/meta.js 来实现动态加载对应语法高亮库
// 但 vue 貌似没有无法在实例初始化后再动态加载对应 JS ，所以此处才把对应的 JS 提前引入
import 'codemirror/mode/clike/clike.js'
import 'codemirror/theme/idea.css';

// 尝试获取全局实例
const CodeMirror = window.CodeMirror || _CodeMirror
let coder = null // 编辑器实例

export default defineComponent({
  name: 'codeEditor',
  props: {
    value: {
      type: String,
      default: ''
    },
    scene: {
      type: String,
      default: 'add' // add: 新增； edit: 编辑； look: 查看
    },
    eventType: {
      type: String,
      default: 'blur' // 可用事件'change', 'blur'等等；具体参考codemirror文档
    },
    theme: {
      type: String,
      default: 'idea' // 编辑器主题色
    }
  },

  setup(props, { emit }) {
    const { proxy } = getCurrentInstance()
    const data = reactive({
      code: props.value, // 内部真实的内容
      // 默认配置
      options: {
        mode: {name:'text/x-java'},
        theme: 'idea',
        autofocus:true,//自动获取焦点
        styleActiveLine:true,//光标代码高亮
        lineNumbers: true, //显示行号
        smartIndent:true,  //自动缩进
        //start-设置支持代码折叠
        lineWrapping:true,
        foldGutter:true,
        gutters:['CodeMirror-linenumbers','CodeMirror-foldgutter'],//end
        extraKeys:{
          "Tab":"autocomplete",
        },
        matchBrackets: true,  //括号匹配，光标旁边的括号都高亮显示
        autoCloseBrackets: true, //键入时将自动关闭()[]{}''""
        readOnly: (props.scene === 'add' || props.scene === 'edit') ? false : 'nocursor' // true: 不可编辑  false: 可编辑 'nocursor' 失焦,不可编辑
      },
      // 初始化
      initialize: () => {
        // 初始化编辑器实例，传入需要被实例化的文本域对象和默认配置
        coder = CodeMirror.fromTextArea(proxy.$refs.textarea, data.options)
        // 此处也可使用'change'事件，不过每次书写的过程中都会触发，为了提高性能，故默认使用'blur'
        coder.on(props.eventType, coder=> {
          emit('update:value', coder.getValue())
        })
      },
      // 动态引入语法高亮库
      importThemDynamic: () => {
        return new Promise(resolve => {
          import('codemirror/theme/idea.css')
          resolve()
        })
      }
    })
    onMounted(() => {
      // console.log('value:', props.value)
      data.importThemDynamic().then(()=>{
        data.initialize()
      })
    })
    onBeforeUnmount(()=> {
      coder.off(props.eventType)
    })
    return {
      ...toRefs(data)
    }
  }
})
</script>

<style lang="scss"> // 此处不可使用"scoped"
.in-coder-panel{
  flex-grow: 1;
  display: flex;
  position: relative;
  .CodeMirror {
    flex-grow: 1;
    text-align: left !important;
    z-index: 999;
    .CodeMirror-code {
      line-height: 19px;
    }
  }
}
</style>
