import {FC, ReactElement, useEffect, useRef, useState} from 'react';
import "./index.less"
import {Typography, Input, Table, Pagination, Tag} from 'antd';
import {getProblemList} from "../../../axios/network/problem";
import {Link, useLocation, useSearchParams} from "react-router-dom";

import 'font-awesome/less/font-awesome.less';
import 'react-fontawesome';
import {useNavigateParams} from "../../../utils/hooks";
const {Title} = Typography
const {Search} = Input
const { Column} = Table;

interface IProps{

}


const ProblemList:FC<IProps> = (props):ReactElement => {

    const [data,setData] = useState(new Array<any>())
    let [searchParam] = useSearchParams()
    let navigateParams = useNavigateParams()
    let pathname = useLocation().pathname
    let searchRef:any = useRef()

    let [search,setSearch] = useState(searchParam.get("search"))
    let [current,setCurrent] = useState(Number(searchParam.get("current")))
    let [total,setTotal] = useState(1)



    const onChange = (currentPage:any) => {
        setCurrent(currentPage)
        navigateParams(pathname,{
            current:currentPage,
            search
        })
    }
    const onSearch = (text:any) => {
        setSearch(text)
        console.log("调用了onSearch：=",search," current:=",current)
        navigateParams(pathname,{
            current,
            search:text
        })
    }
    useEffect(()=>{
        //路由取出search，current
        if (isNaN(current)){
            setCurrent(1)
        }
        getProblemList({
            search,
            size:10,
            current
        }).then(res=>{
            const {data} = res.data
            if (search != null && search !== ''){
                searchRef.current.state.value = search
                console.log(searchRef)
            }
            setData(data.records)
            setTotal(data.total)
        })
    },[current, search])


    document.title = 'CrowOj - 题库'
    return (
        <div className={"problemList"}>
            <div className="search">
                <Title level={2}>CrowOj在线题库</Title>
                <div className={"searchBox"}>
                    <Search
                        ref={searchRef}
                        placeholder="搜索题号、标题、题目来源、算法"
                        allowClear
                        size="large"
                        onSearch={onSearch}
                    />
                </div>
            </div>
            <div className="table">
                <Table dataSource={data} pagination={false}
                    rowKey={record => record.pid}
                >
                    <Column width={"5%"} dataIndex="isAc" key="isAc" render={
                        (isAc)=> isAc !== -1 ? <i style={{color: "green"}} className={"fa fa-check"}/> : null
                    } />
                    <Column width={"8%"} title="#" dataIndex="pid" key="pid" />
                    <Column width={"57%"} title="标题" dataIndex="title" key="title"
                            render={
                                (text,r:any)=>(
                                    <Link to={pathname + "/"+r.pid}>{text}</Link>
                                )
                            }
                    />
                    <Column width={"15%"} title="通过率" dataIndex="acRate" key="acRate" render={
                        (text,record:any)=>{
                            let acRate = record.acceptedCount/record.tryCount * 100
                            if (isNaN(acRate)) acRate = 0
                            return <>{acRate.toFixed(2) + '%'}</>
                        }
                    }/>
                    <Column width={"15%"} title="难度" dataIndex="level" key="level" render={
                        (level)=>{
                            if (level === 0){
                                return <Tag color="#87d068">简单</Tag>
                            }else if (level === 1){
                                return <Tag color="#F0AD4E">中等</Tag>
                            }else {
                                return <Tag color="#D9534F">困难</Tag>
                            }
                        }
                    } />
                </Table>
            </div>
            <div className={"pWrap"}>
                <div className={"pagination"}>
                    <Pagination defaultCurrent={current < 1 ? 1:current} total={total} showSizeChanger={false}
                        showQuickJumper onChange={onChange}
                    />
                </div>
            </div>
        </div>
    );
};

export default ProblemList;
