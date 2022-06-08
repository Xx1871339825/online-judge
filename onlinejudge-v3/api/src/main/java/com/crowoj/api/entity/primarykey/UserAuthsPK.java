package com.crowoj.api.entity.primarykey;

import com.crowoj.api.core.enums.IdentityType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author crow
 * @create 2021/10/18 11:01
 */
@ApiModel("用户安全类主键")
@Data
@Accessors(chain = true)
public class UserAuthsPK implements Serializable {
    @ApiModelProperty("绑定的用户Id")
    private Long uid;
    @ApiModelProperty("认证类型")
    private IdentityType identityType;

}
