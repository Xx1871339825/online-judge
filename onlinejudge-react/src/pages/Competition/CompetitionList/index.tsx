import {FC, ReactElement, useEffect, useRef, useState} from 'react';
import './index.less'
import {Checkbox, Input, Pagination, Select, Typography} from "antd";
import {competitionStatusList} from "../../../assets/ts/const";
import CompetitionItem from "../../../components/CompetitionItem";
import {useLocation, useSearchParams} from "react-router-dom";
import {useNavigateParams} from "../../../utils/hooks";
import {getCompetitionList} from "../../../axios/network/competition";
import stores from "../../../stores/stores";
const {Title} = Typography
const { Option } = Select
const {Search} = Input

interface IProps{

}

const CompetitionList:FC<IProps> = (props):ReactElement => {
    let user:any = stores.userStore.user == null||stores.userStore.user===''? {}:JSON.parse(stores.userStore.user)
    let [searchParam] = useSearchParams()
    let navigateParams = useNavigateParams()
    let pathname = useLocation().pathname
    let searchRef:any = useRef()
    const [data,setData] = useState(new Array<any>())
    let [search,setSearch] = useState(searchParam.get("search"))
    let [current,setCurrent] = useState(Number(searchParam.get("current")))
    let [total,setTotal] = useState(1)
    let [status,setStatus] = useState(searchParam.get("status")==null?-1:Number(searchParam.get("status")))

    let [uid,setUid] = useState(-1)

    const inputValue:any = searchParam.get("search") == null? '':searchParam.get("search")


    const onChange = (currentPage:number) => {
        setCurrent(currentPage)
        navigateParams(pathname,{
            current: currentPage,
            search,
            status,
            uid
        })
    }


    const onStatusChange = (value:number) => {
        setStatus(value)
        navigateParams(pathname,{
            current,
            status:value,
            search,
            uid
        })
    }
    const onSearch = (value:string) => {
        setSearch(value)
        navigateParams(pathname,{
            current,
            status,
            search:value,
            uid
        })
    }

    useEffect(() => {
        getCompetitionList({
            search,
            size: 10,
            current,
            status,
            uid
        }).then(res => {
            const {data} = res.data
            setData(data.records)
            setTotal(data.total)
        })
    },[current, search, status, uid])
    const onCheck = (e:any) => {
        if (e.target.checked){
            setUid(user.uid)
            navigateParams(pathname,{
                current,
                status,
                search,
                uid:user.uid
            })
        }else {
            setUid(-1)
            navigateParams(pathname,{
                current,
                status,
                search,
                uid:-1
            })
        }
    }
    document.title = 'CrowOj - 竞赛'
    return (
        <div className={'competitionList'}>
            <div className={'topBarBox'}>
                <div className={'topBar'}>
                    <div className={'tbText'} style={{width:'150px'}}>
                        <Title level={2} style={{lineHeight:'50px'}}>全部比赛</Title>
                    </div>
                    <div className={'tbText'}>
                        <Search defaultValue={inputValue} style={{marginTop:9}} onSearch={onSearch}  ref={searchRef} size={"middle"} placeholder={'输入关键词'} />
                    </div>
                    <div className={'tbText'} >
                        <Select onChange={onStatusChange} defaultValue={searchParam.get('status') == null? -1:Number(searchParam.get('status'))} style={{width:'100px'}} size={"middle"}  >
                            {
                                competitionStatusList.map(item => (
                                    <Option key={item.id} value={item.id}>{item.status}</Option>
                                ))
                            }
                        </Select>
                    </div>
                    {
                        stores.userStore.accessToken !== '' ? (
                            <div className={'tbText'}>
                                <Checkbox onChange={onCheck} >我报名的</Checkbox>
                            </div>
                        ): null
                    }
                </div>
            </div>
            {
                data.map(item => {
                    return (<CompetitionItem
                        id={item.id}
                        key={item.id}
                        title={item.title}
                        startTime={item.startTime}
                        endTime={item.endTime}
                        auth={item.auth}
                    />)
                })
            }
            <div className={"pWrap"}>
                <div className={"pagination"}>
                    <Pagination current={current} total={total} showSizeChanger={false}
                                showQuickJumper onChange={onChange}
                    />
                </div>
            </div>
        </div>
    );
};

export default CompetitionList;
