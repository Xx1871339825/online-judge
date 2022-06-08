package com.crowoj.api.core.exception;

import com.crowoj.api.core.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author crow
 * @create 2021/10/19 20:40
 * @description
 */
@Getter
@AllArgsConstructor
@Accessors(chain = true)
public class SimpleException extends RuntimeException {
    private Integer status;
    private String message;

    public SimpleException(ResultEnum resultEnum){
        super(resultEnum.getMessage());
        this.status = resultEnum.getStatus();
        this.message = resultEnum.getMessage();
    }

}
