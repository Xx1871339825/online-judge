package com.crowoj.judgeserver.vo;

/**
 * @author crow
 * @create 2022/2/23 4:01
 * @description
 */

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * [
 *     {
 *         "status": "Accepted",
 *         "exitStatus": 0,
 *         "time": 47540151,
 *         "memory": 28106752,
 *         "runTime": 294058247,
 *         "files": {
 *             "stderr": "",
 *             "stdout": "helloWord!\n"
 *         }
 *     }
 * ]
 */

/**
 * interface Result {
 *     status: Status;
 *     error?: string; // 详细错误信息
 *     exitStatus: number; // 程序返回值
 *     time: number;   // 程序运行 CPU 时间，单位纳秒
 *     memory: number; // 程序运行内存，单位 byte
 *     runTime: number; // 程序运行现实时间，单位纳秒
 *     // copyOut 和 pipeCollector 指定的文件内容
 *     files?: {[name:string]:string};
 *     // copyFileCached 指定的文件 id
 *     fileIds?: {[name:string]:string};
 *     // 文件错误详细信息
 *     fileError?: FileError[];
 * }
 */
@Data
@Accessors(chain = true)
public class SandBoxResult {
    private String status;
    private Integer exitStatus;
    private Long time;
    private Long memory;
    private Long runTime;
    private Map<String,String> files;

}
