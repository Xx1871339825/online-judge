import { createApp } from 'vue'
import App from './App.vue'
import router from "./router/router";
import stores from './stores/stores';
import '@/assets/css/base.scss'
import {getTree} from "./utils/utils";
import * as ElIcons from '@element-plus/icons-vue'
import { GlobalCmComponent } from "codemirror-editor-vue3";


const app = createApp(App)
Object.keys(ElIcons).forEach(key => {
    app.component(key, ElIcons[key as keyof typeof ElIcons])
})
let menus:any[] = stores.getters.getMenus
const routes:any[] = getTree(menus);
routes.forEach(item => {
    router.addRoute('Home',item)
})
console.log(router.getRoutes())

app.use(stores).use(GlobalCmComponent).use(router).mount('#app')
