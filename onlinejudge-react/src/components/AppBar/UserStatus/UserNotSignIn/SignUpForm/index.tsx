import {FC, ReactElement, useEffect, useState} from 'react';
import {Button, Form, Input, message} from "antd";
import Search from "antd/es/input/Search";
import {sendSignUpEmail, signUp} from "../../../../../axios/network/auth";
import {checkRule, CheckRuleEnum} from "../../../../../utils/check.rule";
import {useStore} from "../../../../../stores/store.context.provider";

interface IProps{

}

const SignUpForm:FC<IProps> = (props):ReactElement => {
    const {userStore} = useStore()
    const [countdown,setCountdown] = useState(0)
    const [isSend,setSend] = useState(false)
    const [btnLoading,setBtnLoading] = useState(false)

    const sendEmailMessage = message


    useEffect(()=>{
        if (isSend){
            const timer = setInterval(()=>{
                setCountdown(countdown => {
                    if (countdown === 0){
                        setSend(!isSend)
                        clearInterval(timer)
                    }else {
                        return countdown-1
                    }
                    return countdown
                })
            },1000)
        }
    },[isSend])

    const sendEmail = async (email:string)=> {
        const emailReg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
        if (emailReg.test(email)){
            sendSignUpEmail({email}).then((res) => {
                setSend(!isSend)
                setCountdown(60)
                sendEmailMessage.success(res.data.message)
            })
        }else {
            sendEmailMessage.destroy()
            sendEmailMessage.error('请输入正确的邮箱')
        }
    }

    const submitSignUp = (value:any) => {
        setBtnLoading(true)
        signUp(value).then(res => {
            const {data} = res.data
            userStore.user = JSON.stringify(data.user)
            userStore.accessToken = data.accessToken
            userStore.refreshToken = data.refreshToken
            message.success('注册成功，自动登录^_^!',1).then()

        })
        .finally(()=>{
            setBtnLoading(false)
        })

    }

    return (<Form
        name="basic"
        labelCol={{span: 6 }}
        wrapperCol={{ span: 17 }}
        initialValues={{ remember: true }}
        autoComplete="off"
        onFinish={submitSignUp}
    >
        <Form.Item
            label="用户名"
            name="username"
            rules={checkRule(CheckRuleEnum.USERNAME)}
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
        <Form.Item
            label="邮箱"
            name="email"
            rules={checkRule(CheckRuleEnum.EMAIL)}
        >
            <Search
                allowClear
                enterButton={countdown === 0 ? '发送':countdown}
                disabled={isSend}
                onSearch={sendEmail}
                size="large"
            />
        </Form.Item>
        <Form.Item
            label="验证码"
            name="emailValidCode"
            rules={checkRule(CheckRuleEnum.EMAIL_VALID_CODE)}
        >
            <Input />
        </Form.Item>
        <Form.Item wrapperCol={{ offset: 6, span: 5 }}>
            <Button loading={btnLoading} type="primary" htmlType="submit">
                注册
            </Button>
        </Form.Item>
    </Form>)
};

export default SignUpForm;
