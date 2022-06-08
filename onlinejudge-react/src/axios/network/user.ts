import request from "../request";
import stores from "../../stores/stores";
export function getUserInfo(pid:any) {
    return request.request({
        url: 'api/user/info/' + pid,
        method: 'get'
    })
}

export function upLoadAvatar(file:File){
    let headers = {
        'Content-Type': 'multipart/form-data',
        Authorization: stores.userStore.accessToken,
    }
    let data = new FormData();
    data.append("avatar",file)
    return request.request({
        url:'api/upload/avatar',
        method:'post',
        headers,
        data
    })
}

export function updateUser(data:{}){
    return request.request({
        url:'api/user/update',
        method:'put',
        headers: {
            Authorization: stores.userStore.accessToken
        },
        data
    })
}

export function getRankList(params:{search:string|null,current:number,size:number}){
    return request.request({
        url: 'api/user/rank-list',
        method: 'get',
        params
    })
}

export function updateEmail(data:{email:string,code:string}){
    return request.request({
        url:'api/user/update-email',
        method: 'put',
        headers: {
            Authorization: stores.userStore.accessToken
        },
        data
    })
}

export function updatePwd(data:{newPwd:string,emailCode:string}){
    return request.request({
        url: 'api/user/update-pwd',
        method: 'put',
        headers: {
            Authorization: stores.userStore.accessToken
        },
        data
    })
}

