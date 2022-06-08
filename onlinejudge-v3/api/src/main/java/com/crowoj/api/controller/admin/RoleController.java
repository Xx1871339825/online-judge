package com.crowoj.api.controller.admin;

import com.crowoj.api.core.enums.ResultEnum;
import com.crowoj.api.core.exception.SimpleException;
import com.crowoj.api.core.utils.ResultUtil;
import com.crowoj.api.entity.Role;
import com.crowoj.api.param.UpdateRoleMenuParam;
import com.crowoj.api.param.UpdateUserRoleParam;
import com.crowoj.api.service.RoleService;
import com.crowoj.api.vo.ResultVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author crow
 * @create 2022/3/14 14:54
 * @description
 */
@RestController("adminRoleController")
@RequestMapping("admin/role")
public class RoleController {
    @Resource
    private RoleService roleService;

    @GetMapping("list/{uid}")
    public ResultVO<List<Role>> getRoleListByUid(@PathVariable Long uid){
        return ResultUtil.success(roleService.findRoleByUid(uid));
    }

    @GetMapping("list")
    public ResultVO<List<Role>> getAllRoleList(){
        return ResultUtil.success(roleService.list());
    }

    @RequiresPermissions({"sys:user:addRole"})
    @PostMapping("update-user-role")
    public ResultVO<Boolean> updateUserRole(@RequestBody @Valid UpdateUserRoleParam param){
        if (param.getRidArr() != null){
            param.getRidArr().forEach(item -> {
                if (item==1){
                    throw new SimpleException(40100,"神的权限可不能乱给");
                }
            });
        }
        return ResultUtil.success(roleService.updateUserRole(param));
    }


    @RequiresPermissions({"sys:role:update"})
    @PostMapping("save-or-update-role")
    public ResultVO<Boolean> saveOrUpdateRole(@RequestBody Role role){
        if (role.getRoleName().equals("超级管理员")){
            throw new SimpleException(40100,"神的权限可不能乱动");
        }
        if (role.getRid() == -1){
            role.setRid(null);
        }
        try {
            return ResultUtil.success(roleService.saveOrUpdate(role));
        }catch (Exception e){
            throw new SimpleException(ResultEnum.CONFLICT_USER_ALREADY_EXISTS.getStatus(),"角色标识不能重复");
        }

    }

    @RequiresPermissions({"sys:role:update"})
    @PostMapping("update-role-menu")
    public ResultVO<Boolean> updateRoleMenu(@RequestBody @Valid UpdateRoleMenuParam param){
        if (param.getRid() == 1){
            throw new SimpleException(40100,"神的地位不可撼动");
        }
        return ResultUtil.success(roleService.updateRoleMenu(param));
    }

    @RequiresPermissions({"sys:role:delete"})
    @DeleteMapping("delete/{rid}")
    public ResultVO<Boolean> deleteRole(@PathVariable Long rid){
        var role = roleService.getById(rid);
        if(role.getRoleName().equals("超级管理员"))
            throw new SimpleException(40100,"神的地位不可撼动");
        return ResultUtil.success(roleService.deleteByRid(rid));
    }
}
