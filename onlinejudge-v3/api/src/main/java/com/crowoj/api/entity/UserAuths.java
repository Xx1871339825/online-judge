package com.crowoj.api.entity;

import com.crowoj.api.core.enums.IdentityType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserAuths {
    @ApiModelProperty("绑定的用户Id")
    private Long uid;
    @ApiModelProperty("认证类型")
    private IdentityType identityType;
    @ApiModelProperty("标识 (手机号/邮箱/用户名或第三方应用的唯一标识)")
    private String identifier;
    @ApiModelProperty("密码凭证 (站内的保存密码 , 站外的不保存或保存 token)")
    private String credential;
}
