package com.crowoj.api.dto;

import com.crowoj.api.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author crow
 * @create 2022/3/13 20:51
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class UserAllInfoDTO extends User {
    private String email;
    private String account;
    private Long adminId;
    private String roleName;
    private String rid;
}
