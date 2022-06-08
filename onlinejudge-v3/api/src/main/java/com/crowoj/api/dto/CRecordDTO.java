package com.crowoj.api.dto;

import com.crowoj.api.entity.CompetitionRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author crow
 * @create 2022/3/20 5:21
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CRecordDTO extends CompetitionRecord {
    private String nickname;
    private Double score;
}
