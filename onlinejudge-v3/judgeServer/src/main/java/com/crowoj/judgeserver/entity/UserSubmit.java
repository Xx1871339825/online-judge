package com.crowoj.judgeserver.entity;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.crowoj.judgeserver.enums.SubmitStatusEnum;
import lombok.*;
import lombok.experimental.Accessors;
/**
 * (UserSubmit)表实体类
 *
 * @author crow
 * @since 2022-02-22 23:57:10
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
    private Integer language;
    //代码
    private String code;

    //最终判题结果
    private SubmitStatusEnum status;
    //程序运行使用时间
    private Long time;
    //程序运行使用内存
    private Long memory;
    //分数
    private Double score;
    //提交时间
    private Timestamp submitTime;
    //错误提示
    private String errorMessage;
    //比赛id
    private Long cid;
}

