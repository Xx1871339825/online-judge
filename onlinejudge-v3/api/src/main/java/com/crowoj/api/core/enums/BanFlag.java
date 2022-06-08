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
 * @create 2021/10/18 21:39
 * @description
 */
@AllArgsConstructor
public enum BanFlag implements IEnum<Integer> {
    NOT_BAN(0,"ban.notBan"),IS_BAN(1,"ban.isBan");
    @JsonValue
    private final Integer banNo;
    private final String banStatus;



    private static MessageSource messageSource;
    public static void setMessageSource(MessageSource messageSource){
        BanFlag.messageSource = messageSource;
    }

    public String getBanStatus() {
        return messageSource.getMessage(banStatus,null,banStatus, LocaleContextHolder.getLocale());
    }

    public Integer getBanNo() {
        return banNo;
    }

    @Override
    public Integer getValue() {
        return banNo;
    }

    @Component
    public static class ReportTypeServiceInjector{
        @Resource
        private MessageSource messageSource;
        @PostConstruct
        public void postConstruct(){
            BanFlag.setMessageSource(messageSource);
        }
    }

}
