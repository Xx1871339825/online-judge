package com.crowoj.judgeserver.vo;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author crow
 * @create 2021/10/19 18:34
 * @description
 */
@Data
@Accessors(chain = true)
public class ResultVO<T> {
    private Integer status;
    private String message;
    private T data;
}
