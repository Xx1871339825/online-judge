package com.crowoj.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crowoj.api.core.enums.SettingType;
import com.crowoj.api.entity.Setting;
import com.crowoj.api.vo.SettingVO;

import java.util.List;

/**
 * @author crow
 * @create 2021/12/20 10:39
 * @description
 */
public interface SettingService extends IService<Setting> {
    List<Setting> findBySettingType(SettingType settingType);

    Page<SettingVO> getSettingList(String search, Integer type, Page<Setting> page);

    Page<SettingVO> getArticle(String search, Page<Setting> page);
}
