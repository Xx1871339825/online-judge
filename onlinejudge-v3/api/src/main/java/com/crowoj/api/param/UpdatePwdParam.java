package com.crowoj.api.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author crow
 * @create 2022/3/4 18:11
 * @description
 */
@Data
public class UpdatePwdParam {
    @NotBlank
    private String newPwd;
    @NotBlank
    private String emailCode;
}
