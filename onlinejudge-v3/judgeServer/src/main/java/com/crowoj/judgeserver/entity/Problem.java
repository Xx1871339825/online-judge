package com.crowoj.judgeserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;
/**
 * (Problem)表实体类
 *
 * @author crow
 * @since 2022-02-22 23:31:54
 */
@Data
@Accessors(chain = true)   
public class Problem{
    @TableId(type = IdType.AUTO)
    private Long pid;
    
    private Long uid;
    
    private String title;
    
    private Integer level;
    
    private String description;
    
    private Long timeLimit;
    
    private Long memoryLimit;
    
    private Long stackLimit;
    
    private Long favouriteCount;
    
    private Long acceptedCount;
    
    private Long tryCount;
}

