import {FC, ReactElement, useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import './index.less'
import { Table } from 'antd'
import {getCRankList} from "../../../../axios/network/competition";
import DynamicTable from "../../../../components/DynamicTable";
const { Column } = Table

interface IProps{

}

const Rank:FC<IProps> = (props):ReactElement => {
    const {id} = useParams()
    const [tableData,setTableData] = useState(new Array<any>())
    useEffect(() => {
        getCRankList(id).then(res => {
            setTableData(res.data.data)
            console.log(res.data)
        })
    },[id])
    return (
        <div className={'cRank'}>
            <DynamicTable tableData={tableData} cid={Number(id)}
            />
        </div>
    );
};

export default Rank;
