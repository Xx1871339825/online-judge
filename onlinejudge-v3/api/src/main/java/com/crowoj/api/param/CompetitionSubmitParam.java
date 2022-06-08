package com.crowoj.api.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author crow
 * @create 2022/3/19 5:18
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CompetitionSubmitParam extends SubmitParam {
    @NotNull
    private Long cid;
}
