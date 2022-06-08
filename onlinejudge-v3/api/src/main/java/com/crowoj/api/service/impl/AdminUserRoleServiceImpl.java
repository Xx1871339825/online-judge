package com.crowoj.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.api.dao.AdminUserRoleDao;
import com.crowoj.api.entity.AdminUserRole;
import com.crowoj.api.service.AdminUserRoleService;
import org.springframework.stereotype.Service;

/**
 * (AdminUserRole)表服务实现类
 *
 * @author crow
 * @since 2022-03-08 14:28:21
 */
@Service("adminUserRoleService")
public class AdminUserRoleServiceImpl extends ServiceImpl<AdminUserRoleDao, AdminUserRole> implements AdminUserRoleService {

}

