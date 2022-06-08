package com.crowoj.api.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author crow
 * @create 2022/2/22 22:20
 * @description
 */
@AllArgsConstructor
@Getter
public enum SandboxStatusEnum {
    AC("Accepted"),
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
