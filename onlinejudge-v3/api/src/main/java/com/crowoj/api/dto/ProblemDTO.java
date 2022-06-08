package com.crowoj.api.dto;

import com.crowoj.api.core.enums.ProblemType;
import com.crowoj.api.entity.Problem;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Objects;


/**
 * @author crow
 * @create 2022/2/22 16:14
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ProblemDTO extends Problem {
    private Integer isAc;
    private Integer isFavorite;
    private String tag;
    private String nickname;
}
