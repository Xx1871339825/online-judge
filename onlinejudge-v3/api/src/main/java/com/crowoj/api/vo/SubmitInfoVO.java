package com.crowoj.api.vo;

import com.crowoj.api.entity.JudgeCase;
import com.crowoj.api.entity.UserSubmit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author crow
 * @create 2022/3/1 18:56
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubmitInfoVO extends UserSubmit {
    private String nickname;
    private List<JudgeCaseVO> judgeCaseList = new ArrayList<>();
}
