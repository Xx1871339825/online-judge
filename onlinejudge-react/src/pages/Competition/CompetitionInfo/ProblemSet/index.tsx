import {FC, ReactElement, useEffect, useState} from 'react';
import {Link, useLocation, useParams} from "react-router-dom";
import {getProblemListByCid} from "../../../../axios/network/competition";
import 'font-awesome/less/font-awesome.less';
import 'react-fontawesome';
import {Table, Tag} from "antd";
const { Column} = Table;

interface IProps{

}

const ProblemSet:FC<IProps> = (props):ReactElement => {
    const {id} = useParams()
    const [tableData,setTableData] = useState(new Array<any>())
    const pathname =  useLocation().pathname.replace('/problem-set','')
    useEffect(()=>{
        getProblemListByCid(id).then(res => {
            const {data} = res.data
            console.log(data)
            setTableData(data)
        })
    },[])
    return (
        <div>
            <Table dataSource={tableData} pagination={false}
                   rowKey={record => record.pid}
            >
                <Column width={"5%"} dataIndex="isAc" key="isAc" render={
                    (isAc)=> isAc !== -1 ? <i style={{color: "green"}} className={"fa fa-check"}/> : null
                } />
                <Column width={"8%"} title="#" dataIndex="pid" key="pid" />
                <Column width={"57%"} title="标题" dataIndex="title" key="title"
                        render={
                            (text,r:any)=>(
                                <Link to={pathname + "/p"+r.pid}>{text}</Link>
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
    );
};

export default ProblemSet;
