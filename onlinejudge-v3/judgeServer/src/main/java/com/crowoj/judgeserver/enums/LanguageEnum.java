package com.crowoj.judgeserver.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author crow
 * @create 2022/2/22 22:31
 * @description
 */
@AllArgsConstructor
@Getter
public enum LanguageEnum implements IEnum<Integer> {
    CPP(0,"cpp"),
    JAVA(1,"java")
    ;
    private Integer languageNo;
    private String language;

    @Override
    public Integer getValue() {
        return languageNo;
    }
}
