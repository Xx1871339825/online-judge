package com.crowoj.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.crowoj.api.entity.Setting;
import com.crowoj.api.vo.SettingVO;

/**
 * @author crow
 * @create 2021/12/20 10:38
 * @description
 */
public interface SettingDao extends BaseMapper<Setting> {

    Page<SettingVO> getSettingList(String search, Integer type,Integer status, Page<Setting> page);




}
