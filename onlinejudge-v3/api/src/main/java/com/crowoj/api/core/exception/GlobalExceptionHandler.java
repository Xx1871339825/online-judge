package com.crowoj.api.core.exception;

import com.crowoj.api.core.enums.ResultEnum;
import com.crowoj.api.core.utils.ResultUtil;
import com.crowoj.api.vo.ResultVO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author crow
 * @create 2021/10/19 20:37
 * @description
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = SimpleException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResultVO<Object> handlerSimpleException(SimpleException simpleException){
        return ResultUtil.error(simpleException.getStatus(),simpleException.getMessage());
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResultVO<Object> handlerValueValidException(BindException e){
        return ResultUtil.error(400,e.getMessage());
    }

    /**
     * 处理JWT超时异常 ExpiredJwtException
     */
    @ExceptionHandler(value = ExpiredJwtException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResultVO<Object> handlerExpiredJwtExceptionException(ExpiredJwtException e){
        e.printStackTrace();
        return ResultUtil.error(ResultEnum.UNAUTHORIZED_JWT_EXPIRE);
    }
    /**
     * 处理JWT验证异常 MalformedJwtException
     */
    @ExceptionHandler(value = MalformedJwtException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResultVO<Object> handlerMalformedJwtException(MalformedJwtException e){
        e.printStackTrace();
        return ResultUtil.error(ResultEnum.UNAUTHORIZED_JWT_VALIDATION_EXCEPTION);
    }

    /**
     * 处理JWT签名验证异常 MalformedJwtException
     */
    @ExceptionHandler(value = SignatureException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResultVO<Object> handlerSignatureException(SignatureException e){
        e.printStackTrace();
        return ResultUtil.error(ResultEnum.UNAUTHORIZED_JWT_SIGNATURE_EXCEPTION);
    }

    /**
     * 处理sql唯一约束重复异常
     */
    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ResultVO<Object> handlerSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e){
        e.printStackTrace();
        return ResultUtil.error(ResultEnum.FORBIDDEN_UNIQUE);
    }

    /**
     * 处理请求头为空异常
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResultVO<Object> handlerHttpMessageNotReadableException(HttpMessageNotReadableException e){
        e.printStackTrace();
        return ResultUtil.error(ResultEnum.BAD_REQUEST_BODY_IS_MISSING);
    }

    @ExceptionHandler(value = AuthorizationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResultVO<Object> handlerAuthorizationException(AuthorizationException e){
        e.printStackTrace();
        return ResultUtil.error(ResultEnum.UNAUTHORIZED);
    }



}
