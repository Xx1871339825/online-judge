package com.crowoj.api.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;

/**
 * @author crow
 * @create 2021/10/25 11:16
 * @description
 */
@Data
@Accessors(chain = true)
@ApiModel("邮箱验证模型")
public class EmailParam {
    @Email
    @ApiModelProperty("邮箱地址")
    private String email;
}
