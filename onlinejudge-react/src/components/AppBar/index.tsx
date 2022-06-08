import {FC, ReactElement} from 'react';

import MiniNavBar from "./MiniNavBar";
import NavBar from "./NavBar";
import {useWindowSize} from "../../utils/hooks";


interface IProps{
}

const pathArr = [
    {
        name: '首页',
        path: '/'
    },
    {
        name: '题库',
        path: '/problem-set'
    },
    {
        name: '竞赛',
        path: '/competition'
    },
    {
        name: '状态',
        path: '/status'
    },
    {
        name: '排名',
        path: '/rank'
    },
        // {
        //     name: '关于',
        //     path: '/about'
        // }
]

const AppBar:FC<IProps> = (props):ReactElement => {

    const {width} = useWindowSize()

    return (
        <>
            {width < 930? <MiniNavBar pathArr={pathArr}/>:<NavBar pathArr={pathArr}/>}
        </>
    );
};

export default AppBar;
