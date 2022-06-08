package com.crowoj.api.entity;

import lombok.*;
import lombok.experimental.Accessors;
/**
 * (Favorite)表实体类
 *
 * @author crow
 * @since 2022-02-22 16:44:27
 */
@Data
@Accessors(chain = true)   
public class Favorite{
    //用户id
    private Long uid;
    //题目id
    private Long pid;
}

