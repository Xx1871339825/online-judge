import {FC, useEffect, useState} from 'react';
import {Outlet, useLocation, useNavigate} from "react-router-dom";
import {useStore} from "../../stores/store.context.provider";
import {message} from "antd";
import {authRouter} from "../router";

interface IProps{

}

const RouterBeforeEach:FC<IProps> = (props) => {
    const navigate = useNavigate()
    const location = useLocation()
    const [auth,setAuth] = useState(false)
    let {userStore,modalStore} = useStore();
    let jwt = userStore.accessToken
    let isLogin = jwt!=null && jwt!==''
    let pathname = location.pathname
    let pathArr = pathname.split('/')
    let last = Number(pathArr[pathArr.length-1])
    if (!isNaN(last)){
        pathname = pathname.replace(last.toString(),":id")
    }
    useEffect(()=>{
        let valid = false
        for (let string of authRouter) {
            if (string === pathname){
                valid = true
                break
            }
        }
        if (valid && !isLogin){
            setAuth(false)
            //message.error("对不起你还未登录！")
            modalStore.visible = true
            navigate('/',{replace:true})
        }else {
            setAuth(true)
        }
    }, [isLogin, modalStore, navigate, pathname])
    return auth?<Outlet />:null
};

export default RouterBeforeEach;
