import {FC, ReactElement} from 'react';
import {Avatar, Button, Popover, Typography} from "antd";
import {useStore} from "../../../../stores/store.context.provider";
import {UserOutlined} from "@ant-design/icons";
import {Link} from "react-router-dom";
const {Title} = Typography
interface IProps{

}

const User:FC<IProps> = (props):ReactElement => {
    let {userStore} = useStore()
    let userObj:any = JSON.parse(userStore.user)
    const signOut = () => {
        userStore.user = ''
        userStore.refreshToken = ''
        userStore.accessToken = ''
        window.location.reload()
    }

    const {avatar} = userStore.getUserObj()
    const icon = avatar === null ?  <UserOutlined /> : null
    return (
        <Popover placement="bottom" title={<Title level={3}>{userObj.nickname}</Title>} content={
            <>
                <div style={{width:'fit-content',margin:'0 auto'}}>
                    <Link to={'/mine'}>个人信息</Link>

                </div>
                <div style={{width:'fit-content',margin:'10px auto'}}>
                    <Button onClick={signOut} type={'primary'} danger>注销</Button>
                </div>
            </>
        }>
            <Avatar size={'large'} icon={icon} src={avatar} />
        </Popover>
    );
};

export default User;
