package com.crowoj.api.vo;

import com.crowoj.api.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author xiaoxing
 * @Description
 * @create 2020/12/14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel("jwt返回模型")
public class JWTVO {
    @ApiModelProperty("用户")
    private User user;
    @ApiModelProperty("访问令牌，用于请求接口")
    private String accessToken;
    @ApiModelProperty("刷新令牌，当访问令牌过期时，使用刷新令牌获取新的访问令牌")
    private String refreshToken;
}
