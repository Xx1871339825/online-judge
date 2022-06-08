package com.crowoj.api.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author crow
 * @create 2021/10/19 20:10
 * @description
 */
@Data
@Accessors(chain = true)
@ApiModel("登录验证模型")
public class SignInParam {
    @NotBlank
    @ApiModelProperty("认证标识 账号/手机/邮箱")
    private String identifier;
    @NotBlank
    @ApiModelProperty("密码")
    private String password;
}
