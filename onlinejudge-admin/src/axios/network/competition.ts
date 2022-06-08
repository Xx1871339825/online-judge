import stores from "../../stores/stores";
import request from "../request";

let headers =  {
    Authorization: stores.getters.getAccessToken
}

export function addCompetition(data:any) {
    let obj = {
        ...data,
        startTime: data.time[0],
        endTime: data.time[1]
    }
    return request.request({
        url: 'admin/competition/add',
        method: 'post',
        data: obj,
        headers
    })
}

export function getCompetitionPage(params:any){
    return request.request({
        url: 'admin/competition/search',
        method: 'get',
        headers,
        params
    })
}

export function deleteCompetition(cid:any){
    return request.request({
        url: 'admin/competition/delete/' + cid,
        method: 'delete',
        headers
    })
}

export function getProblemListByCid(cid:any){
    return request.request({
        url: 'admin/competition/get-problem-list-by-cid/' + cid,
        method: 'get',
        headers
    })
}

export function updateCompetition(data:any){
    let obj = {
        ...data,
        startTime: data.time[0],
        endTime: data.time[1]
    }
    return request.request({
        url: 'admin/competition/update',
        method: 'post',
        data: obj,
        headers
    })
}

export function getCompetitionInfoDTO(cid:any){
    return request.request({
        url: 'api/competition/' + cid,
        method: 'get',
        headers
    })
}

export function getCompetitionDesc(cid:any){
    return request.request({
        url: 'api/competition/desc/' + cid,
        method: 'get',
        headers
    })
}

export function getProblemInfoListByCid(cid:any){
    return request.request({
        url: 'admin/competition/get-pdto-list-by-cid/' + cid,
        method: 'get',
        headers
    })
}

export function getCSubmitList(cid:any,params:any){
    return request.request({
        url: 'api/problem/c-submit/list/' + cid,
        method: 'get',
        headers,
        params
    })
}

export function getCRankList(cid:number|string|undefined){
    return request.request({
        url: 'api/competition/rank/' + cid,
        method: 'get',
        headers
    })
}
