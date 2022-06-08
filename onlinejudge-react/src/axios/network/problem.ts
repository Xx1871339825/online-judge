import request from "../request";
import stores from "../../stores/stores";

export function getProblem(pid:any){
    let {userStore} = stores
    let headers = {}
    if (userStore.accessToken!==null&&
        userStore.accessToken!==''){
        //添加请求头
        headers = {
            Authorization: userStore.accessToken
        }
    }
    return request.request({
        url:'api/problem/info/'+pid,
        method:'get',
        headers
    })
}

export function submitProblem(data:{pid:number,language:string,code:string}){
    return request.request({
        url:'api/problem/submit',
        method: 'post',
        headers: {
            Authorization: stores.userStore.accessToken
        },
        data
    })
}

export function getStatusList(params:{pid:number|null,nickname:string|null,status:number|null,size:10,current:number}){
    return request.request({
        url: 'api/problem/submit/list',
        method: 'get',
        params
    })
}

export function getCSubmitList(cid:any,params:{pid:number|null,nickname:string|null,status:number|null,size:10,current:number}){
    return request.request({
        url: 'api/problem/c-submit/list/' + cid,
        method: 'get',
        headers: {
            Authorization: stores.userStore.accessToken
        },
        params
    })
}

export function getStatusInfo(sid:any){
    return request.request({
        url: '/api/problem/submit/info/' + sid,
        method: 'get'
    })
}

export function getProblemList(params:{search:string|null,size:number,current:number}){
    let {userStore} = stores
    let headers = {}
    if (userStore.accessToken!==null&&
    userStore.accessToken!==''){
        //添加请求头
        headers = {
            Authorization: userStore.accessToken
        }
    }
    return request.request({
        url:'api/problem/list',
        method:'get',
        headers,
        params
    })
}
