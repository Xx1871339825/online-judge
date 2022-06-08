package com.crowoj.judgeserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;
/**
 * (User)表实体类
 *
 * @author crow
 * @since 2022-02-28 18:27:41
 */
@Data
@Accessors(chain = true)   
public class User{
    @TableId(type = IdType.AUTO)
    private Long uid;
    
    private String nickname;
    
    private String avatar;
    
    private String slogan;
    
    private Integer gender;
    
//    private String sno;
//
//    private String realName;
//
//    private String school;
//
//    private Long problemSolvingCount;
    
    private Long acceptCount;
    
//    private Long follow1;
//
//    private Long follow2;
    
    private Integer banFlag;
}

