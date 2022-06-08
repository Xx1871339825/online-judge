package com.crowoj.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.api.core.enums.BanFlag;
import com.crowoj.api.core.enums.SettingType;
import com.crowoj.api.entity.Setting;
import com.crowoj.api.dao.SettingDao;
import com.crowoj.api.service.SettingService;
import com.crowoj.api.vo.SettingVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author crow
 * @create 2021/12/20 10:39
 * @description
 */
@Service
public class SettingServiceImpl extends ServiceImpl<SettingDao,Setting> implements SettingService {

    @Override
    public List<Setting> findBySettingType(SettingType settingType) {
        var sQw = new QueryWrapper<Setting>();
        sQw.lambda().eq(Setting::getSettingType,settingType).eq(Setting::getStatus, BanFlag.NOT_BAN);
        return list(sQw);
    }

    @Override
    public Page<SettingVO> getSettingList(String search, Integer type, Page<Setting> page) {
        if (search == null) search = "";
        if (type != null &&type == -1) type = null;
        return getBaseMapper().getSettingList(search,type,-1,page);
    }

    @Override
    public Page<SettingVO> getArticle(String search, Page<Setting> page) {
        if (search == null) search = "";
        return getBaseMapper().getSettingList(search,SettingType.NOTICE.getValue(),0,page);
    }
}
