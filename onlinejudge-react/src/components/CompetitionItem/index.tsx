import {FC, ReactElement, useState} from 'react';
import './index.less'
import {Input, message, Modal, Space, Tag, Typography} from 'antd'
import {CalendarTwoTone, ClockCircleTwoTone} from "@ant-design/icons";
import {useNavigate} from "react-router-dom";
import {validCompetition} from "../../axios/network/competition";
const {Title} = Typography
const {Search} = Input
interface IProps{
    id:number,
    title:string,
    startTime:string,
    endTime:string,
    auth:number
}

const CompetitionItem:FC<IProps> = (props):ReactElement => {
    let startDate = new Date(props.startTime)
    let endDate = new Date(props.endTime)
    let duration = (endDate.getTime() - startDate.getTime()) / (1000*3600)
    let tag = props.auth === 0? <Tag color={'green'}>公开赛</Tag>:<Tag color={'red'}>私有赛</Tag>
    let color = '#ED3F14'
    let current = new Date().getTime()
    let statusTag
    if (current > endDate.getTime()){
        statusTag = <Tag color={'#ED3F14'}>已结束</Tag>
        color = '#ED3F14'
    }else{
        if (current > startDate.getTime()){
            statusTag = <Tag color={'#19BE6B'}>进行中</Tag>
            color = '#19BE6B'
        }else {
            statusTag = <Tag color={'#FF9900'}>未开始</Tag>
            color = '#FF9900'
        }
    }

    const navigate = useNavigate()
    // const [visible,setVisible] = useState(false)
    const onClickTitle = () => {
        // if (props.auth === 0){
        //     navigate(props.id+'')
        // }else {
        //     setVisible(!visible)
        // }
        navigate(props.id+'')
    }

    return (
        <div style={{borderLeftColor:color}} className={'competitionItem'}>
            <Title level={3}>
                <span onClick={onClickTitle} className={'title'}>{props.title}</span>
                <div style={{float:"right"}}>
                    {statusTag}
                </div>
            </Title>
            <Space size={30}>
                <span><CalendarTwoTone /> 开始时间: {props.startTime}</span>
                <span><ClockCircleTwoTone /> 持续时间: {duration.toFixed(1)}小时</span>
                <span>{tag}</span>
            </Space>


        </div>
    );
};

export default CompetitionItem;
