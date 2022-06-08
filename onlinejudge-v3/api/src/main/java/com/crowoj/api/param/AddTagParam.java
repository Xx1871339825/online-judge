package com.crowoj.api.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author crow
 * @create 2022/3/12 15:24
 * @description
 */
@Data
public class AddTagParam {
    @NotBlank
    @Size(min = 1,max = 10)
    private String title;
    @NotNull
    private List<Long> pidArr;
}
