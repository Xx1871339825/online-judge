package com.crowoj.api.entity;

import lombok.*;
import lombok.experimental.Accessors;
/**
 * (CompetitionRegister)表实体类
 *
 * @author crow
 * @since 2022-03-06 21:17:57
 */
@Data
@Accessors(chain = true)   
public class CompetitionRegister{
    //比赛id
    private Long cid;
    //用户id
    private Long uid;
}

