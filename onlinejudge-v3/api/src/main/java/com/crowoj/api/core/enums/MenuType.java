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
 * @create 2021/10/18 23:50
 * @description
 */
@AllArgsConstructor
public enum MenuType implements IEnum<Integer> {
    MENU(0,"menu.menu"),BUTTON(1,"menu.button");
    @JsonValue
    private final Integer menuNo;
    private final String menuType;


    private static MessageSource messageSource;
    private static void setMessageSource(MessageSource messageSource){
        MenuType.messageSource = messageSource;
    }

    public String getMenuType() {
        return messageSource.getMessage(menuType,null,menuType, LocaleContextHolder.getLocale());
    }

    @Override
    public Integer getValue() {
        return menuNo;
    }

    @Component
    public static class ReportTypeServiceInjector{
        @Resource
        private MessageSource messageSource;
        @PostConstruct
        public void postConstruct(){
            MenuType.setMessageSource(messageSource);
        }
    }
}
