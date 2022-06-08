const CracoLessPlugin = require('craco-less');
module.exports = {
    plugins: [{
        plugin: CracoLessPlugin,
        options: {
            lessLoaderOptions: {
                lessOptions: {
                    modifyVars: {
                        //标题主色
                        // '@heading-color': '#020202'
                    },
                    javascriptEnabled: true,
                },
            },
        },
    },],
    babel:{
        plugins: [
            [
                "import",
                {
                    "libraryName": "antd",
                    "libraryDirectory": "es",
                    "style": true //设置为true即是less
                }
            ]
        ],
    }
}

    // [
    // "import",
    //     {
    //         "libraryName": "antd",
    //         "libraryDirectory": "es",
    //         "style": true //设置为true即是less
    //     }
    // ]

    // [
    // 'babel-plugin-import',
    //     {
    //         libraryName: '@arco-design/web-react',
    //         libraryDirectory: 'es',
    //         camel2DashComponentName: false,
    //         style: true, // 样式按需加载
    //     },
    // ],
