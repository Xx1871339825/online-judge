package com.crowoj.api.entity;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.crowoj.api.core.enums.LanguageEnum;
import com.crowoj.api.core.enums.ProblemType;
import com.crowoj.api.core.enums.StatusEnum;
import com.crowoj.api.core.enums.SubmitType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
/**
 * (UserSubmit)表实体类
 *
 * @author crow
 * @since 2022-02-22 23:32:05
 */
@Data
@Accessors(chain = true)   
public class UserSubmit{
    @TableId(type = IdType.AUTO)
    private Long id;
    //提交的用户id
    private Long userId;
    //问题Id
    private Long problemId;
    //语言
    private LanguageEnum language;
    //代码
//    @JsonIgnore
    private String code;

    //判题状态
    private StatusEnum status;
    //程序运行使用时间
    private Long time;
    //程序运行使用内存
    private Long memory;
    //分数
    private Double score;

    //提交时间
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Timestamp submitTime;
    //错误提示
    private String errorMessage;
    //提交类型
    private SubmitType submitType;

    //比赛id
    private Long cid;
}

