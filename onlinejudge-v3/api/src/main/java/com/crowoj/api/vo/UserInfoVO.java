package com.crowoj.api.vo;

import com.crowoj.api.dto.UserInfoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author crow
 * @create 2022/3/3 18:29
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfoVO extends UserInfoDTO {
    private List<Long> pidList;
}
