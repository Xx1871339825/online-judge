package com.crowoj.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * @author crow
 * @create 2022/3/19 16:20
 * @description 比赛缩略信息
 */
@Data
@Accessors(chain = true)
public class CinfoDTO {
    private Long id;
    private String title;
    private Integer registerCount;
    private Integer isRegister;
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp startTime;
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp endTime;
    private Integer auth;
}
