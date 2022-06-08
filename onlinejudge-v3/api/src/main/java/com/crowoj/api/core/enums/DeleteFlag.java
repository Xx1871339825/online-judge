package com.crowoj.api.core.enums;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author crow
 * @create 2021/10/18 21:38
 * @description
 */
@AllArgsConstructor
public enum DeleteFlag {
    IS_DELETE(0,"delete.isDelete"),NOT_DELETE(1,"delete.notDelete");
    private final Integer deleteNo;
    private final String deleteStatus;

    private static MessageSource messageSource;

    public static void setMessageSource(MessageSource messageSource){
        DeleteFlag.messageSource = messageSource;
    }

    public String getDeleteStatus() {
        return messageSource.getMessage(deleteStatus,null,deleteStatus, LocaleContextHolder.getLocale());
    }

    @Component
    public static class ReportTypeServiceInjector{
        @Resource
        private MessageSource messageSource;
        @PostConstruct
        public void postConstruct(){
            DeleteFlag.setMessageSource(messageSource);
        }
    }
}
