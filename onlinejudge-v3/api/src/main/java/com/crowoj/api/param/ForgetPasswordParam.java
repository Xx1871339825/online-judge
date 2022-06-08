package com.crowoj.api.param;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author crow
 * @create 2022/3/22 13:55
 * @description
 */
@Data
@Accessors(chain = true)
public class ForgetPasswordParam {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String emailValid;
    @NotBlank
    private String password;
}
