package com.crowoj.api.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
/**
 * (Competition)表实体类
 *
 * @author crow
 * @since 2022-03-18 14:20:33
 */
@Data
@Accessors(chain = true)   
public class Competition{
    //主键
    @TableId(type = IdType.AUTO)
    private Long id;
    //比赛标题
    private String title;
    //0=公开赛，1=需要密码
    private Integer auth;
    //比赛密码
    private String password;
    //开始时间
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;
    //结束时间
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;
    //比赛说明
    private String description;
    //创建比赛的用户
    private Long uid;
}

