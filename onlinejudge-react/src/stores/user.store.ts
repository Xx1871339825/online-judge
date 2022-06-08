import {makeAutoObservable} from "mobx";
export default class UserStore{
    private _user:string = localStorage.getItem('user') || ''
    private _accessToken:string = localStorage.getItem('accessToken') || ''
    private _refreshToken:string = localStorage.getItem('refreshToken') || ''
    constructor() {
        makeAutoObservable(this)
    }

    getUserObj():any{
        return this._user === '' ? {}:JSON.parse(this._user)
    }

    get user(): string {
        return this._user;
    }

    set user(value: string) {
        if (value === '') {
            localStorage.removeItem('user')
        }else {
            localStorage.setItem('user',value)
        }

        this._user = value;
    }

    get refreshToken(): string {
        return this._refreshToken;
    }

    set refreshToken(value: string) {
        if (value === ''){
            localStorage.removeItem('refreshToken')
        }else {
            localStorage.setItem('refreshToken',value)
        }

        this._refreshToken = value;
    }

    get accessToken(): string {
        return this._accessToken;
    }

    set accessToken(value: string) {
        if (value === ''){
            localStorage.removeItem('accessToken')
        }else {
            localStorage.setItem('accessToken',value)
        }
        this._accessToken = value;
    }
}
