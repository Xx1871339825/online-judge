package com.crowoj.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;
/**
 * (CompetitionRecord)表实体类
 *
 * @author crow
 * @since 2022-03-06 21:17:57
 */
@Data
@Accessors(chain = true)   
public class CompetitionRecord{
    @TableId(type = IdType.AUTO)
    private Long id;
    //比赛id
    private Long cid;
    //用户id
    private Long uid;
    //题目id
    private Long pid;
    //提交的Id,算分用
    private Long submitId;
    //提交结果，0表示为Ac通过，1表示Ac通过，-1未Ac通过算罚时
    private Integer status;
}

