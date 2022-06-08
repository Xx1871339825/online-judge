import {FC, ReactElement, useEffect, useState} from 'react';
import {Avatar, Typography} from 'antd';
import 'font-awesome/less/font-awesome.less';
import 'react-fontawesome';
import './index.less'
import ReactECharts from 'echarts-for-react';
import 'echarts/lib/chart/pie';
import {EditFilled, UserOutlined} from "@ant-design/icons";
import {Link, useParams} from "react-router-dom";
import {getUserInfo} from "../../axios/network/user";
import {genderList} from "../../assets/ts/const";
const {Title} = Typography

interface IProps{

}

const UserHome:FC<IProps> = (props):ReactElement => {

// private Integer waCount;
// private Integer naAcCount;
// private Integer reCount;
// private Integer ceCount;
// private Integer mleCount;
// private Integer tleCount;

    const [userInfo,setUserInfo] = useState<any>({})
    const [pidList,setPidList] = useState<any>([])
    const {id} = useParams();

    useEffect(()=>{
        getUserInfo(id)
            .then(res => {
                console.log(res.data.data)
                let gender:any = genderList.find(item => item.id === res.data.data.gender)
                if (gender === null){
                    res.data.data.gender = '隐藏'
                }else {
                    res.data.data.gender = gender.gender
                }
                setUserInfo(res.data.data)
                setPidList(res.data.data.pidList)
            })
    },[id])

    const opt = () => {
        return {
            title: {
                text: '用户提交情况',
                left: 'center'
            },
            tooltip: {
                trigger: 'item'
            },
            series: [
                {
                    name: '状态',
                    type: 'pie',
                    radius: '50%',
                    data: [
                        {
                            value: userInfo.acceptCount,
                            name: '通过',
                            itemStyle: {
                                color:'#91CC75'
                            }
                        },
                        {
                            value: userInfo.waCount,
                            name: '错误的答案',
                            itemStyle: {
                                color:'#EE6666'
                            }
                        },
                        {
                            value: userInfo.naAcCount,
                            name: '未通过所有测试用例',
                            itemStyle: {
                                color:'#FAC858'
                            }
                        },
                        {
                            value: userInfo.reCount,
                            name: '运行时错误',
                            itemStyle: {
                                color:'#5470C6'
                            }
                        },
                        {
                            value: userInfo.ceCount,
                            name: '编译错误',
                            itemStyle: {
                                color: '#909399'
                            }
                        },
                        {
                            value: userInfo.tleCount,
                            name: '超出时间限制',
                            itemStyle: {
                                color: '#CD5741'
                            }
                        },
                        {
                            value: userInfo.mleCount,
                            name: '超出内存限制',
                            itemStyle: {
                                color: '#7E5F54'
                            }
                        },
                    ],
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        }
    }

    return (
        <div className={'userHome'}>
            <div className={'userInfo'}>
                <div className={'titleBox'}>
                    用户信息
                </div>
                <div className={'avatarBox'}>
                    <div className={'avatarImage'}>
                        <Avatar src={userInfo.avatar} size={120} icon={<UserOutlined />} />
                    </div>
                    <Title level={2}>{userInfo.nickname}</Title>

                </div>

                <div className={'infoBox'}>
                    <div className={'submitStatus'}>
                        <ReactECharts option={opt()} />
                    </div>
                    <div className={'itemBox'}>
                        <div className={'infoItem'}>
                            <div className={'left'}>
                                <EditFilled /> 个性签名:
                            </div>
                            <div className={'right'}>
                                {userInfo.slogan}
                            </div>
                        </div>
                        <div className={'infoItem'}>
                            <div className={'left'}>
                                <i style={{fontWeight:'bold'}} className={'fa fa-transgender-alt'}/> 性别:
                            </div>
                            <div className={'right'}>
                                {userInfo.gender}
                            </div>
                        </div>
                        <div className={'infoItem'}>
                            <div className={'left'}>
                                <i style={{fontWeight:'bold'}} className={'fa fa-paper-plane'}/> 提交:
                            </div>
                            <div className={'right'}>
                                {userInfo.submitCount}
                            </div>
                        </div>
                        <div className={'infoItem'}>
                            <div className={'left'}>
                                <i style={{fontWeight:'bold'}} className={'fa fa-check'}/> 通过:
                            </div>
                            <div className={'right'}>
                                {userInfo.acceptCount}
                            </div>
                        </div>
                    </div>
                </div>


            </div>

            <div className={'acBox'}>
                <div className={'titleBox'}>
                    已解决的问题列表
                </div>
                <div className={'tagBox'}>
                    {pidList.map((item: any) => {
                        return (
                            <Link key={item} to={'/problem-set/' + item}>
                                <div className={'tagItem'}>
                                    {item}
                                </div>
                            </Link>
                        )
                    })}
                </div>
            </div>
        </div>
    );
};

export default UserHome;
