package com.crowoj.api.param;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author crow
 * @create 2022/3/4 18:41
 * @description
 */
@Data
public class UpdateEmailParam {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String code;
}
