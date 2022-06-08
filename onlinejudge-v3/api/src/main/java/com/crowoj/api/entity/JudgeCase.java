package com.crowoj.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
/**
 * (JudgeCase)表实体类
 *
 * @author crow
 * @since 2022-03-01 19:00:12
 */
@Data
@Accessors(chain = true)   
public class JudgeCase{
    //提交的id
    private Long submitId;
    //题目id
    private Long pid;
    //用户id
    private Long uid;
    //单个样例评测结果
    private Integer status;
    //测试样例Id
    @JsonIgnore
    private Long caseId;
    //运行时间（ms）
    private Long time;
    //运行内存 (kb)
    private Long memory;
}

