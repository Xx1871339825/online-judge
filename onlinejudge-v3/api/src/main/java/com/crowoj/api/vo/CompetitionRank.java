package com.crowoj.api.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author crow
 * @create 2022/3/20 3:28
 * @description
 */
@Data
@Accessors(chain = true)
public class CompetitionRank {
    private Long rank;
    private Long uid;
    private String nickname;
    private Map<String,RankProblemStatus> submissionInfo;
    private Double totalScore;
}
