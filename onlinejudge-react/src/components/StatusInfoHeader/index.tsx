import {FC, ReactElement, useEffect, useState} from 'react';
import './index.less'
import {Typography,Space} from "antd";
import {languageList, statusArr} from "../../assets/ts/const";
import {CheckCircleOutlined, CloseCircleOutlined, ExclamationCircleOutlined, SyncOutlined} from "@ant-design/icons";
import {Link} from "react-router-dom";
const {Title} = Typography

interface IProps{
    statusNo?:number
    time?:number,
    memory?:number,
    language?:number,
    nickname?:string,
    message?:string,
    score?:number,
    pid?: number,
    uid?: number
}

const StatusInfoHeader:FC<IProps> = (props):ReactElement => {
    let status:any = statusArr.find(item => item.id === props.statusNo) === undefined ? {}:statusArr.find(item => item.id === props.statusNo)
    let statusNo = status.id
    let icon = <CheckCircleOutlined />

    if (statusNo === 1||statusNo === 3||statusNo === 4||statusNo === 5||statusNo === 9) {
        icon = <CloseCircleOutlined />
    }else if (statusNo === 2){
        icon = <ExclamationCircleOutlined />
    }else if (statusNo === 6||statusNo === 7||statusNo === 8){
        icon = <SyncOutlined spin />
    }
    let language:any = languageList.find(item => item.id === props.language) === undefined ? {}:languageList.find(item => item.id === props.language)
    let languageName = language.name
    return (
        <div style={{backgroundColor:status.bgColor}} className={'statusInfoHeader'}>
            <div className={'iconBox'}>
                {icon}
            </div>
            <div className={'statusBox'}>
                <Title style={{color:'white'}} level={2}>{status.name}</Title>
                <div className={'info'}>
                    <Space size={30}>
                        <span>题目id: <Link to={'/problem-set/' + props.pid}>{props.pid}</Link></span>
                        <span>运行时间: {props.time}ms</span>
                        <span>运行内存: {props.memory}kb</span>
                        <span>语言: {languageName}</span>
                        <span>用户: <Link to={'/user/' + props.uid}>{props.nickname}</Link></span>
                        <span>分数: {props.score}</span>
                    </Space>
                </div>
            </div>
            <div style={{color:'white',padding:'10px'}}>
                {props.message}
            </div>
        </div>
    );
};

export default StatusInfoHeader;
