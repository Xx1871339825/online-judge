package com.crowoj.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author crow
 * @create 2021/10/19 18:34
 * @description
 */
@Data
@Accessors(chain = true)
@ApiModel("通用返回模型")
public class ResultVO<T> {
    @ApiModelProperty("状态码")
    private Integer status;
    @ApiModelProperty("状态信息")
    private String message;
    @ApiModelProperty("数据")
    private T data;
}
