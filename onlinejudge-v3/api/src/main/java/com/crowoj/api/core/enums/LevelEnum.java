package com.crowoj.api.core.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

/**
 * @author crow
 * @create 2022/2/21 1:59
 * @description
 */
@AllArgsConstructor
public enum LevelEnum implements IEnum<Integer> {
    EASY(0),NORMAL(1),HARD(2);
    @JsonValue
    private final Integer level;

    @Override
    public Integer getValue() {
        return level;
    }
}
