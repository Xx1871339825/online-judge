import request from "../request";
import stores from "../../stores/stores";
export function signIn(data:{identifier:string,password:string}){
    return request.request({
        url: 'admin/auth/sign-in',
        method: 'post',
        data
    })
}

export function getMenuList(){
    return request.request({
        url: 'admin/auth/menu-list',
        method: 'get',
        headers : {
            Authorization: stores.getters.getAccessToken
        }
    })
}

export function uploadFile(file:any){
    let headers = {
        'Content-Type': 'multipart/form-data',
        Authorization: stores.getters.getAccessToken,
    }
    let data = new FormData();
    data.append("file",file)
    return request.request({
        url:'api/upload',
        method:'post',
        headers,
        data
    })
}

export function getUserList(params:any){
    return request.request({
        url: 'admin/user/list',
        method: 'get',
        headers : {
            Authorization: stores.getters.getAccessToken
        },
        params
    })
}

export function banUser(data:any){
    let list:any = []
    data.forEach((i:any) => {
        list.push(i.uid)
    })

    return request.request({
        url:'admin/user/ban-user',
        method: "put",
        headers : {
            Authorization: stores.getters.getAccessToken
        },
        data:{
            uid: list
        }
    })
}
