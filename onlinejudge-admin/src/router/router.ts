import {createRouter, createWebHistory, RouteRecordRaw} from "vue-router";
import stores from "../stores/stores";
import 'element-plus/es/components/message/style/css'
import {getMenuList} from "../axios/network/user";
import {getTree} from "../utils/utils";
const Home = () => import('@/pages/Home/Home.vue')
const Login = () => import('@/pages/Login/Login.vue')
const Welcome = () => import('@/pages/Welcome/Welcome.vue')
const routes:Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'Home',
        meta:{
          requiresAuth: true,
        },
        component: Home,
        children:[
            {
                path: '',
                name: 'welcome',
                meta:{
                  name: '首页',
                },
                component: Welcome
            }
        ]
    },
    {
        path:'/login',
        name:'Login',
        component:Login
    },
    {
        path: '/:pathMatch(.*)',
        name: 'notFound',
        redirect: '/login',
        component: Login
    }
]

const router = createRouter({
    history:createWebHistory(),
    routes
})

const menuListToRouteList = (menus:any[]) => {
    let routes:any[] = []
    menus.forEach(item => {
        routes.push({
            path: item.menuPath,
            component: ()=>import('../pages/'+item.component+'.vue'),
            name:item.menuName,
            children: [],
            meta:{
                component: item.component,
                icon:item.icon,
                mid:item.mid,
                parentId:item.parentId,
                name:item.menuName
            }
        })
    })
    return routes
}


router.beforeEach((to,from,next)=>{
    const {getAccessToken,getMenus} = stores.getters
    if (getAccessToken == null || getAccessToken == ''){
        //未登录，判断页面是否需要登录
        if (to.matched.length > 0 && !to.matched.some(record => record.meta.requiresAuth)){
            next();
        }else {
            next({
                path: '/login'
            })
        }
    }else {
        //已登录
        if (getMenus == null || getMenus.length == 0){
            getMenuList().then((res)=>{
                const {data} = res.data
                stores.commit('setMenus',data)
                const routes:any[] = getTree(data);
               routes.forEach(item => {
                   router.addRoute('Home',item)
               })
                console.log("此时的Router",router.getRoutes())
                next({
                    path: to.path
                })
            })
        }else {
            if (to.path !== '/login'){
                next()
            }else {
                console.log('form:=',from.fullPath)
                next(from.fullPath)
            }
        }
    }
})

export default router
