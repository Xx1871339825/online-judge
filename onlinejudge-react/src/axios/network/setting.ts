import request from "../request";
import stores from "../../stores/stores";

export function getBannerList(){
    return request.request({
        url:'api/setting/banner',
        method:'get'
    })
}

export function getArticleList(params:{search:string,current:number,size:number}){
    return request.request({
        url: 'api/setting/article',
        method: 'get',
        params
    })
}

export function testAuth(){
    return request.request({
        url:'api/setting/test',
        method:'get',
        headers:{
            Authorization:stores.userStore.accessToken
        }
    })
}
