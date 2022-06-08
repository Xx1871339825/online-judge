package com.crowoj.api.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.crowoj.api.core.enums.BanFlag;
import com.crowoj.api.core.enums.GenderType;
import com.crowoj.api.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author crow
 * @create 2022/3/3 0:50
 * @description
 */
@Data
public class UserInfoDTO {
    private Long uid;

    private String nickname;

    private String avatar;

    private String slogan;

    private GenderType gender = GenderType.HIDE;

    private Long problemSolvingCount = 0L;

    private Long acceptCount = 0L;

    private Long submitCount = 0L;

    private Long follow1 = 0L;

    private Long follow2 = 0L;

    private BanFlag banFlag = BanFlag.NOT_BAN;

    private Integer waCount;
    private Integer naAcCount;
    private Integer reCount;
    private Integer ceCount;
    private Integer mleCount;
    private Integer tleCount;

}
