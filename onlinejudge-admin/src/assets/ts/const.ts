export const settingType = [
    //BANNER(0),NOTICE(1)
    {
        id:-1,
        name:'全部'
    },
    {
        id:0,
        name:'轮播图'
    },
    {
        id:1,
        name: '公告'
    }
]
export const statusArr = [
    {
        id:0,
        name: '启用'
    },
    {
        id:1,
        name: '禁用'
    }
]

export const settingRules = {
    title: [
        {
            required:true,
            message: '标题不能为空',
            trigger: 'blur'
        },{
            max:50,
            message: '标题长度不能大于50个字符',
            trigger: 'blur'
        }
    ],
    content:[
        {
            required:true,
            message: '内容不能为空',
            trigger: 'blur'
        }
    ]
}

export const languageList = [
    {
        id: 0,
        name: 'cpp'
    },
    {
        id: 1,
        name: 'java'
    }
]

export const toolBar = ['bold',
    'underline',
    'italic',
    '-',
    'strikeThrough',
    'sub',
    'sup',
    'quote',
    'unorderedList',
    'orderedList',
    '-',
    'codeRow',
    'code',
    'link',
    'image',
    'table',
    'mermaid',
    'katex',
    '-',
    'pageFullscreen',
    'preview',
    'htmlPreview',
    'catalog',
]
