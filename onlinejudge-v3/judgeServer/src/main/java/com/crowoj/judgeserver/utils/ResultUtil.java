package com.crowoj.judgeserver.utils;

import com.crowoj.judgeserver.enums.ResultEnum;
import com.crowoj.judgeserver.vo.ResultVO;

/**
 * @author crow
 * @create 2021/10/19 18:36
 * @description 统一返回工具类
 */
public class ResultUtil {
    /**
     * 成功且携带数据且携带自定义msg
     * @param message 自定义Msg
     * @param data 数据
     * @return 统一返回类
     */
    public static <T> ResultVO<T> success(String message, T data){
        return new ResultVO<T>()
                .setData(data)
                .setMessage(message)
                .setStatus(ResultEnum.SUCCESS.getStatus());
    }

    /**
     * 成功且携带数据
     * @param data 数据
     * @return 统一返回类
     */
    public static <T> ResultVO<T> success(T data){
        return new ResultVO<T>()
                .setStatus(ResultEnum.SUCCESS.getStatus())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(data);
    }

    /**
     * 成功但不携带数据
     * @return 统一返回类
     */
    public static <T> ResultVO<T> success(){
        return success(null);
    }

    /**
     * 失败携带自定义状态码和信息
     * @param status 状态码
     * @param message 信息
     * @return 统一返回类
     */
    public static <T> ResultVO<T> error(Integer status,String message){
        return new ResultVO<T>()
                .setStatus(status)
                .setMessage(message);
    }

    public static <T> ResultVO<T> error(Integer status,String message, T data){
        return new ResultVO<T>()
                .setStatus(status)
                .setMessage(message)
                .setData(data);
    }

    /**
     * 失败
     * @param resultEnum 返回枚举类
     * @return 统一返回类
     */
    public static <T> ResultVO<T> error(ResultEnum resultEnum){
        return error(resultEnum.getStatus(),resultEnum.getMessage());
    }






}
