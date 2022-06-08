package com.crowoj.judgeserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;
/**
 * (ProblemCase)表实体类
 *
 * @author crow
 * @since 2022-02-22 23:57:10
 */
@Data
@Accessors(chain = true)   
public class ProblemCase{
    @TableId(type = IdType.AUTO)
    private Long id;
    //绑定的问题id
    private Long pid;
    //标准输入
    private String stdin;
    //标准输出
    private String stdout;
}

