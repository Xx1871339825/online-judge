package com.crowoj.api.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author crow
 * @create 2022/2/22 21:43
 * @description
 */
@Data
@Accessors(chain = true)
public class SubmitVO {
    private Integer statusNo;
    private String status;
}
