package com.crowoj.api.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author crow
 * @create 2022/3/19 15:54
 * @description
 */
@Data
public class RegisterCompetitionParam {
    @NotNull
    private Long cid;
    private String pwd;
}
