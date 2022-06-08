import request from "../request";
import stores from "../../stores/stores";
const {userStore} = stores
export function getCompetitionList(params:any){
    return request.request({
        url: 'api/competition/list',
        method: 'get',
        params
    })
}
export function validCompetition(cid:number,pwd:string){
    return request.request({
        url: 'api/competition/valid/'+cid + '/' + pwd,
        method: 'get',
        headers: {
            Authorization: userStore.accessToken
        }
    })
}
export function getCompetitionDesc(cid:any){
    return request.request({
        url: 'api/competition/desc/' + cid,
        method: 'get',
        headers: {
            Authorization: userStore.accessToken
        }
    })
}
export function getProblemListByCid(cid:any){
    return request.request({
        url: 'api/competition/problem/' + cid,
        method: 'get',
        headers: {
            Authorization: userStore.accessToken
        }
    })
}

export function getCompetitionInfoDTO(cid:any){
    return request.request({
        url: 'api/competition/' + cid,
        method: 'get',
        headers: {
            Authorization: userStore.accessToken
        }
    })
}

export function getCompetitionProblemInfo(cid:any,pid:any){
    return request.request({
        url: 'api/competition/problem/info/'+cid + '/' + pid,
        method: 'get',
        headers: {
            Authorization: userStore.accessToken
        }
    })
}

export function register(data:{cid:number|string,pwd:string|null}){
    return request.request({
        url: 'api/competition/register',
        method: 'post',
        headers: {
            Authorization: userStore.accessToken
        },
        data
    })
}

export function cSubmit(data:{cid:number,pid:number,language:string,code:string}){
    return request.request({
        url:'api/problem/c-submit',
        method: 'post',
        headers: {
            Authorization: stores.userStore.accessToken
        },
        data
    })
}

export function getCRankList(cid:number|string|undefined){
    return request.request({
        url: 'api/competition/rank/' + cid,
        method: 'get',
        headers: {
            Authorization: stores.userStore.accessToken
        }
    })
}


