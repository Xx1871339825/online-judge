package com.crowoj.api.entity.primarykey;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author crow
 * @create 2021/10/19 0:15
 * @description 用户角色表主键
 */
@Data
@Accessors(chain = true)
public class AdminUserRolePK implements Serializable {
    private Long adminId;
    private Long rid;
}
