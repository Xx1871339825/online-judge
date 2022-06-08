package com.crowoj.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crowoj.api.entity.Role;

import java.util.List;

/**
 * (Role)表数据库访问层
 *
 * @author crow
 * @since 2022-03-08 14:28:16
 */
public interface RoleDao extends BaseMapper<Role> {
    List<Role> findRoleByUid(Long uid);
}

