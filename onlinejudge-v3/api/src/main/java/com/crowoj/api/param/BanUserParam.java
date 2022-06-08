package com.crowoj.api.param;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author crow
 * @create 2022/4/6 1:16
 * @description
 */
@Data
@Accessors
public class BanUserParam {
    @NotNull
    private Long[] uid;
}
