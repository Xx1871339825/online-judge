package com.crowoj.api.vo;

import com.crowoj.api.entity.JudgeCase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author crow
 * @create 2022/3/23 20:22
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class JudgeCaseVO extends JudgeCase {
    private String stdin;
    private String stdout;
}
