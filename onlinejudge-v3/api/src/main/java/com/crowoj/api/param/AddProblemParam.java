package com.crowoj.api.param;

import com.crowoj.api.entity.Problem;
import com.crowoj.api.entity.ProblemCase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author crow
 * @create 2022/3/13 13:58
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AddProblemParam extends Problem {

    @NotNull
    private List<ProblemCase> testCase;
}
