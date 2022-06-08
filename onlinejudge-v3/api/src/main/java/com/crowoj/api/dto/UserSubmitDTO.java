package com.crowoj.api.dto;

import com.crowoj.api.entity.UserSubmit;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author crow
 * @create 2022/2/28 18:21
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserSubmitDTO extends UserSubmit {
    private String nickname;
}
