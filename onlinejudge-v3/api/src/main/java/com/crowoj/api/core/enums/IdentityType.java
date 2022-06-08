package com.crowoj.api.core.enums;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@AllArgsConstructor
public enum IdentityType implements IEnum<Integer> {
    PHONE(0,"identity.phone"),EMAIL(1,"identity.email"),USERNAME(2,"identity.username");
    @JsonValue
    private final Integer identityNo;
    private final String identity;

    private static MessageSource messageSource;
    public static void setMessageSource(MessageSource messageSource){
        IdentityType.messageSource = messageSource;
    }

    public String getIdentity() {
        return messageSource.getMessage(identity,null,identity, LocaleContextHolder.getLocale());
    }

    @Override
    public Integer getValue() {
        return identityNo;
    }

    @Component
    public static class ReportTypeServiceInjector{
        @Resource
        private MessageSource messageSource;
        @PostConstruct
        public void postConstruct(){
            IdentityType.setMessageSource(messageSource);
        }
    }
}