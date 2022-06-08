package com.crowoj.api.entity;

import lombok.*;
import lombok.experimental.Accessors;
/**
 * (AdminUserRole)表实体类
 *
 * @author crow
 * @since 2022-03-08 14:28:20
 */
@Data
@Accessors(chain = true)   
public class AdminUserRole{
    
    private Long adminId;
    
    private Long rid;
}

