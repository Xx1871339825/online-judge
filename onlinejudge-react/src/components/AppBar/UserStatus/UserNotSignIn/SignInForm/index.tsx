import {FC, ReactElement, useState} from 'react';
import {Button, Form, Input, message} from "antd";
import {useStore} from "../../../../../stores/store.context.provider";
import {checkRule, CheckRuleEnum} from "../../../../../utils/check.rule";
import {signIn} from "../../../../../axios/network/auth";


interface IProps{

}

const SignInForm:FC<IProps> = (props):ReactElement => {

    const {userStore} = useStore()

    const [btnLoading,setBtnLoading] = useState(false)

    const submitSignIn = (values:any) => {
        const {username,password} = values
        //登录
        setBtnLoading(true)
        signIn({identifier:username,password})
            .then(res => {
                const {data} = res.data
                userStore.user = JSON.stringify(data.user)
                userStore.accessToken = data.accessToken
                userStore.refreshToken = data.refreshToken
                message.success('登录成功^_^!',1).then()
            })
            .finally(()=>{
                setBtnLoading(false)
            })


    }


    return (
        <Form
            name="basic"
            labelCol={{span: 6 }}
            wrapperCol={{ span: 17 }}
            initialValues={{ remember: true }}
            autoComplete="off"
            onFinish={submitSignIn}

        >
            <Form.Item
                label="账号/邮箱"
                name="username"
                rules={checkRule(CheckRuleEnum.USERNAME_OR_EMAIL)}
            >
                <Input />
            </Form.Item>

            <Form.Item
                label="密码"
                name="password"
                rules={checkRule(CheckRuleEnum.PASSWORD)}
            >
                <Input.Password />
            </Form.Item>
            <Form.Item wrapperCol={{ offset: 6, span: 5 }}>
                {userStore.accessToken}
                <Button loading={btnLoading} type="primary" htmlType="submit">
                    登录
                </Button>
            </Form.Item>
        </Form>
    )
};

export default SignInForm;
