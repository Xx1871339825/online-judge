package com.crowoj.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.crowoj.api.core.enums.BanFlag;
import com.crowoj.api.core.enums.SettingType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author crow
 * @create 2021/12/20 9:59
 * @description
 */
@Data
@Accessors(chain = true)
public class Setting {
    @TableId(type = IdType.AUTO)
    private Long settingId;
    private Long userId;
    @NotBlank
    private String title;
    @NotNull
    private SettingType settingType;
    @NotNull
    private Integer sort;
    @NotBlank
    private String content;
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp createTime;
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp updateTime;
    @NotNull
    private BanFlag status;
}
