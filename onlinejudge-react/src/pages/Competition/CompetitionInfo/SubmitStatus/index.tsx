import {FC, ReactElement, useEffect, useState} from 'react';
import {Button, Input, InputNumber, Pagination, Select, Table, Typography} from "antd";
import {languageList, statusArr} from "../../../../assets/ts/const";
import {SyncOutlined} from "@ant-design/icons";
import {Link, useLocation, useParams, useSearchParams} from "react-router-dom";
import StatusTag from "../../../../components/StatusTag";
import {useNavigateParams} from "../../../../utils/hooks";
import {getCSubmitList, getStatusList} from "../../../../axios/network/problem";
import './index.less'
const {Column} = Table
const { Option } = Select;
const {Title} = Typography


interface IProps{

}

const SubmitStatus:FC<IProps> = (props):ReactElement => {

    const {id} = useParams()
    const [data,setData] = useState<any>([])
    const [total,setTotal] = useState<any>(0)
    let [searchParam] = useSearchParams()
    let navigateParams = useNavigateParams()
    let pathname = useLocation().pathname

    const [params,setParams] = useState<any>({
        pid:searchParam.get("pid"),
        nickname:searchParam.get("nickname"),
        status:searchParam.get("status"),
        current:Number(searchParam.get("current")) < 1 ? 1:Number(searchParam.get("current"))
    })

    useEffect(()=>{
        console.log("effect:",params)
        getCSubmitList(id,params).then(res => {
            const {data} = res.data
            setData(data.records)
            setTotal(data.total)
            console.log(data.records)
        })
    },[params.current])

    const pidInput = (value:any) => {
        if (value === ''){
            setParams({...params,pid: null})
            return
        }
        setParams({...params,pid: value})
    }

    const nicknameInput = (event:any) => {
        setParams({...params,nickname:event.target.value})
    }

    const statusSelect = (value:any) =>{
        if (value === ''){
            setParams({...params,status:null})
            return
        }
        setParams({...params,status:value})
    }

    const onChange = (currentPage:any) => {
        setParams({
            ...params,
            current:currentPage
        })
        navigateParams(pathname,{
            ...params,
            current: currentPage
        })
    }

    const onRefresh = () => {
        getCSubmitList(id,params).then(res => {
            const {data} = res.data
            setData(data.records)
            setTotal(data.total)
            console.log(data.records)
        })
    }

    const searchBtn = () => {
        setParams({
            ...params,
            current: 1
        })
        getCSubmitList(id,params).then(res => {
            const {data} = res.data
            setData(data.records)
            console.log(total)
        })
        navigateParams(pathname, {
            ...params,
            current: 1
        })
    }
    return (
        <div className={'submitList'}>
            <div className={'topBarBox'}>
                <div className={'topBar'}>
                    <div className={'tbText'}>
                        <Title level={2} style={{lineHeight:'50px'}}>状态</Title>
                    </div>
                    <div className={'tbText'}>
                        <InputNumber defaultValue={params.pid} onChange={pidInput} size={"middle"} placeholder={'题目编号'}/>
                    </div>
                    <div className={'tbText'}>
                        <Input defaultValue={params.nickname} onChange={nicknameInput} size={"middle"} placeholder={'用户'} />
                    </div>
                    <div className={'tbText'}>
                        <Select onChange={statusSelect} style={{width:'100px'}} size={"middle"} defaultValue={params.status} >
                            <Option value="">All</Option>
                            {statusArr.map(item => <Option key={item.id} value={item.id}>{item.name}</Option>)}
                        </Select>
                    </div>
                    <div className={'tbText'} style={{width:'120px'}}>
                        <Button onClick={onRefresh}  shape="circle" style={{marginRight:'10px'}}><SyncOutlined/></Button>
                        <Button onClick={searchBtn} type={'primary'}>搜索</Button>
                    </div>
                </div>
            </div>
            <div className={'statusTable'}>
                <Table  pagination={false}
                        dataSource={data}
                        rowKey={'id'}
                >
                    <Column title="id" dataIndex="id" key="id" />
                    <Column title="题目Id" dataIndex="problemId" key="problemId"
                            render={
                                pid => pid
                            }
                    />
                    <Column title="状态" dataIndex="status" key="status"
                            render={(statusNo) => <StatusTag statusNo={statusNo} />}
                    />
                    <Column title="分数" dataIndex="score" key="score" />
                    <Column title="运行时间(ms)" dataIndex="time" key="time" />
                    <Column title="运行内存(kb)" dataIndex="memory" key="memory" />
                    <Column title="语言" dataIndex="language" key="language" render={
                        (languageNO,record:any) => {
                            let lStr = ''
                            for (let languageListElement of languageList) {
                                if (languageListElement.id === languageNO){
                                    lStr = languageListElement.name
                                    break
                                }
                            }
                            return <Link to={pathname+'/'+record.id}>{lStr}</Link>
                        }
                    }/>
                    <Column title="用户" dataIndex="nickname" key="nickname"
                            render={(text,record:any) => {
                                return <Link to={'/user/' + record.userId}>{text}</Link>
                            }}
                    />
                    <Column title="提交时间" dataIndex="submitTime" key="submitTime"/>
                </Table>
            </div>
            <div className={"pWrap"}>
                <div className={"pagination"}>
                    <Pagination current={params.current} total={total}
                                showQuickJumper onChange={onChange}
                    />
                </div>
            </div>
        </div>
    );
};

export default SubmitStatus;
