package com.crowoj.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crowoj.api.entity.AdminUser;
import org.apache.ibatis.annotations.Delete;

/**
 * (AdminUser)表数据库访问层
 *
 * @author crow
 * @since 2022-03-14 15:59:55
 */
public interface AdminUserDao extends BaseMapper<AdminUser> {

    @Delete("""
            DELETE au,aur
             FROM admin_user as au,admin_user_role AS aur
             WHERE au.admin_id = aur.admin_id
             AND au.uid=#{uid}
             AND aur.rid!=1
            """)
    int deleteAdminUserAndRoleByUid(Long uid);


    @Delete("""
            DELETE aur
             FROM admin_user as au,admin_user_role AS aur
             WHERE au.admin_id = aur.admin_id
             AND au.uid=#{uid}
             AND aur.rid!=1
            """)
    int deleteAdminUserRoleByUid(Long uid);


}

