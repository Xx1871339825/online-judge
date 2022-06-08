package com.crowoj.api.entity;

import lombok.*;
import lombok.experimental.Accessors;
/**
 * (CompetitionProblem)表实体类
 *
 * @author crow
 * @since 2022-03-06 21:17:57
 */
@Data
@Accessors(chain = true)   
public class CompetitionProblem{
    //比赛id
    private Long cid;
    //题目id
    private Long pid;
}

