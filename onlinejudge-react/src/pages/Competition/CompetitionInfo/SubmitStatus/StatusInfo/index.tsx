import {FC, ReactElement, useEffect, useState} from 'react';
import './index.less'
import StatusInfoHeader from "../../../../../components/StatusInfoHeader";
import {useParams} from "react-router-dom";
import {getStatusInfo} from "../../../../../axios/network/problem";
import {statusArr} from "../../../../../assets/ts/const";

interface IProps{

}

const StatusInfo:FC<IProps> = (props):ReactElement => {
    let {sid} = useParams()
    const [statusInfo,setStatusInfo] = useState<any>({})
    const [judgeCaseList,setJudgeCaseList] = useState([])
    useEffect(()=>{
        getStatusInfo(sid)
            .then(res => {
                const {data} = res.data
                let caseList = data.judgeCaseList
                let caseArr:any = []
                for (let caseListElement of caseList) {
                    let status:any = statusArr.find(item => caseListElement.status === item.id)
                    caseArr.push({
                        ...caseListElement,
                        status: status.name,
                        color: status.bgColor
                    })
                }
                setJudgeCaseList(caseArr)
                setStatusInfo(data)
            })
    },[sid])


    return (
        <div className={'statusInfo'}>
            <StatusInfoHeader
                uid={statusInfo.userId}
                pid={statusInfo.problemId}
                statusNo={statusInfo.status}
                time={statusInfo.time}
                memory={statusInfo.memory}
                language={statusInfo.language}
                nickname={statusInfo.nickname}
                message={statusInfo.errorMessage}
                score={statusInfo.score}
            />
            {
                judgeCaseList.length === 0 ? null:(
                    <div className={'caseBox'}>
                        <div className={'title'}>
                            测试点详情
                        </div>
                        <div className={'caseItemBox'}>
                            {judgeCaseList.map((item:any,index:number) => (
                                <div key={index} className={'caseItem'} style={{borderColor:item.color,color:item.color}}>
                                    <div className={'no'}>
                                        Test #{index}
                                    </div>
                                    <div className={'statusStr'}>
                                        {item.status}
                                    </div>
                                    <div className={'timeAndMemory'}>
                                        {item.time}ms/{item.memory}kb
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                )
            }
        </div>

    );
};

export default StatusInfo;
