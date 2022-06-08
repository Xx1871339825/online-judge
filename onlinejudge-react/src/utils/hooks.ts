import {useEffect, useState} from "react";
import {createSearchParams, generatePath, useLocation, useNavigate} from "react-router-dom";

//获取屏幕宽高
const useWindowSize = () => {
    const [windowSize,setWindowSize] = useState({
        width:window.innerWidth,
        height:window.innerHeight
    })
    useEffect(()=>{
        const updateSize = () => setWindowSize({
            width:window.innerWidth,
            height:window.innerHeight
        })
        window.addEventListener('resize',updateSize)
        return ()=> window.removeEventListener('resize',updateSize)
    },[])

    return windowSize
}

//获取当前的PathObj
const useCurrentPathObj = (pathArr:Array<{name:string,path:string}>)=> {
    let location = useLocation();
    let pathname = location.pathname;
    /**
     * 用途：遇到null或者undefined时可以立即停止表达式的运行。
     看个例子：
     let a = { b: 1};
     const val = a?.b;
     编译之后生成的JavaScript代码：
     var a = { b: 1 };
     var val = a === null || a === void 0 ? void 0 : a.b;
     */
    return pathArr.find(value => value.path === pathname)
}
const useNavigateParams = () => {
    const navigate = useNavigate();

    return (url: string, params:any) => {
        for (let key of Object.keys(params)) {
            if (params[key] == null || params[key] === '' ){
                delete params[key]
            }
        }
        const path = generatePath(":url?:queryString", {
            url,
            queryString: createSearchParams(params).toString()
        });
        navigate(path);
    };
};
export {useWindowSize,useCurrentPathObj,useNavigateParams}
