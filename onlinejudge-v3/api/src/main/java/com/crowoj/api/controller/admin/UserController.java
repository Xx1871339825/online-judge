package com.crowoj.api.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crowoj.api.core.utils.ResultUtil;
import com.crowoj.api.dto.UserAllInfoDTO;
import com.crowoj.api.entity.Role;
import com.crowoj.api.param.BanUserParam;
import com.crowoj.api.service.UserService;
import com.crowoj.api.vo.ResultVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author crow
 * @create 2022/3/13 20:11
 * @description
 */
@RestController("adminUserController")
@RequestMapping("admin/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequiresPermissions({"sys:user:list"})
    @GetMapping("list")
    public ResultVO<Page<UserAllInfoDTO>> search(String search,Integer banFlag, Page<UserAllInfoDTO> page){
        System.err.println("banFlag:="+banFlag);
        return ResultUtil.success(userService.getUserAllInfoDTO(search,banFlag,page));
    }

    @RequiresPermissions({"sys:user:update"})
    @PutMapping("ban-user")
    public ResultVO<Boolean> banUser(@RequestBody @Valid BanUserParam param){
        return ResultUtil.success(userService.banUser(param.getUid()));
    }


}
