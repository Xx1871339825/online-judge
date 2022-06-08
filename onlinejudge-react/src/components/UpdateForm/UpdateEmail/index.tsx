import {FC, ReactElement, useEffect, useState} from 'react';
import {Button, Form, Input, message} from "antd";
import {checkRule, CheckRuleEnum} from "../../../utils/check.rule";
import Search from "antd/es/input/Search";
import {sendUpdateEmail} from "../../../axios/network/auth";
import {updateEmail} from "../../../axios/network/user";

interface IProps{

}

const UpdateEmail:FC<IProps> = (props):ReactElement => {
    const [countdown,setCountdown] = useState(0)
    const [isSend,setSend] = useState(false)
    const [btnLoading,setBtnLoading] = useState(false)

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
    const sendEmailMessage = message
    const sendEmail = async (email:string)=> {
        const emailReg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
        if (emailReg.test(email)){
            sendUpdateEmail(email).then(
                res => {
                    setSend(!isSend)
                    setCountdown(60)
                    sendEmailMessage.success(res.data.message)
                }
            )
        }else {
            sendEmailMessage.destroy()
            sendEmailMessage.error('请输入正确的邮箱')
        }
    }
    const submit = (value:any) => {
        setBtnLoading(true)
        updateEmail(value).then(
            () => {
                sendEmailMessage.success('修改成功！')
            }
        ).finally(()=>{
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
            onFinish={submit}
        >
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
                name="code"
                rules={checkRule(CheckRuleEnum.EMAIL_VALID_CODE)}
            >
                <Input />
            </Form.Item>
            <Form.Item wrapperCol={{ offset: 6, span: 5 }}>
                <Button loading={btnLoading} type="primary" htmlType="submit">
                    确认修改
                </Button>
            </Form.Item>
        </Form>
    );
};

export default UpdateEmail;
