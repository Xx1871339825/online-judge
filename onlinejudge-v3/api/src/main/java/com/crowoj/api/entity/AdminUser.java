package com.crowoj.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;
/**
 * (AdminUser)表实体类
 *
 * @author crow
 * @since 2022-03-14 15:59:55
 */
@Data
@Accessors(chain = true)   
public class AdminUser{
    @TableId(type = IdType.AUTO)
    private Long adminId;
    //对应的用户id
    private Long uid;
}

