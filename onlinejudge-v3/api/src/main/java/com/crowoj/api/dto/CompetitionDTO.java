package com.crowoj.api.dto;

import com.crowoj.api.entity.Competition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author crow
 * @create 2022/3/18 19:20
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CompetitionDTO extends Competition {
    private String nickname;
}
