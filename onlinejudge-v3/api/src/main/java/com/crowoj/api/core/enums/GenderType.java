package com.crowoj.api.core.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author crow
 * @create 2021/10/14 18:48
 */
@AllArgsConstructor
public enum GenderType implements IEnum<Integer> {
    MALE(0,"gender.male"),FEMALE(1,"gender.female"),HIDE(2,"gender.hide");
    @JsonValue
    private final Integer genderNo;
    private final String gender;

    private static MessageSource messageSource;

    public static void setMessageSource(MessageSource messageSource){
        GenderType.messageSource = messageSource;
    }

    public String getGender() {
        return messageSource.getMessage(gender,null,gender, LocaleContextHolder.getLocale());
    }

    @Override
    public Integer getValue() {
        return genderNo;
    }

    @Component
    public static class ReportTypeServiceInjector{
        @Resource
        private MessageSource messageSource;
        @PostConstruct
        public void postConstruct(){
            GenderType.setMessageSource(messageSource);
        }
    }
}
