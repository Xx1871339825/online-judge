import request from "../request";
import stores from "../../stores/stores";
import {settingType, statusArr} from "../../assets/ts/const";
import 'element-plus/es/components/message/style/css'
export function getSettingList(params:any) {
    let {type} = params
    let typeObj:any = settingType.find(item => item.name === type)
    type = typeObj == null ? -1:typeObj.id
    delete params.type
    return request.request({
        url: 'admin/setting/list',
        method: 'get',
        headers : {
            Authorization: stores.getters.getAccessToken
        },
        params: {
            ...params,
            type
        }
    })
}

export function addSetting(data:any){
    let setting = data.settingType
    let status = data.status
    let settingObj:any = settingType.find(item => item.name === setting)
    let statusObj:any = statusArr.find(item => item.name === status)
    return request.request({
        url: 'admin/setting/add',
        method: 'post',
        headers : {
            Authorization: stores.getters.getAccessToken
        },
        data: {
            ...data,
            settingType: settingObj == null ? 1:settingObj.id,
            status: statusObj == null ? 0:statusObj.id
        }
    })
}

export function updateSetting(data:any){
    let setting = data.settingType
    let status = data.status
    let settingObj:any = settingType.find(item => item.name === setting)
    let statusObj:any = statusArr.find(item => item.name === status)
    return request.request({
        url: 'admin/setting/update',
        method: 'post',
        headers : {
            Authorization: stores.getters.getAccessToken
        },
        data: {
            ...data,
            settingType: settingObj == null ? 1:settingObj.id,
            status: statusObj == null ? 0:statusObj.id
        }
    })
}

export function getSetting(sid:number){
    return request.request({
        url: 'admin/setting/' + sid,
        method: 'get',
        headers : {
            Authorization: stores.getters.getAccessToken
        }
    })
}
export function deleteSetting(sid:number){
    return request.request({
        url: 'admin/setting/delete/' + sid,
        method: 'delete',
        headers : {
            Authorization: stores.getters.getAccessToken
        }
    })
}

