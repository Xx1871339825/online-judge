import {FC, ReactElement, useEffect, useState} from 'react';
import {Button, Menu, message, Modal} from "antd";
import './index.less'
import {CurrentSelectEnum} from "../../../../enums/CurrentSelectEnum";
import SignInForm from "./SignInForm";
import SignUpForm from "./SignUpForm";
import ForgetPwd from "./ForgetPwd";
import {observer} from "mobx-react-lite";
import {useStore} from "../../../../stores/store.context.provider";

const MenuItem = Menu.Item


interface IProps{

}

const UserNotSignIn:FC<IProps> = (props):ReactElement => {


    let modalStore = useStore().modalStore

    let visible = modalStore.visible

   // const [visible,setVisible] = useState(false)

    const showModal = () => {
        //setVisible(!visible)
        modalStore.visible = !modalStore.visible
    }

    const handleCancel = () => {
        //setVisible(false)
        modalStore.visible = false
    }

    const [currentSelect,setCurrentSelect] = useState(CurrentSelectEnum.SIGN_IN)
    let form;
    if (currentSelect === CurrentSelectEnum.SIGN_IN){
        form = (<SignInForm />)
    }else if (currentSelect === CurrentSelectEnum.SIGN_UP){
        form = (<SignUpForm/>)
    }else {
        form = (<ForgetPwd />)
    }

    const changeCurrentSelect = (current:number) => {
        return () => {
            setCurrentSelect(current)
        }
    }

    return (
        <div className={'user-not-sign-in'}>
            {/*<Avatar className={'hover-pointer my-avatar'} size={'large'}>登录</Avatar>*/}
            <Button onClick={showModal} className={'login-btn'} type={"text"}>登录</Button>
            <Modal
                width={440}
                className={'none-modal-header-bottom'}
                visible={visible}
                title={
                    <Menu  mode="horizontal" defaultSelectedKeys={['signIn']}>
                        <MenuItem key="signIn" onClick={changeCurrentSelect(CurrentSelectEnum.SIGN_IN)}>
                            登录
                        </MenuItem>
                        <MenuItem key="signUp" onClick={changeCurrentSelect(CurrentSelectEnum.SIGN_UP)}>
                            注册
                        </MenuItem>

                        <MenuItem key="forgetPwd" onClick={changeCurrentSelect(CurrentSelectEnum.FORGET_PWD)}>
                            忘记密码
                        </MenuItem>
                    </Menu>
                }
                onCancel={handleCancel}
                footer={null}
            >
                {form}
            </Modal>
        </div>
    );
};

export default observer(UserNotSignIn);
