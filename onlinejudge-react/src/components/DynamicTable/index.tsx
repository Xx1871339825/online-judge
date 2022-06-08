import {FC, ReactElement, useEffect, useState} from 'react';
import {Table} from "antd";
import {getProblemListByCid} from "../../axios/network/competition";
import './index.less'
const { Column } = Table
interface IProps{
    cid:number|string,
    tableData:Array<any>
}

const DynamicTable:FC<IProps> = (props):ReactElement => {
    //获取到题目，动态渲染表格
    const [column,setColumn] = useState(new Array<any>())
    useEffect(()=>{
        getProblemListByCid(props.cid).then(res => {
            const {data} = res.data
            setColumn(data)
        })
    },[props.cid])
    return (
        <>
            <Table rowKey={'rank'}
                   rowClassName={''}
                   dataSource={props.tableData} bordered>
                <Column align={"center"} width={80} title="排名" dataIndex="rank" key="rank" />
                <Column align={"center"} width={200} title="用户" dataIndex="nickname" key="nickname" />
                <Column align={"center"} width={80} title="总分" dataIndex="totalScore" key="totalScore" />
                {column.map(item => {
                    return (
                        <Column
                            filtered = {false}
                            align={"center"}
                            width={80}
                            title={'p'+item.pid}
                            dataIndex={'p'+item.pid}
                            key={'p'+item.pid}
                            onCell={(record:any) => {
                                const {submissionInfo} = record
                                let obj = submissionInfo['p'+item.pid]
                                console.log()
                                if (obj.status === 1){
                                    return {className:'ac'}
                                }else if (obj.status === 2 && obj.score > 0){
                                    return {className: 'naAc'}
                                }else if (obj.status === 2 && obj.score === 0){
                                    return {className: 'wa'}
                                }
                                return {className:''}
                            }}
                            render={(text, record:any) => {
                                const {submissionInfo} = record
                                let obj = submissionInfo['p'+item.pid]
                                return <>{obj.score}</>
                            }}
                        />
                    )
                })}
            </Table>
        </>
    );
};

export default DynamicTable;
