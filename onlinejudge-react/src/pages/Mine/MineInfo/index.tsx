import {FC, ReactElement, useState} from 'react';
import {useStore} from "../../../stores/store.context.provider";
import './index.less'
import {Button, Col, Input, message, Modal, Row, Select, Typography, Upload} from "antd";
import {LoadingOutlined, LockOutlined, MailOutlined, PlusOutlined} from "@ant-design/icons";
import {genderList} from "../../../assets/ts/const";
import {updateUser, upLoadAvatar} from "../../../axios/network/user";
import {UpdateTypeEnum} from "../../../enums/UpdateTypeEnum";
import UpdateEmail from "../../../components/UpdateForm/UpdateEmail";
import UpdatePwd from "../../../components/UpdateForm/UpdatePwd";

const {Title} = Typography
const { Option } = Select;


interface IProps{

}

const MineInfo:FC<IProps> = (props):ReactElement => {

    const {userStore} = useStore();
    const [uploadLoading,setUploadLoading] = useState(false)
    const [userObj,setUserObj] = useState<any>(JSON.parse(userStore.user))

    const [nickName,setNickname] = useState(userObj.nickname)
    const [slogan,setSlogan] = useState(userObj.slogan)
    const [gender,setGender] = useState(userObj.gender)
    const [visible,setVisible] = useState(false)
    const [modalTitle,setModalTitle] = useState('')

    const [currentForm,setCurrentForm] = useState(UpdateTypeEnum.EMAIL)

    const changeCurrentForm = (current:number) => {
        return () => {
            setVisible(!visible)
            setCurrentForm(current)
        }
    }
    let form
    if (currentForm === UpdateTypeEnum.EMAIL){
        form = <UpdateEmail />
    }else {
        form = <UpdatePwd />
    }
    const nicknameChange = (e:any) => {
        setNickname(e.target.value)
    }

    const handleCancel = () => {
        setVisible(false)
    }
    const sloganChange = (e:any) => {
        setSlogan(e.target.value)
    }

    const genderChange = (value:any) => {
        setGender(value)
    }

    const onClickUpdate = () => {
        updateUser({
            nickname:nickName,
            slogan: slogan,
            gender: gender
        }).then(res=>{
            const {data} = res.data
            setUserObj(data)
            userStore.user = JSON.stringify(data)
            message.success('修改成功')
        })
    }

    const beforeUpload = (file:any) => {
        const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
        if (!isJpgOrPng) {
            message.error('头像文件必须为jpg/png');
        }
        const isLt10M = file.size / 1024 / 1024 < 10;
        if (!isLt10M) {
            message.error('图片不能大于10M');
        }
        return isJpgOrPng && isLt10M;
    }

    const submitNewAvatar = (file:any) => {
        upLoadAvatar(file.file).then(res => {
            const {data} = res.data
            setUserObj(data)
            userStore.user = JSON.stringify(data)
        })
    }

    const uploadButton = (
        <div>
            {uploadLoading ? <LoadingOutlined /> : <PlusOutlined />}
            <div style={{ marginTop: 8 }}>Upload</div>
        </div>
    );

    return (
        <div className={'mineInfo'}>
            <div className={'avatarBox'}>
                <Upload
                    beforeUpload={beforeUpload}
                    name="avatar"
                    listType="picture-card"
                    className="avatar-uploader"
                    showUploadList={false}
                    customRequest={submitNewAvatar}
                >
                    {userObj.avatar ? <img height={'180px'} width={'180px'} src={userObj.avatar} alt="avatar" style={{ width: '100%'}} /> : uploadButton}
                </Upload>
            </div>
            <div className={"mineInfoBox"}>
                <div className={'titleBox'}>
                    个人信息
                </div>
                <div className={'infoBox'}>
                    <div className={'item'}>
                        <Title level={5}>昵称:</Title>
                        <Input onChange={nicknameChange} defaultValue={userObj.nickname} />
                    </div>
                    <div className={'item'}>
                        <Title level={5}>个性签名:</Title>
                        <Input onChange={sloganChange} defaultValue={userObj.slogan} />
                    </div>
                    <div className={'item'}>
                        <Title level={5}>性别:</Title>
                        <Select onChange={genderChange} defaultValue={userObj.gender} style={{ width: 120 }}>
                            {genderList.map(item => {
                                return <Option key={item.id} value={item.id}>{item.gender}</Option>
                            })}
                        </Select>
                    </div>
                    <div className={'updateBtn'}>
                        <Button onClick={onClickUpdate} size={"large"} type={'primary'}>更新信息</Button>
                    </div>
                </div>
            </div>
            <div className={'authBox'}>
                <div className={'titleBox'}>
                    账号安全
                </div>
                <div className={'authItem'}>
                    <Row>
                        <Col span={18}><LockOutlined /> 密码</Col>
                        <Col span={6} style={{textAlign:'center'}}><Button onClick={changeCurrentForm(UpdateTypeEnum.PASSWORD)} >修改密码</Button></Col>
                    </Row>
                </div>
                <div className={'authItem'}>
                    <Row>
                        <Col span={18}><MailOutlined /> 邮箱</Col>
                        <Col span={6} style={{textAlign:'center'}}><Button onClick={changeCurrentForm(UpdateTypeEnum.EMAIL)}>修改邮箱</Button></Col>
                    </Row>
                </div>
                <Modal
                    title={'修改信息'}
                    visible={visible}
                    onCancel={handleCancel}
                    width={440}
                    className={'none-modal-header-bottom'}
                    footer={null}
                >
                    {form}
                </Modal>
            </div>
        </div>
    );
};

export default MineInfo;
