import axios, {AxiosInstance, AxiosPromise, AxiosRequestConfig} from "axios"
import stores from "../stores/stores";
import 'element-plus/es/components/message/style/css'
import {ElMessage} from "element-plus";
const defaultBaseUrl = 'http://127.0.0.1:8080'
// // 是否正在刷新的标记
// let isRefreshing:boolean = false
// // 重试队列，每一项将是一个待执行的函数
// let reTryRequests:Function[] = []

const request = axios.create({
    baseURL: defaultBaseUrl
})

class HttpRequest { // 定义一个接口请求类，用于创建一个axios请求实例
    private readonly baseUrl: string;



    constructor(baseUrl: string | undefined) { // 这个类接收一个字符串参数，是接口请求的基本路径
        if (baseUrl === undefined){
            this.baseUrl = '';
        }else {
            this.baseUrl = baseUrl
        }
    }
    public request(options: AxiosRequestConfig): AxiosPromise { // 我们实际调用接口的时候调用实例的这个方法，他返回一个AxiosPromise
        const instance: AxiosInstance = axios.create() // 这里使用axios.create方法创建一个axios实例，他是一个函数，同时这个函数包含多个属性
        options = this.mergeConfig(options) // 合并基础路径和每个接口单独传入的配置，比如url、参数等
        this.interceptors(instance, options.url) // 调用interceptors方法使拦截器生效
        return instance(options) // 最后返回AxiosPromise
    }
    private interceptors(instance: AxiosInstance, url?: string) { // 定义这个函数用于添加全局请求和响应拦截逻辑
        instance.interceptors.request.use((config)=>{
            // 接口请求的所有配置，都在这个config对象中，他的类型是AxiosRequestConfig，你可以看到他有哪些字段
            // 如果你要修改接口请求配置，需要修改 axios.defaults 上的字段值
            // 判断本地存储是否有token，有则添加   ----不需要判断该接口是否需要携带，后端自会报错
            return config
        },(error)=>{

            return Promise.reject(error)
        })

        instance.interceptors.response.use((response)=>{
            if (response.status !== 200){
                return Promise.reject(response)
            }
            return response
        },async (error)=>{// 这里是遇到报错的回调
            /* code=401 status=40101 Jwt过期异常，可以尝试刷新，如果调用刷新接口仍旧
            *   报错，则refreshToken过期，需要重新登陆
            *   tips：刷新成功时可以判断是否返回了refreshToken，如果返回了则替换本地存储
            *   中的refreshToken
            * */
            if (error.response === undefined){
                return Promise.reject(error)
            }
            if (error.response.status === 401 && error.response.data.status === 40101){
               // jwt 过期异常
               // 判断是否有refreshToken
                let {getters} = stores
                if (getters.getRefreshToken === '') {
                    //没有refreshToken，不需要往后执行，直接return
                    await stores.dispatch('setUserAsync', '')
                    await stores.dispatch('setAccessTokenAsync','')
                    return
                }
                //有refreshToken刷新
                try {
                    const res = await axios({
                        method: 'get',
                        url: defaultBaseUrl + '/admin/auth/refresh',
                        headers: {
                            'Content-Type':'application/json;charset=utf-8',
                            Authorization: getters.getRefreshToken
                        }
                    });
                    //刷新成功将新的token更新
                    const {data} = res.data
                    await stores.dispatch('setAccessTokenAsync',data.accessToken)
                    if(res.data.data.refreshToken !== null){
                        await stores.dispatch('setRefreshTokenAsync',data.refreshToken)
                    }
                    //把之前失败的请求发出去
                    let config = error.config
                    config.headers = {
                        'Content-Type':'application/json;charset=utf-8',
                        Authorization: getters.getAccessToken
                    }
                    return request(config)
                }catch (err){
                    //如果获取失败，提示登录过期，请重新登陆
                   await stores.dispatch('setUserAsync','')
                   await stores.dispatch('setAccessTokenAsync','')
                   await stores.dispatch('setRefreshTokenAsync','')
                }
            }
            if (error.response.data.message == null || error.response.data.message == ''){
                ElMessage.error(error.toString())
                return Promise.reject(error)
            }
            ElMessage.error(error.response.data.message)
            return Promise.reject(error)
        })

    }
    private mergeConfig(options: AxiosRequestConfig): AxiosRequestConfig { // 这个方法用于合并基础路径配置和接口单独配置
        return Object.assign({ baseURL: this.baseUrl }, options);
    }
}
export default new HttpRequest(defaultBaseUrl);
