import stores from "../../stores/stores";
import request from "../request";
export function getTagList(params:any) {
    return request.request({
        url: 'admin/tag/list',
        method: "get",
        headers : {
            Authorization: stores.getters.getAccessToken
        },
        params
    })
}

export function addTag(data:any){
    console.log('tagData:',data)
    return request.request({
        url: 'admin/tag/add',
        method: 'post',
        headers : {
            Authorization: stores.getters.getAccessToken
        },
        data
    })
}

export function updateTag(data:any){
    return request.request({
        url: 'admin/tag/update',
        method: 'put',
        headers : {
            Authorization: stores.getters.getAccessToken
        },
        data
    })
}

export function deleteTag(tid: number){
    return request.request({
        url: 'admin/tag/delete/' + tid,
        method: 'delete',
        headers : {
            Authorization: stores.getters.getAccessToken
        },
    })
}

export function getTag(tid:number){
    return request.request({
        url:'admin/tag/' + tid,
        method: 'get',
        headers : {
            Authorization: stores.getters.getAccessToken
        },
    })
}
