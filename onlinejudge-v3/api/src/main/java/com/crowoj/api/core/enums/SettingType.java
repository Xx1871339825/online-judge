package com.crowoj.api.core.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

/**
 * @author crow
 * @create 2021/12/20 10:22
 * @description
 */
@AllArgsConstructor
public enum SettingType implements IEnum<Integer> {
    //轮播图，公告
    BANNER(0),NOTICE(1);
    @JsonValue
    private final Integer settingNo;

    @Override
    public Integer getValue() {
        return settingNo;
    }
}
