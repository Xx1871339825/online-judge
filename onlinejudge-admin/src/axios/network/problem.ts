import request from "../request";
import stores from "../../stores/stores";
export function getProblemList(params:any){
    return request.request({
        url:'admin/problem/list',
        method: 'get',
        headers : {
            Authorization: stores.getters.getAccessToken
        },
        params
    })
}

export function getAllProblemList(){
    return request.request({
        url: 'admin/problem/get-all-problem-list',
        method: 'get',
        headers : {
            Authorization: stores.getters.getAccessToken
        }
    })
}

export function saveOrUpdateProblem(data:any){
    return request.request({
        url: 'admin/problem',
        method: 'post',
        headers : {
            Authorization: stores.getters.getAccessToken
        },
        data
    })
}

export function getProblemInfo(pid:number){
    return request.request({
        url: 'admin/problem/' + pid,
        method: 'get',
        headers : {
            Authorization: stores.getters.getAccessToken
        }
    })
}

export function getSearchProblemList(params:{search:string|null}){
    return request.request({
        url: 'admin/problem/get-search-problem-list',
        method: 'get',
        headers : {
            Authorization: stores.getters.getAccessToken
        },
        params
    })
}

export function getStatusInfo(sid:any){
    return request.request({
        url: '/admin/problem/submit/info/' + sid,
        method: 'get',
        headers : {
            Authorization: stores.getters.getAccessToken
        }
    })
}
