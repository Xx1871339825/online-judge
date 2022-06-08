package com.crowoj.api.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author crow
 * @create 2022/3/20 16:32
 * @description
 */
@Data
@Accessors(chain = true)
public class RankProblemStatus {
    private Integer status;// 0未提交过，1已提交，2提交且未通过
    private Double score;
}
