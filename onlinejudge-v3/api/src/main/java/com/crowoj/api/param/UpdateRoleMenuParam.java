package com.crowoj.api.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author crow
 * @create 2022/3/15 16:59
 * @description
 */
@Data
public class UpdateRoleMenuParam {
    @NotNull
    private Long rid;
    @NotNull
    private List<Long> midArr;
}
