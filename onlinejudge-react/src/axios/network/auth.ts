import request from "../request";
import stores from "../../stores/stores";
export function signIn(param: { identifier:string, password:string }){
    return request.request({
        url: 'api/auth/sign-in',
        method: 'post',
        data: param
    })
}

export function sendSignUpEmail(param:{email:string}){
    return request.request({
        url: 'api/auth/sign-up/email',
        method: 'post',
        data: param
    })
}

export function signUp(param:{username:string,password:string,email:string,emailValidCode:string}){
    return request.request({
        url: 'api/auth/sign-up',
        method: 'post',
        data: param
    })
}

export function sendUpdateEmail(email:string){
    return request.request({
        url: 'api/auth/update/email/' + email,
        method: 'post'
    })
}

export function sendUpdatePwdEmail(){
    return request.request({
        url: 'api/user/send-update-pwd-email',
        method: 'post',
        headers: {
            Authorization: stores.userStore.accessToken
        }
    })
}

export function submitForgetPwd(data:any){
    return request.request({
        url: 'api/auth/forget',
        method: 'post',
        data
    })
}


