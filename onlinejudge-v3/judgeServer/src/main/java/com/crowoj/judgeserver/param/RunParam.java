package com.crowoj.judgeserver.param;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author crow
 * @create 2022/2/18 22:46
 * @description
 */
/*
* interface Cmd {
    args: string[]; // 程序命令行参数
    env?: string[]; // 程序环境变量

    // 指定 标准输入、标准输出和标准错误的文件
    files?: (LocalFile | MemoryFile | PreparedFile | Collector | null)[];
    tty?: boolean; // 开启 TTY （需要保证标准输出和标准错误为同一文件）同时需要指定 TERM 环境变量 （例如 TERM=xterm）

    // 资源限制
    cpuLimit?: number;     // CPU时间限制，单位纳秒
    clockLimit?: number;   // 等待时间限制，单位纳秒 （通常为 cpuLimit 两倍）
    memoryLimit?: number;  // 内存限制，单位 byte
    stackLimit?: number;   // 栈内存限制，单位 byte
    procLimit?: number;    // 线程数量限制
    cpuRateLimit?: number; // 仅 Linux，CPU 使用率限制，1000 等于单核 100%
    cpuSetLimit?: string;  // 仅 Linux，限制 CPU 使用，使用方式和 cpuset cgroup 相同 （例如，`0` 表示限制仅使用第一个核）
    strictMemoryLimit?: boolean; // 开启严格内存限制 （仅 Linux，设置 rlimit 内存限制）

    // 在执行程序之前复制进容器的文件列表
    copyIn?: {[dst:string]:LocalFile | MemoryFile | PreparedFile};

    // 在执行程序后从容器文件系统中复制出来的文件列表
    // 在文件名之后加入 '?' 来使文件变为可选，可选文件不存在的情况不会触发 FileError
    copyOut?: string[];
    // 和 copyOut 相同，不过文件不返回内容，而是返回一个对应文件 ID ，内容可以通过 /file/:fileId 接口下载
    copyOutCached?: string[];
    // 指定 copyOut 复制文件大小限制，单位 byte
    copyOutMax?: number;
}
* */
@Data
public class RunParam {
    //@NotNull
    private Long cpuLimit; // CPU时间限制，单位纳秒
    //@NotNull
    private Long clockLimit; // 等待时间限制，单位纳秒 （通常为 cpuLimit 两倍）
    //@NotNull
    private Long memoryLimit; // 内存限制，单位 byte
    //@NotNull
    private Long stackLimit; // 栈内存限制，单位 byte
    private Long copyOutMax; // 指定 copyOut 复制文件大小限制，单位 byte
    private Long procLimit; // 线程数量限制

    private Long stdoutMax = 10240L; //标准输出文件大小限制 默认10240
    private Long stderrMax = 10240L; //标准错误文件大小限制 默认10240

    private String fileId;
    private String stdin = ""; //标准输入
    //@NotNull
    private String language;//代码类型
    //@NotNull
    private String content;//代码内容


}
