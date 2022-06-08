import request from "../request";
import stores from "../../stores/stores";
let headers =  {
    Authorization: stores.getters.getAccessToken
}
export function getRoleListByUid(uid:number){
    return request.request({
        url: 'admin/role/list/' + uid,
        method: 'get',
        headers
    })
}

export function getAllRoleList(){
    return request.request({
        url:'admin/role/list',
        method: 'get',
        headers
    })
}

export function updateUserRole(data:any){
    return request.request({
        url: 'admin/role/update-user-role',
        method: 'post',
        headers,
        data
    })
}

export function updateRoleMenu(data:any){
    return request.request({
        url: 'admin/role/update-role-menu',
        method: 'post',
        headers,
        data
    })
}
export function saveOrUpdateRole(data:any){
    return request.request({
        url: 'admin/role/save-or-update-role',
        method: 'post',
        headers,
        data
    })
}

export function deleteRole(rid:number){
    return request.request({
        url: 'admin/role/delete/' + rid,
        method: 'delete',
        headers
    })
}

