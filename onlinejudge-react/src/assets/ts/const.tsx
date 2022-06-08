/*
*   Accepted(0,"Accepted"),// 通过
    WrongAnswer(1,"WrongAnswer"),// 答案错误
    NotAllAccepted(2,"NotAllAccepted"),//部分答案错误
    RuntimeError(3,"RuntimeError"),// 程序异常终止
    TimeLimitExceeded(4,"TimeLimitExceeded"),// 时间超出限制
    MemoryLimitExceeded(5,"MemoryLimitExceeded"),// 内存超出限制
    Pending(6,"Pending"),// 排队等待中
    Compiling(7,"Compiling"),// 编译中
    Judging(8,"Judging"), // 正在运行程序
    CompileError(9,"CompileError"); // 编译出错*/
import {Tag} from "antd";

export const statusArr = [
    {
        id:0,
        name: 'Accepted',
        bgColor: '#19BE6B',
        tag: <Tag color={'#19BE6B'}>Accepted</Tag>
    },
    {
        id: 1,
        name: 'WrongAnswer',
        bgColor: '#ED3F14',
        tag: <Tag color={'#ED3F14'}>WrongAnswer</Tag>
    },
    {
        id: 2,
        name: 'NotAllAccepted',
        bgColor: '#FF9900',
        tag: <Tag color={'#FF9900'}>NotAllAccepted</Tag>
    },
    {
        id: 3,
        name: 'RuntimeError',
        bgColor: '#ED3F14',
        tag: <Tag color={'#ED3F14'}>RuntimeError</Tag>
    },
    {
        id: 4,
        name: 'TimeLimitExceeded',
        bgColor: '#ED3F14',
        tag: <Tag color={'#ED3F14'}>TimeLimitExceeded</Tag>
    },
    {
        id: 5,
        name: 'MemoryLimitExceeded',
        bgColor: '#ED3F14',
        tag: <Tag color={'#ED3F14'}>MemoryLimitExceeded</Tag>
    },
    {
        id: 6,
        name: 'Pending',
        bgColor: '#FF9900',
        tag: <Tag color={'#FF9900'}>Pending</Tag>
    },
    {
        id: 7,
        name: 'Compiling',
        bgColor: '#19BE6B',
        tag: <Tag color={'#19BE6B'}>Compiling</Tag>
    },
    {
        id: 8,
        name: 'Judging',
        bgColor: '#2D8CF0',
        tag: <Tag color={'#2D8CF0'}>Judging</Tag>
    },
    {
        id: 9,
        name: 'CompileError',
        bgColor: '#FF9900',
        tag: <Tag color={'#FF9900'}>CompileError</Tag>
    }
]

//    CPP(0,"cpp"),
//     JAVA(1,"java")
export const languageList = [
    {
       id: 0,
       name: 'cpp'
    },
    {
        id: 1,
        name: 'java'
    }
]
// MALE(0,"gender.male"),FEMALE(1,"gender.female"),HIDE(2,"gender.hide");
export const genderList = [
    {
        id:0,
        gender: '男'
    },
    {
        id:1,
        gender: '女'
    },
    {
        id:2,
        gender: '隐藏'
    }
]

export const competitionStatusList = [
    {
        id: -1,
        status: '全部'
    },
    {
        id: 0,
        status: '筹备中'
    },
    {
        id: 1,
        status: '进行中'
    },
    {
        id: 2,
        status: '已结束'
    }
]
