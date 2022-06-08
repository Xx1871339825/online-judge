import {FC, ReactElement, useEffect, useState} from 'react';
import "./index.less"
import {useParams} from "react-router-dom";
import {getProblem, submitProblem} from "../../../../axios/network/problem";
import Editor from 'md-editor-rt';
import 'md-editor-rt/lib/style.css';

import {UnControlled as CodeMirror} from "react-codemirror2";
import 'codemirror/lib/codemirror.css';
import 'codemirror/lib/codemirror.js';
// 主题风格
import 'codemirror/theme/idea.css';
// 代码模式，clike是包含java,c++等模式的
import 'codemirror/mode/clike/clike.js';
import 'codemirror/mode/css/css';
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
import {Select, Button, message} from 'antd';
import {Tag, Typography} from "antd";
import {useStore} from "../../../../stores/store.context.provider";
import {languageList} from "../../../../assets/ts/const";
import {cSubmit, getCompetitionProblemInfo} from "../../../../axios/network/competition";
const { Option } = Select;
const {Title} = Typography
interface IProps{

}

const ProblemInfo:FC<IProps> = (props):ReactElement => {
    const {pid,id} = useParams()
    const [problem,setProblem] = useState<any>({})
    const [code,setCode] = useState<string>("")
    const [language,setLanguage] = useState(languageList[0].name)
    const [status,setStatus] = useState<any>(null)

    const [loading,setLoading] = useState(false)
    const {userStore,modalStore} = useStore()

    const myMessage = message

    const changeCode = (editor:any, data:any, value:any) => {
        setCode(value)
    }

    const changeLanguage = (value:any) => {
        setLanguage(value)
    }

    const submitBtn = () => {
        //先判断是否登录
        if (userStore.accessToken == null || userStore.accessToken === ''){
            // myMessage.destroy()
            // myMessage.error('请先登录')
            modalStore.visible = true
        }else {
            if(code == null || code === ''){
                myMessage.destroy()
                myMessage.error("提交的代码不能为空")
                return
            }
            setLoading(true)
            cSubmit({cid:Number(id),pid:Number(pid),code,language}).then(res => {
                console.log(res.data)
                setStatus(res.data.data)
                setLoading(false)
            }).catch(err => {
                console.log(err)
                setLoading(false)
            })
        }
    }

    useEffect(()=>{
        getCompetitionProblemInfo(id,pid).then(res => {
            let {data} = res.data
            if (data == null){
                //跳转到404
                // throw new Error("404")
                data = {}
            }
            if (!data.description){
                data.description = ""
            }
            if (!data.inputDescription){
                data.inputDescription = ""
            }
            if (!data.outputDescription){
                data.outputDescription = ""
            }
            if (!data.inputExample){
                data.inputExample = ""
            }
            if (!data.outputExample){
                data.outputExample = ""
            }
            if (!data.tips){
                data.tips = ""
            }

            console.log(data)
            setProblem(data)
        })
    },[pid])
    return (
        <div className={'cProblemInfo'}>
            <Title level={2}>{problem.pid}. {problem.title}</Title>
            <div className={"descriptionBox"}>
                <div className={"left"}>
                    <Editor
                        modelValue={problem.description}
                        previewOnly
                    />
                </div>
                <div className={"right"}>
                    <div className={"infoItem bgColor"}>
                        <span>难度: {problem.level === 0? ( <Tag color="#87d068">简单</Tag>):problem.level === 1? (<Tag color="#F0AD4E">中等</Tag>):(<Tag color="#D9534F">困难</Tag>)}
                        </span>
                    </div>
                    <div className={"infoItem"}>
                        <span>时/空限制(ms/kb): {problem.timeLimit}/{problem.memoryLimit}</span>
                    </div>
                    <div className={"infoItem bgColor"}>
                        <span>总通过数: {problem.acceptedCount}</span>
                    </div>
                    <div className={"infoItem"}>
                        <span>总尝试数: {problem.tryCount}</span>
                    </div>
                </div>
            </div>
            <div className={"codeBar"}>
                <div className={"codeSelect"}>
                    <Select defaultValue={languageList[0].name} style={{ width: 120,marginRight:'10px'}} onChange={changeLanguage}>
                        {languageList.map((item,index) => <Option key={index} value={item.name}>{item.name}</Option>)}
                    </Select>
                </div>
            </div>
            <div className={"codeEditor"}>
                <CodeMirror
                    onChange={changeCode}
                    options={{
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
                        autoCloseBrackets: true //键入时将自动关闭()[]{}''""
                    }}
                />
            </div>
            <div className={"submitBox"}>
                {status !== null ? (
                    status.statusNo === 0 ? (
                        <Tag color={'#87d068'}>{status.status}</Tag>
                    ): status.statusNo === 1 ? (
                        <Tag color={'#D9534F'}>{status.status}</Tag>
                    ): status.statusNo === 2 ? (
                        <Tag color={'#F0AD4E'}>{status.status}</Tag>
                    ): (<Tag color={'#F0AD4E'} >{status.status}</Tag>)
                ) : null}
                <Button loading={loading} onClick={submitBtn} type="primary" size={"large"} style={{float:'right'}} >
                    提交答案
                </Button>
            </div>
        </div>
    );
};

export default ProblemInfo;
