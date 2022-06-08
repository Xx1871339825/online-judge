package com.crowoj.api.param;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author crow
 * @create 2022/3/14 15:51
 * @description
 */
@Data
@Accessors(chain = true)
public class UpdateUserRoleParam {
    @NotNull
    private Long uid;
    @NotNull
    private List<Long> ridArr;
}
