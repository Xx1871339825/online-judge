package com.crowoj.api.controller.admin;

import com.crowoj.api.core.utils.ResultUtil;
import com.crowoj.api.entity.Menu;
import com.crowoj.api.service.MenuService;
import com.crowoj.api.vo.ResultVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author crow
 * @create 2022/3/15 15:02
 * @description
 */
@RestController("adminMenuController")
@RequestMapping("admin/menu")
public class MenuController {
    @Resource
    private MenuService menuService;

    @RequiresPermissions({"sys:role:update"})
    @GetMapping("rid/{rid}")
    public ResultVO<List<Menu>> getMenusByRid(@PathVariable Long rid){
        return ResultUtil.success(menuService.findMenuByRid(rid));
    }



}
