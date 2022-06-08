package com.crowoj.api.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author crow
 * @create 2021/10/24 11:09
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("注册验证模型")
public class SignUpParam {
    @ApiModelProperty("用户名")
    @Pattern(regexp = "^[a-zA-Z]\\w{5,17}$",message = "格式不正确，正确格式：以字母开头，长度在6~18之间，只能包含字符、数字和下划线。")
    private String username;
    @ApiModelProperty("密码")
    @NotBlank
    private String password;
    @ApiModelProperty("邮箱地址")
    @Email
    private String email;
    @ApiModelProperty("邮箱验证码")
    @NotBlank
    private String emailValidCode;
}
