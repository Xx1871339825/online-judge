package com.crowoj.api.vo;

import lombok.Data;

import java.util.List;

/**
 * @author crow
 * @create 2022/3/13 22:10
 * @description
 */
@Data
public class MyPage<T> {
    private List<T> records;
    private Integer size = 10;
    private Integer current = 1;
    private Long total = 0L;
}
