package com.crowoj.api.core.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author crow
 * @create 2022/3/3 15:09
 * @description
 */
@AllArgsConstructor
@Getter
public enum ProblemType implements IEnum<Integer> {
    DEFAULT(0),COMPETITION(1)
    ;
    @JsonValue
    private Integer problemType;

    @Override
    public Integer getValue() {
        return problemType;
    }


}
