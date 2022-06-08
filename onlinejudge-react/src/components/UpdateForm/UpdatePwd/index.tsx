import {FC, ReactElement, useEffect, useState} from 'react';
import {Button, Form, Input, message} from "antd";
import {checkRule, CheckRuleEnum} from "../../../utils/check.rule";
import Search from "antd/es/input/Search";
import {sendSignUpEmail, sendUpdatePwdEmail} from "../../../axios/network/auth";
import {updatePwd} from "../../../axios/network/user";

interface IProps{

}

const UpdatePwd:FC<IProps> = (props):ReactElement => {
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
    const sendEmail = async ()=> {
       sendUpdatePwdEmail()
           .then( ()=>{
               setSend(!isSend)
               setCountdown(60)
               sendEmailMessage.success('发送成功')}
           )
    }

    const submit = (value:any) => {
        setBtnLoading(true)
        updatePwd(value).then(res => {
            message.success("修改成功")
        }).finally( ()=> {
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
                label="验证码"
                name="emailCode"
                rules={checkRule(CheckRuleEnum.EMAIL_VALID_CODE)}
            >
                <Search
                    onSearch={sendEmail}
                    allowClear
                    enterButton={countdown === 0 ? '获取':countdown}
                    loading={isSend}
                    size="large"
                />
            </Form.Item>
            <Form.Item
                label="新密码"
                name="newPwd"
                rules={checkRule(CheckRuleEnum.PASSWORD)}>
                <Input.Password />
            </Form.Item>
            <Form.Item wrapperCol={{ offset: 6, span: 5 }}>
                <Button loading={btnLoading} type="primary" htmlType="submit">
                    确认修改
                </Button>
            </Form.Item>
        </Form>
    );
};

export default UpdatePwd;
