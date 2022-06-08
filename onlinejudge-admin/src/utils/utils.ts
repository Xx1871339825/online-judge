
function menuListToRouteList(menus:any[])  {
    let routes:any[] = []
    menus.forEach(item => {
        if (item.menuType == 0){
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
        }
    })
    return routes
}

export function getTree(menus:any[]) {
    let toRoutes = menuListToRouteList(menus)
    let map = new Map<number,any>()
    toRoutes.forEach(item => map.set(item.meta.mid,item))
    toRoutes.forEach(item => {
        let i = map.get(item.meta.parentId)
        if (i != null && item.meta.parentId !== 0){
            let parent = map.get(item.meta.parentId)
            parent.children.push(map.get(item.meta.mid))
        }
    })
    let mapToList:any[] = [...map]
    let newRoutes:any[] = []
    for (let item of mapToList) {
        if (item[1].meta.parentId == 0){
            newRoutes.push(item[1])
        }
    }
    return newRoutes
}

export function getTimeLeft2ByMss(mss:number): string {
    let s = Number((mss/1000).toFixed(0))
    let hours = Math.round((s - 30 * 60) / (60 * 60));
    let minutes = Math.round((s - 30) / 60) % 60;
    let seconds = s % 60;
    return (hours > 0 ? (hours + "小时") : "") + (minutes > 0 ? (minutes + "分钟") : "") + (seconds > 0 ? (seconds + "秒") : "");
}

