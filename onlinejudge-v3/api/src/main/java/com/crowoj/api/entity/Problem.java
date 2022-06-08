package com.crowoj.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.crowoj.api.core.enums.LevelEnum;
import com.crowoj.api.core.enums.ProblemType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Objects;

@Data
@Accessors(chain = true)
public class Problem {
    /**
     * 题目 Id
     */
    @TableId(type = IdType.AUTO)
    private Long pid;
    /**
     * 题目添加者，默认0=管理员
     */
    private Long uid;
    /**
     * 标题
     */
    private String title;
    /**
     * 等级（难度）0=简单，1=中等，2=困难
     */
    private LevelEnum level;
    /**
     * 题目描述
     */
    private String description;
    /**
     * 时间限制 默认为c/c++限制，其他语言*2
     */
    private Long timeLimit;
    /**
     * 空间限制 默认为c/c++限制，其他语言*2
     */
    private Long memoryLimit;
    /**
     * 栈限制 默认为c/c++限制，其他语言*2
     */
    private Long stackLimit;
    /**
     * 收藏数量
     */
    private Long favouriteCount;
    /**
     * 总通过数量
     */
    private Long acceptedCount;
    /**
     * 总尝试次数
     */
    private Long tryCount;

    private ProblemType problemType;

}