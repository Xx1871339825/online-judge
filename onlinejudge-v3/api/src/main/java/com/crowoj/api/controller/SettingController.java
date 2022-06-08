package com.crowoj.api.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crowoj.api.core.enums.SettingType;
import com.crowoj.api.core.utils.ResultUtil;
import com.crowoj.api.entity.Setting;
import com.crowoj.api.service.SettingService;
import com.crowoj.api.vo.ResultVO;
import com.crowoj.api.vo.SettingVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author crow
 * @create 2021/12/20 10:54
 * @description
 */
@RestController
@RequestMapping("api/setting")
public class SettingController {

    @Resource
    private SettingService settingService;

    @GetMapping("banner")
    public ResultVO<List<Setting>> getBannerList(){
        return ResultUtil.success(settingService.findBySettingType(SettingType.BANNER));
    }

    @GetMapping("article")
    public ResultVO<Page<SettingVO>> getArticle(String search, Page<Setting> page){
        return ResultUtil.success(settingService.getArticle(search,page));
    }
}
