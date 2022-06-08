package com.crowoj.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crowoj.api.entity.Role;
import com.crowoj.api.param.UpdateRoleMenuParam;
import com.crowoj.api.param.UpdateUserRoleParam;

import java.util.List;

/**
 * (Role)表服务接口
 *
 * @author crow
 * @since 2022-03-08 14:28:17
 */
public interface RoleService extends IService<Role> {
    List<Role> findRoleByUid(Long uid);

    Boolean updateUserRole(UpdateUserRoleParam param);

    Boolean updateRoleMenu(UpdateRoleMenuParam param);

    Boolean deleteByRid(Long rid);
}

