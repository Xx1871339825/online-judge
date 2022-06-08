package com.crowoj.api.param;

import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author crow
 * @create 2022/2/22 20:33
 * @description 提交参数验证
 */
@Data
public class SubmitParam {
    @NotNull
    @Min(1)
    private Long pid;
    @NotBlank
    private String language;
    @NotBlank
    private String code;
}
