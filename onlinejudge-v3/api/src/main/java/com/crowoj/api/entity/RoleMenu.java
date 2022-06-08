package com.crowoj.api.entity;

import lombok.*;
import lombok.experimental.Accessors;
/**
 * (RoleMenu)表实体类
 *
 * @author crow
 * @since 2022-03-08 14:28:19
 */
@Data
@Accessors(chain = true)   
public class RoleMenu{
    
    private Long rid;
    
    private Long mid;
}

