package com.crowoj.judgeserver.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author crow
 * @create 2022/2/19 1:04
 * @description
 */
@AllArgsConstructor
@Getter
public enum StatusEnum {
    AC("Accepted"),
    WA("Wrong Answer"),
    MLE("Memory Limit Exceeded"),
    TLE("Time Limit Exceeded"),
    OLE("Output Limit Exceeded"),
    FE("File Error"),
    NZES("Non Zero Exit Status"),
    SIGNALLED("Signalled"),
    DS("Dangerous Syscall"),
    IE("Internal Error");
    private String status;
}
