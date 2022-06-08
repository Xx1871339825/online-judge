import {FC, ReactElement} from 'react';
import {statusArr} from "../../assets/ts/const";

interface IProps{
    statusNo:number
}



const StatusTag:FC<IProps> = (props):ReactElement => {
    const {statusNo} = props
    let status = statusArr.find(item => item.id === statusNo)
    if (status === undefined){
        status = statusArr[3]
    }
    return (
        <>
            {status.tag}
        </>
    );
};

export default StatusTag;
