package com.crowoj.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.api.dao.AdminUserDao;
import com.crowoj.api.dao.RoleDao;
import com.crowoj.api.entity.AdminUser;
import com.crowoj.api.entity.AdminUserRole;
import com.crowoj.api.entity.Role;
import com.crowoj.api.entity.RoleMenu;
import com.crowoj.api.param.UpdateRoleMenuParam;
import com.crowoj.api.param.UpdateUserRoleParam;
import com.crowoj.api.service.AdminUserRoleService;
import com.crowoj.api.service.AdminUserService;
import com.crowoj.api.service.RoleMenuService;
import com.crowoj.api.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * (Role)表服务实现类
 *
 * @author crow
 * @since 2022-03-08 14:28:18
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {
    @Resource
    private AdminUserDao adminUserDao;


    @Resource
    private AdminUserRoleService adminUserRoleService;

    @Override
    public List<Role> findRoleByUid(Long uid) {
        var list = getBaseMapper().findRoleByUid(uid);
        return list == null ? new ArrayList<>():list;
    }



    @Transactional
    @Override
    public Boolean updateUserRole(UpdateUserRoleParam param) {
        var list= param.getRidArr();
        if (list == null || list.size() == 0){
            //删除管理员身份以及拥有的所有角色（adminUser and adminUserRole ）
            adminUserDao.deleteAdminUserAndRoleByUid(param.getUid());
            return true;
        }else {
            //判断是否是管理员
            var aQw = new QueryWrapper<AdminUser>();
            aQw.lambda().eq(AdminUser::getUid,param.getUid());
            var row = adminUserDao.selectCount(aQw);
            if (row == 0){
                //添加为管理员
                 var admin = new AdminUser().setUid(param.getUid());
                 adminUserDao.insert(admin);
                 //添加权限
                var adminUserRoleList = new ArrayList<AdminUserRole>();
                list.forEach(i -> {
                    if (i != 1){
                        adminUserRoleList.add(new AdminUserRole().setAdminId(admin.getAdminId()).setRid(i));
                    }
                });
                return adminUserRoleService.saveBatch(adminUserRoleList);
            }

            //先删除所有角色
            adminUserDao.deleteAdminUserRoleByUid(param.getUid());
            //添加找出用户的adminId
            var qw = new QueryWrapper<AdminUser>();
            qw.lambda().eq(AdminUser::getUid,param.getUid());
            var adminId = adminUserDao.selectOne(qw).getAdminId();
            var adminUserRoleList = new ArrayList<AdminUserRole>();
            list.forEach(i -> {
                if (i != 1){
                    adminUserRoleList.add(new AdminUserRole().setAdminId(adminId).setRid(i));
                }
            });
            adminUserRoleService.saveBatch(adminUserRoleList);
            return true;
        }
    }

    @Resource
    private RoleMenuService roleMenuService;

    @Override
    public Boolean updateRoleMenu(UpdateRoleMenuParam param) {
        var qw = new QueryWrapper<RoleMenu>();
        qw.lambda().eq(RoleMenu::getRid,param.getRid());
        roleMenuService.remove(qw);
        var rmList = new ArrayList<RoleMenu>();
        param.getMidArr().forEach(i -> {
            rmList.add(new RoleMenu().setRid(param.getRid()).setMid(i));
        });
        roleMenuService.saveBatch(rmList);
        return true;
    }

    @Override
    public Boolean deleteByRid(Long rid) {
        var rmQw = new QueryWrapper<RoleMenu>();
        rmQw.lambda().eq(RoleMenu::getRid,rid);
        removeById(rid);
        roleMenuService.remove(rmQw);
        return true;
    }
}

