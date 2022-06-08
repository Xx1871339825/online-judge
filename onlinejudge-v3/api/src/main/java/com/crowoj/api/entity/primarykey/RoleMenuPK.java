package com.crowoj.api.entity.primarykey;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author crow
 * @create 2021/10/19 0:15
 * @description 角色菜单表主键
 */
@Data
@Accessors(chain = true)
public class RoleMenuPK implements Serializable {
    private Long rid;
    private Long mid;
}
