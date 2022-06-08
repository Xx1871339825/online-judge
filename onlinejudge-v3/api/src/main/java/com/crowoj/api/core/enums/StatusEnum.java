package com.crowoj.api.core.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author crow
 * @create 2022/2/22 19:58
 * @description
 */
@AllArgsConstructor
@Getter
public enum StatusEnum implements IEnum<Integer> {
    Accepted(0,"Accepted"),// 通过
    WrongAnswer(1,"WrongAnswer"),// 答案错误
    NotAllAccepted(2,"NotAllAccepted"),//部分答案错误
    RuntimeError(3,"RuntimeError"),// 程序异常终止
    TimeLimitExceeded(4,"TimeLimitExceeded"),// 时间超出限制
    MemoryLimitExceeded(5,"MemoryLimitExceeded"),// 内存超出限制
    Pending(6,"Pending"),// 排队等待中
    Compiling(7,"Compiling"),// 编译中
    Judging(8,"Judging"), // 正在运行程序
    CompileError(9,"CompileError"); // 编译出错
    @JsonValue
    private final Integer statusNO;
    private final String status;

    @Override
    public Integer getValue() {
        return statusNO;
    }
}
