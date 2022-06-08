package com.crowoj.api.dto;

import com.crowoj.api.entity.UserSubmit;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author crow
 * @create 2022/3/2 0:27
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubmitInfoDTO extends UserSubmit {
    private String nickname;
}
