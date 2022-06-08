import {FC, ReactElement, useEffect, useRef, useState} from 'react';
import Particles from "reactparticles.js";
import {Button, Card, Carousel, Image, Input, Modal, Pagination, Space, Table} from "antd";
import "./index.less"
import Title from "antd/es/typography/Title";
import {getArticleList, getBannerList} from "../../axios/network/setting";
import {useNavigateParams} from "../../utils/hooks";
import {useLocation, useSearchParams} from "react-router-dom";
import Announcement from "../../components/Announcement";
const { Column} = Table;
const {Search} = Input
interface IProps{

}
const fallbackImg = 'data:image/png;base64,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'

const Home:FC<IProps> = (props):ReactElement => {
    document.title = 'CrowOj'
    let navigateParams = useNavigateParams()
    let pathname = useLocation().pathname
    let [searchParam] = useSearchParams()
    let searchRef:any = useRef()
    const [pageObj,setPageObj] = useState<any>({
        tableData: [],
        total: 0,
        current: Number(searchParam.get("current"))===0?1:Number(searchParam.get("current")),
        search:searchParam.get("search")
    })

    const onChangePage = (currentPage:any) => {
        setPageObj({
            ...pageObj,
            current: currentPage
        })
        navigateParams(pathname,{
            current: currentPage,
            search: pageObj.search
        })
    }

    const onChangeSearch = (value:string) => {
        setPageObj({
            ...pageObj,
            search: value
        })
        navigateParams(pathname,{
            current: pageObj.current,
            search:value
        })
    }

    let [bannerList,setBannerList] = useState(Array<any>())

    useEffect(()=>{
        getBannerList().then(res=>{
            const {data} = res.data
            data.sort((o1:any,o2:any)=>{
                return o1.sort - o2.sort
            })
            setBannerList(data)
        })
        getArticleList({
            search: pageObj.search,
            current: pageObj.current,
            size: 10
        }).then(res => {
            const {data} = res.data
            const {records} = data
            records.sort((o1:any,o2:any)=>{
                return o1.sort - o2.sort
            })
            console.log(data)
            if (pageObj.search != null && pageObj.search !== ''){
                searchRef.current.state.value = pageObj.search
            }
            setPageObj({
                ...pageObj,
                tableData:records,
                total: data.total
            })

        })
    },[pageObj.current,pageObj.search])

    const [visible,setVisible] = useState(false)
    const [record,setRecord] = useState<any>({})
    const clickAnnouncement = (record:any) => {
        return ()=> {
            setRecord(record)
            setVisible(!visible)
        }
    }
    const handleCancel = () => {
        setVisible(!visible)
    };

    return (
        <>
            <Particles id="particles-1" config="particles.json"/>
            <Card title="Welcome to Crow Online Judge System V2.0" className={'cardClass'}>
                <div className={'home'}>
                    {/*<div className={'title'}>*/}
                    {/*    <Title style={{color:'black'}}>Welcome to Crow Online Judge System V2.0</Title>*/}
                    {/*</div>*/}
                    <div className="banner">
                        <Carousel autoplay>
                            {bannerList.map(value =>
                                <div className={"banner-item"} key={value.settingId}>
                                    <Image
                                        preview={false}
                                        src={value.content}
                                        fallback={fallbackImg}
                                    />
                                </div>
                            )}
                        </Carousel>
                    </div>

                    <div className={'tabBox'}>
                        <div className={"searchBox"}>
                            <Search
                                ref={searchRef}
                                placeholder="搜索发布用户/标题"
                                allowClear
                                size="large"
                                onSearch={onChangeSearch}
                            />
                        </div>
                        <Table pagination={false} dataSource={pageObj.tableData} rowKey={record => record.settingId}>
                            <Column align={'center'} title="用户名" dataIndex="nickname" key="nickname" />
                            <Column align={'center'} title="标题" dataIndex="title" key="title"
                                render={(text,record:any) => {
                                    return <Button type={'text'} onClick={clickAnnouncement(record)}>{text}</Button>
                                }}
                            />
                            <Column align={'center'} title="发布时间" dataIndex="createTime" key="createTime" />
                            <Column align={'center'} title="更新时间" dataIndex="updateTime" key="updateTime" />
                        </Table>
                    </div>

                    <div className={"pWrap"}>
                        <div className={"pagination"}>
                            <Pagination showSizeChanger={false}
                                        showQuickJumper current={pageObj.current}
                                        // total={pageObj.total}
                                        total={500}
                                        onChange={onChangePage}
                            />
                        </div>
                    </div>

                </div>

                <Modal width={"80%"} onCancel={handleCancel} footer={null}
                       title={
                           <div>
                               <Space size={30}>
                                   <span>{record.title}</span>
                                   <span>作者：{record.nickname}</span>
                                   <span>发布时间：{record.createTime}</span>
                                   {record.updateTime != null&& record.updateTime!=='' ?
                                       <span>更新时间：{record.updateTime}</span>:null
                                   }
                               </Space>
                            </div>
                       }
                       visible={visible}>
                    <Announcement {...record} />
                </Modal>

            </Card>
        </>
    );
};

export default Home;
