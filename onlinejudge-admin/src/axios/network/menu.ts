import request from "../request";
import stores from "../../stores/stores";
let headers =  {
    Authorization: stores.getters.getAccessToken
}
export function getMenuByRid(rid:any){
    return request.request({
        url: 'admin/menu/rid/' + rid,
        method: 'get',
        headers
    })
}
