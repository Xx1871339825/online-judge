import {FC, ReactElement, useEffect} from 'react';
import {testAuth} from "../../axios/network/setting";

interface IProps{

}

const Activity:FC<IProps> = (props):ReactElement => {
    document.title = 'CrowOj - 活动'
    useEffect(()=>{
        testAuth().then(res=>{
            console.log(res)
        }).catch((err)=>{
            console.log(err,'报错了')
        })
    },[])
    return (
        < >
            这是活动页
        </>
    );
};

export default Activity;
