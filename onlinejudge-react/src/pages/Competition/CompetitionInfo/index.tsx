import {FC, ReactElement, useEffect, useState} from 'react';
import './index.less'
import {Button, Space, Tag, Menu, Modal, Input, message} from 'antd';
import RouterBeforeEach from "../../../route/RouterBeforeEach";
import {useNavigate, useParams} from "react-router-dom";
import {getCompetitionInfoDTO, register, } from "../../../axios/network/competition";
import {getTimeLeft2ByMss} from "../../../utils/utlis";
const {Search} = Input


interface IProps{

}

const CompetitionInfo:FC<IProps> = (props):ReactElement => {
    const navigate = useNavigate()
    const {id} = useParams()
    const [visible,setVisible] = useState(false)
    const [countDown,setCountDown] = useState(0)
    const clickMenu = (type:number) => {
        return () => {
            if (type === 0){
                navigate('')
            }else if (type === 1){
                navigate('problem-set')
            }else if (type === 2){
                navigate('submit')
            }else {
                navigate('rank')
            }
        }
    }
    const onClickRegister = () => {
        if (data.auth !== 0){
            //需要密码
            setVisible(!visible)
        }else {
            register({cid:data.id,pwd:null}).then(res => {
                message.success('报名成功')
                getCompetitionInfoDTO(id).then(res => {
                    const {data} = res.data
                    setData(data)
                })
            })
        }
    }
    const onClickSubmit = (value:string) => {
        register({cid:data.id,pwd:value}).then(res => {
            message.success('报名成功')
            getCompetitionInfoDTO(id).then(res => {
                const {data} = res.data
                setData(data)
            })
        })
    }

    //判断是否报名，不报名不能查看题目
    const [data,setData] = useState<any>({})
    useEffect(() => {
        getCompetitionInfoDTO(id).then(res => {
            const {data} = res.data
            setData(data)
            const startTime = new Date(data.startTime).getTime()
            const endTime = new Date(data.endTime).getTime()
            const current = new Date().getTime()
            if (current >= startTime && current <= endTime){
                setCountDown(endTime - current)
            }
        })
    },[id])

    const onClose = () => {
        setVisible(!visible)
    }

    const statusObj = () => {
        const startTime = new Date(data.startTime).getTime()
        const endTime = new Date(data.endTime).getTime()
        const current = new Date().getTime()
        if(startTime > current){
            return {
                id: 0,
                color:'#FF9900',
                title: '未开始'
            }
        }if (current > startTime && current < endTime){
            return {
                id: 1,
                color: '#19BE6B',
                title: '进行中'
            }
        }else {
            return {
                id: 2,
                color: '#ED3F14',
                title: '已结束'
            }
        }
    }

    useEffect(() => {
        let timer = setInterval(() => {
            if (countDown > 0){
                setCountDown(countDown-1000)
            }
        },1000);
        return () => {
            clearInterval(timer)
        }
    },[countDown])

    const getCountDown = () => {
        return getTimeLeft2ByMss(countDown)
    }

    const getMenu = () => {
        const startTime = new Date(data.startTime).getTime()
        const endTime = new Date(data.endTime).getTime()
        const current = new Date().getTime()
        if (data.isRegister !==-1 && startTime <= current && endTime >= current){
            //报名且开始
            return ( <><Menu.Item onClick={clickMenu(1)} key="problem-set">
                题目列表
            </Menu.Item><Menu.Item onClick={clickMenu(2)} key="submitStatus">
                提交状态
            </Menu.Item><Menu.Item onClick={clickMenu(3)} key="rank">
                排行
            </Menu.Item></>)
        }else if ( endTime <= current){
            //已结束
            return ( <><Menu.Item onClick={clickMenu(1)} key="problem-set">
                题目列表
            </Menu.Item><Menu.Item onClick={clickMenu(2)} key="submitStatus">
                提交状态
            </Menu.Item><Menu.Item onClick={clickMenu(3)} key="rank">
                排行
            </Menu.Item></>)
        }else {
            return null
        }
    }



    return (
        <div className={'competitionInfo'}>
            <div className={'titleBox'}>
                <div className={'item'}>
                    <span style={{fontWeight:'bolder',fontSize:'1.5em'}}>{data.title}</span>
                </div>
                <div className={'item'}>
                    <Tag color={statusObj().color}>{statusObj().title}</Tag>
                </div>
            </div>

            <div className={'infoBox'}>
                <Space size={20}>
                    <span>参与人数: {data.registerCount}</span>
                    <span>比赛时间：{data.startTime} 至 {data.endTime}</span>
                    {data.isRegister === -1? (<><span>{statusObj().id !== 2? <Button onClick={onClickRegister} type="primary">报名</Button>:null}</span></>):null}
                    {
                        statusObj().id === 1 && data.isRegister !== -1?
                            (<div className={'item'}>
                                倒计时:{getCountDown()}
                            </div>):null
                    }
                </Space>
            </div>
            <div className={'menuBox'}>
                <Menu mode="horizontal">
                    <Menu.Item onClick={clickMenu(0)} defaultChecked key="description">
                        详情
                    </Menu.Item>
                    {getMenu()}
                </Menu>
            </div>
            <RouterBeforeEach />
            <Modal footer={null} onCancel={onClose} title={"密码"} visible={visible}>
                <Search
                    onSearch={onClickSubmit}
                    placeholder="私有赛需要填写密码"
                    allowClear
                    enterButton="提交"
                    size="large"
                />
            </Modal>
        </div>
    );
};

export default CompetitionInfo;
