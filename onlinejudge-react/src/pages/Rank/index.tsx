import {FC, ReactElement, useEffect, useState} from 'react';
import './index.less'
import {Avatar, Input, Table, Typography} from "antd";
import {Link} from "react-router-dom";
import {getRankList} from "../../axios/network/user";
import {UserOutlined} from "@ant-design/icons";
const {Title} = Typography
const { Column} = Table;
const {Search} = Input

interface IProps{

}

const Rank:FC<IProps> = (props):ReactElement => {
    document.title = 'CrowOj - 排名'
    const [params,setParams] = useState<any>({
        size:10,
        current:1,
        search: ''
    })
    const [list,setList] = useState<any>([])

    const onSearch = (search:any) => {
        setParams({
            ...params,
            search: search
        })
    }

    useEffect(()=>{
        getRankList(params).then(res => {
            let {data} = res.data
            for (let i = 0; i < data.length; i++) {
                data[i].rank = i+1
            }
            console.log(data)
            setList(data)
        })
    },[params])

    return (
        <div className={'rank'}>
            <div className="search">
                <Title level={2}>排名</Title>
                <div className={"searchBox"}>
                    <Search
                        onSearch={onSearch}
                        placeholder="搜索用户昵称"
                        allowClear
                        size="large"
                    />
                </div>
            </div>
            <div className="table">
                <Table rowKey={'uid'} dataSource={list} pagination={false}>
                    <Column title="排名" dataIndex="rank" key="rank"/>
                    <Column title="用户" dataIndex="nickname" key="nickname" render={
                        (text,record:any) => {
                            return (
                                <>
                                    <Avatar src={record.avatar} icon={<UserOutlined />} /> <Link to={'/user/' + record.uid}>{text}</Link>
                                </>
                            )
                        }
                    }/>
                    <Column title="通过" dataIndex="acceptCount" key="acceptCount"/>
                    <Column title="提交总数" dataIndex="submitCount" key="submitCount"/>
                    <Column title="通过率" dataIndex="acRate" key="acRate" render={
                        (text,record:any)=>{
                            let acRate = record.acceptCount/record.submitCount * 100
                            if (isNaN(acRate)) acRate = 0
                            return <>{acRate.toFixed(2) + '%'}</>
                        }
                    } />
                </Table>
            </div>
        </div>
    );
};

export default Rank;
