import {createStore, StoreOptions} from "vuex";
import {getMenuList} from "../axios/network/user";

const userStore:StoreOptions<any> = {
    state:{
        user: {},
        accessToken: '',
        refreshToken: '',
        roles: [],
        menus: []
    },
    mutations: {
        setUser(state,data){
            state.user = data
            localStorage.setItem('user',JSON.stringify(data))
        },
        setAccessToken(state,data){
            state.accessToken = data
            localStorage.setItem('accessToken',data)
        },
        setRefreshToken(state,data){
            state.refreshToken = data
            localStorage.setItem('refreshToken',data)
        },
        setRoles(state,data){
            state.roles = data
            localStorage.setItem('roles',JSON.stringify(data))
        },
        setMenus(state,data){
            state.menus = data
            localStorage.setItem('menus',JSON.stringify(data))
        },
        setUserStore(state,data){
            const {user,accessToken,refreshToken} = data
            state.user = user
            state.accessToken = accessToken
            state.refreshToken = refreshToken
            localStorage.setItem("user",JSON.stringify(user))
            localStorage.setItem("accessToken",accessToken)
            localStorage.setItem("refreshToken",refreshToken)
        },
        signOut(state,data){
            state.user = {}
            state.accessToken = ''
            state.refreshToken= ''
            state.role=[]
            state.menus = []
            localStorage.clear()
        }
    },

    actions: {
        setUserAsync({commit},data){
            commit('setUser',data)
        },
        setAccessTokenAsync({commit},data){
            commit('setAccessToken',data)
        },
        setRefreshTokenAsync({commit},data){
            commit('setRefreshToken',data)
        },
        setRolesAsync({commit},data){
            commit('setRoles',data)
        },
        setMenusAsync({commit}){
            getMenuList().then(res => {
                commit('setMenus',res.data.data)
            })
        },
        setUserStoreAsync({commit},data){
            commit('setUserStore',data)
        }
    },
    getters:{
        getUser: state => {
            const userStr = localStorage.getItem('user')
            state.user = userStr == null? {}:JSON.parse(userStr)
            return state.user
        },
        getAccessToken: state => {
            const accessToken = localStorage.getItem('accessToken')
            state.accessToken = accessToken == null? '':accessToken
            return state.accessToken
        },
        getRefreshToken: state => {
            const refreshToken = localStorage.getItem('refreshToken')
            state.refreshToken = refreshToken == null ? '':refreshToken
            return state.refreshToken
        },
        getRoles: state => {
            const roles = localStorage.getItem('roles')
            state.roles = roles == null? []:[...JSON.parse(roles)]
            return state.roles
        },
        getMenus: state => {
            const menus = localStorage.getItem('menus')
            state.menus = menus == null? []:[...JSON.parse(menus)]
            return state.menus
        }
    }
}

const store:StoreOptions<any> = {
    modules:{
        userStore
    }
}

const stores = createStore(store)
export default stores
