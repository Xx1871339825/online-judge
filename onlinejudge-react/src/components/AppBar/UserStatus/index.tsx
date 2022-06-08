import {FC, ReactElement} from 'react';
import UserNotSignIn from "./UserNotSignIn";
import {observer} from "mobx-react-lite";
import {useStore} from "../../../stores/store.context.provider";
import User from "./User";


interface IProps{

}

const UserStatus:FC<IProps> = (props):ReactElement => {
    let signIn
    //获取mobx的登录信息，然后决定渲染的组件
    let {userStore} = useStore();
    if (userStore.user === ''){
        console.log('未登录')
        signIn = false
    }else {
        signIn = true
    }
    //未登录，构建表单数据

    return (
        <div style={{width:'fit-content',height:'fit-content'}}>
            {signIn?
                <User />:
                <UserNotSignIn />
            }
        </div>
    );
};

export default observer(UserStatus);
