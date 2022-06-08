package com.crowoj.judgeserver.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * @author crow
 * @create 2022/2/19 0:09
 * @description
 */
@Data
@Accessors(chain = true)
public class RunDto {
    private String[] args;
    private String[] env = new String[]{"PATH=/usr/bin:/bin"};
    private Long cpuLimit; // CPU时间限制，单位纳秒
    private Long clockLimit; // 等待时间限制，单位纳秒 （通常为 cpuLimit 两倍）
    private Long memoryLimit; // 内存限制，单位 byte
    private Long stackLimit; // 栈内存限制，单位 byte
    private Long copyOutMax; // 指定 copyOut 复制文件大小限制，单位 byte
    private Long procLimit; // 线程数量限制

    private List<Map<String,Object>> files;
    private Map<String,Object> copyIn;
    private String[] copyOut;
    private String[] copyOutCached;
}
