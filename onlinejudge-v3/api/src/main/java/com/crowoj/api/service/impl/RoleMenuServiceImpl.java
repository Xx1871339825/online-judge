package com.crowoj.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.api.dao.RoleMenuDao;
import com.crowoj.api.entity.RoleMenu;
import com.crowoj.api.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * (RoleMenu)表服务实现类
 *
 * @author crow
 * @since 2022-03-08 14:28:20
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuDao, RoleMenu> implements RoleMenuService {

}

