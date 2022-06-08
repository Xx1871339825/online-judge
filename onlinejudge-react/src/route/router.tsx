import {lazy, ReactNode,Suspense} from "react";
import Loading from "../components/Loading";
const CompetitionInfo = lazy(() => import('../pages/Competition/CompetitionInfo/index'))
const Description = lazy(() => import('../pages/Competition/CompetitionInfo/Description/index'))
const SubmitStatus = lazy(() => import('../pages/Competition/CompetitionInfo/SubmitStatus/index'))
const CRank = lazy(() => import('../pages/Competition/CompetitionInfo/Rank/index'))
const CProlblemSet = lazy(() => import('../pages/Competition/CompetitionInfo/ProblemSet/index'))
const CProblemInfo = lazy(() => import('../pages/Competition/CompetitionInfo/ProblemInfo/index'))
const CSubmitInfo = lazy(() => import('../pages/Competition/CompetitionInfo/SubmitStatus/StatusInfo/index'))

const CompetitionList = lazy(() => import('../pages/Competition/CompetitionList/index'))
const Mine = lazy(()=>import('../pages/Mine/index'))
const MineInfo = lazy(()=>import('../pages/Mine/MineInfo/index'))
const ProblemList = lazy(()=>import('../pages/ProblemSet/ProblemList/index'))
const UserHome = lazy(()=>import('../pages/UserHome/index'))
const Wrap = lazy(()=> import('../components/Wrap/index'))
const Home = lazy(()=> import('../pages/Home/index'))
const ProblemSet = lazy(()=> import('../pages/ProblemSet/index'))
const ProblemInfo = lazy(()=>import('../pages/ProblemSet/ProblemInfo/index'))
const Competition = lazy(()=> import('../pages/Competition/index'))
const Rank = lazy(()=>import('../pages/Rank/index'))
const Status = lazy(()=>import('../pages/Status/index'))
const StatusList = lazy(()=>import('../pages/Status/StatusList/index'))
const StatusInfo = lazy(() => import('../pages/Status/StatusInfo/index'))
const About = lazy(()=>import('../pages/About/index'))

//以这种懒加载的方式，可以解决整体页面闪屏问题（只在路由组件内闪屏）

// export const routes = [
//     {
//         path:'/',
//         auth:false,
//         element: lazyLoad(<Wrap />),
//         children: [
//             {
//                 index:true,
//                 auth:false,
//                 element: lazyLoad(<Home />)
//             },
//             {
//                 path: 'problem-set',
//                 auth:false,
//                 element: lazyLoad(<ProblemSet />),
//                 children:[
//                     {
//                         path:"/problem-set/:id",
//                         auth:false,
//                         element: lazyLoad(<ProblemInfo />)
//                     }
//                 ]
//             },
//             {
//                 path: 'competition',
//                 auth:false,
//                 element: lazyLoad(<Competition />)
//             },
//             {
//                 path: 'status',
//                 auth:false,
//                 element: lazyLoad(<Status />)
//             },
//             {
//                 path: 'rank',
//                 auth:false,
//                 element: lazyLoad(<Rank />)
//             },
//             {
//                 path: 'about',
//                 auth:false,
//                 element: lazyLoad(<About />)
//             }
//         ]
//     },{
//         path: '*',
//         auth:false,
//         element: lazyLoad(<Wrap />)
//     }
// ]

export const routes = [
    {
        path:'/',
        auth:false,
        element: lazyLoad(<Wrap />),
        children: [
            {
                index:true,
                auth:false,
                element: lazyLoad(<Home />)
            },
            {
                path: 'user/:id',
                auth: false,
                element: lazyLoad(<UserHome />)
            },
            {
                path: 'problem-set',
                auth:false,
                element: lazyLoad(<ProblemSet />),
                children:[
                    {
                        index:true,
                        auth: false,
                        element: lazyLoad(<ProblemList />)
                    },
                    {
                        path:":id",
                        auth:false,
                        element: lazyLoad(<ProblemInfo />)
                    }
                ]
            },
            {
                path: 'competition',
                auth:false,
                element: lazyLoad(<Competition />),
                children: [
                    {
                        index: true,
                        auth: false,
                        element: lazyLoad(<CompetitionList />)
                    },
                    {
                        path: ':id',
                        auth: true,
                        element: lazyLoad(<CompetitionInfo />),
                        children:[
                            {
                                index:true,
                                element: lazyLoad(<Description />)
                            },
                            {
                                path:'submit',
                                element: lazyLoad(<SubmitStatus />)
                            },
                            {
                                path:'rank',
                                element: lazyLoad(<CRank />)
                            },
                            {
                                path: 'problem-set',
                                element: lazyLoad(<CProlblemSet />)
                            },
                            {
                                path: 'p:pid',
                                element: lazyLoad(<CProblemInfo />),
                            },
                            {
                                path: 'status',
                                element: lazyLoad(<StatusInfo />)
                            },
                            {
                                path: '/competition/:id/submit/:sid',
                                element: lazyLoad(<CSubmitInfo />)
                            }
                        ]
                    }
                ]
            },
            {
                path: 'status',
                auth:false,
                element: lazyLoad(<Status />),
                children:[
                    {
                        index:true,
                        auth: false,
                        element: lazyLoad(<StatusList />)
                    },
                    {
                        path: ':id',
                        auth: false,
                        element: lazyLoad(<StatusInfo />)
                    }

                ]
            },
            {
                path: 'rank',
                auth:false,
                element: lazyLoad(<Rank />)
            },
            {
                path: 'about',
                auth:false,
                element: lazyLoad(<About />)
            },
            {
                path: 'mine',
                auth: true,
                element: lazyLoad(<Mine />),
                children: [
                    {
                        index: true,
                        auth: true,
                        element: lazyLoad(<MineInfo />)
                    }
                ]
            }
        ]
    },{
        path: '*',
        auth:false,
        element: lazyLoad(<Wrap children={lazyLoad(<Home />)} />)
    }
]


function lazyLoad(children:ReactNode):ReactNode{
    return (
        <Suspense fallback={<Loading/>}>
            {children}
        </Suspense>
    )
}

export const authRouter = [
    '/competition/:id',
    '/mine'
]



