package com.crowoj.api.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crowoj.api.core.shiro.ActivityUser;
import com.crowoj.api.core.utils.ResultUtil;
import com.crowoj.api.entity.Setting;
import com.crowoj.api.service.SettingService;
import com.crowoj.api.vo.ResultVO;
import com.crowoj.api.vo.SettingVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.sql.Timestamp;

/**
 * @author crow
 * @create 2022/3/8 19:18
 * @description
 */
@RestController("adminSetting")
@RequestMapping("admin/setting")
public class SettingController {

    @Resource
    private SettingService settingService;


    @RequiresPermissions(value = {"sys:setting:list"})
    @GetMapping("list")
    public ResultVO<Page<SettingVO>> list(String search, Integer type,Page<Setting> page){
        return ResultUtil.success(settingService.getSettingList(search,type,page));
    }

    @RequiresPermissions(value = {"sys:setting:add"})
    @PostMapping("add")
    public ResultVO<Boolean> addSetting(@RequestBody @Valid Setting setting){
        var activeUser = (ActivityUser) SecurityUtils.getSubject().getPrincipal();
        setting.setCreateTime(new Timestamp(System.currentTimeMillis()))
                .setUserId(activeUser.getUser().getUid());
        if (setting.getSettingId() == -1){
            setting.setSettingId(null);
        }
        return ResultUtil.success(settingService.save(setting));
    }

    @RequiresPermissions(value = {"sys:setting:update"})
    @PostMapping("update")
    public ResultVO<Boolean> updateSetting(@RequestBody @Valid Setting setting){
        setting.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        return ResultUtil.success(settingService.updateById(setting));
    }

    @RequiresPermissions(value = {"sys:setting:delete"})
    @DeleteMapping("delete/{sid}")
    public ResultVO<Boolean> deleteSetting(@PathVariable Long sid){
        return ResultUtil.success(settingService.removeById(sid));
    }

    @GetMapping("{sid}")
    public ResultVO<Setting> getSetting(@PathVariable Long sid){
        return ResultUtil.success(settingService.getById(sid));
    }



}
