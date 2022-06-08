package com.crowoj.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.crowoj.api.core.enums.BanFlag;
import com.crowoj.api.core.enums.GenderType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Objects;


@Accessors(chain = true)
@Data
@ApiModel("用户类")
public class User {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("用户Id")
    private Long uid;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("用户个性签名")
    private String slogan;

    @ApiModelProperty("用户性别")
    private GenderType gender;

//    @JsonIgnore
//    @ApiModelProperty("学号？选填")
//    private String sno;
//    @JsonIgnore
//    @ApiModelProperty("真实姓名？选填")
//    private String realName;
//    @JsonIgnore
//    @ApiModelProperty("学校？选填")
//    private String school;
//    @JsonIgnore
//    @ApiModelProperty("用户解题数量")
//    private Long problemSolvingCount;

    @ApiModelProperty("用户AC数")
    private Long acceptCount ;

    @ApiModelProperty("用户提交数")
    private Long submitCount;

//    @JsonIgnore
//    @ApiModelProperty("关注数")
//    private Long follow1;
//
//    @JsonIgnore
//    @ApiModelProperty("被关注数")
//    private Long follow2;

    @ApiModelProperty("封禁标识")
    private BanFlag banFlag;

}
