package com.crowoj.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.api.dao.AdminUserDao;
import com.crowoj.api.entity.AdminUser;
import com.crowoj.api.service.AdminUserService;
import org.springframework.stereotype.Service;

/**
 * (AdminUser)表服务实现类
 *
 * @author crow
 * @since 2022-03-14 16:00:02
 */
@Service("adminUserService")
public class AdminUserServiceImpl extends ServiceImpl<AdminUserDao, AdminUser> implements AdminUserService {

}

