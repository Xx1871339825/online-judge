export enum CheckRuleEnum {
    TEL_PHONE,
    VALID_CODE,
    EMAIL,
    INPUT_NOT_NULL,
    USERNAME,
    USERNAME_OR_EMAIL,
    PASSWORD,
    EMAIL_VALID_CODE
}

export const checkRule = (type:CheckRuleEnum) => {
    switch (type){
        case CheckRuleEnum.TEL_PHONE:
            return ([{
                required: true,
                pattern: /^1[345789]\d{9}$/,
                message: '手机号格式不正确'
            }])
        case CheckRuleEnum.VALID_CODE:
            return ([{}])
        case CheckRuleEnum.EMAIL:
            return ([{required: true,message:''},{
                validator:async (rule:any, value:string)=>{
                    const emailReg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
                    if (value === undefined){
                        throw new Error('邮箱不能为空')
                    }
                    if (!emailReg.test(value)){
                        throw new Error('邮箱格式不正确')
                    }
                }
            }])
        case CheckRuleEnum.INPUT_NOT_NULL:
            return ([{
                required: true,
                message: '输入不能为空'
            }])
        case CheckRuleEnum.USERNAME:
            return ([{required: true,message:''},{
                validator:async (rule:any, value:string)=>{
                    const accountReg = /^[a-zA-Z]\w{5,17}$/
                    if (value === undefined){
                        throw new Error('账号不能为空')
                    }
                    if (!accountReg.test(value)){
                        throw new Error('账号只能由大小写字母开头+数字组成，长度为6-18位')
                    }
                }
            }])
        case CheckRuleEnum.USERNAME_OR_EMAIL:
            return ([{required: true,message:''},{
                validator:async (rule:any, value:string)=>{
                    const accountReg = /^[a-zA-Z]\w{5,17}$/
                    const emailReg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
                    if (value === undefined){
                        throw new Error('账号不能为空')
                    }
                    if (value.indexOf('@')===-1&&!accountReg.test(value)){
                        throw new Error('账号只能由大小写字母开头+数字组成，长度为6-18位') //return Promise.reject('')
                    }
                    if (value.indexOf('@') !== -1 && !emailReg.test(value)) throw new Error('邮箱格式不正确');

                }
            }])
        case CheckRuleEnum.PASSWORD:
            return ([{required: true,message:''},{
                validator:async (rule:any, value:string)=>{
                    const passwordReg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$/
                    if (value === undefined || value === null){
                        throw new Error('密码不能为空')
                    }
                    if (!passwordReg.test(value)){
                        throw new Error('密码只能由大小写字母和数字组成，长度为6-18位')
                    }
                }
            }])
        case CheckRuleEnum.EMAIL_VALID_CODE:
            return ([
                {required: true, message: '邮箱验证码不能为空'}
            ])
        default:
            return ;
    }
}
