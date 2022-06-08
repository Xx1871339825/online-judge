package com.crowoj.api.core.exception;

import com.crowoj.api.core.enums.ResultEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.shiro.authc.AuthenticationException;

/**
 * @author crow
 * @create 2022/2/22 1:17
 * @description
 */
@Getter
@Setter
@Accessors(chain = true)
public class MyAuthenticationException extends AuthenticationException {
    private Integer status;
    private String message;

    public MyAuthenticationException(ResultEnum resultEnum){
        super(resultEnum.getMessage());
        this.status = resultEnum.getStatus();
        this.message = resultEnum.getMessage();
    }
}
