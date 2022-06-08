package com.crowoj.api.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author crow
 * @create 2022/3/12 19:34
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateTagParam extends AddTagParam {
    @NotNull
    private Long tid;
}
