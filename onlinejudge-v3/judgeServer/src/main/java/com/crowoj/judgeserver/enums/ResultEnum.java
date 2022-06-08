package com.crowoj.judgeserver.enums;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author crow
 * @create 2021/10/19 19:13
 * @description
 */
@AllArgsConstructor
public enum ResultEnum {
    SUCCESS(20000,"result.success"),

    BAD_REQUEST(40000,"result.badRequest"),

    BAD_REQUEST_EMAIL_ALREADY_EXISTS(40001,"result.badRequest.emailAlreadyExists"),

    BAD_REQUEST_BODY_IS_MISSING(40002,"result.badRequest.requestBodyIsMissing"),

    UNAUTHORIZED(40100,"result.unauthorized"),


    UNAUTHORIZED_JWT_EXPIRE(40101,"result.unauthorized.jwtExpire"),

    UNAUTHORIZED_JWT_VALIDATION_EXCEPTION(40102,"result.unauthorized.jwtValidationException"),

    UNAUTHORIZED_JWT_SIGNATURE_EXCEPTION(40103,"result.unauthorized.jwtSignatureException"),

    UNAUTHORIZED_JWT_INVALID(40103,"result.unauthorized.jwtInvalid"),

    UNAUTHORIZED_USERNAME_OR_PWD_ERROR(40104,"result.unauthorized.usernameOrPwdError"),

    FORBIDDEN(40300,"result.forbidden"),

    FORBIDDEN_IS_BAN(40301,"result.forbidden.isBan"),

    FORBIDDEN_UNIQUE(40302,"result.forbidden.unique"),

    NOT_FOUND(40400,"result.notFound"),
    USER_NOT_FOUND(40401,"result.user.notFound"),

    CONFLICT(40900,"result.conflict"),

    CONFLICT_USER_ALREADY_EXISTS(40901,"result.conflict.userAlreadyExists"),

    INTERNAL_SERVER_ERROR(50000,"result.internal.serverError"),


    INTERNAL_SERVER_ERROR_NOT_ALL_INSERT(50001,"result.internal.serverError.notAllInsert"),

    INTERNAL_SERVER_ERROR_SERVER_BUSY(50002,"result.internal.serverError.serverBusy")
    ;

    private final Integer status;
    private final String message;

    private static MessageSource messageSource;

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return messageSource.getMessage(message,null,message, LocaleContextHolder.getLocale());
    }

    public static void setMessageSource(MessageSource messageSource) {
        ResultEnum.messageSource = messageSource;
    }

    @Component
    public static class ReportTypeServiceInjector{
        @Resource
        private MessageSource messageSource;
        @PostConstruct
        public void postConstruct(){
            ResultEnum.setMessageSource(messageSource);
        }
    }
}
