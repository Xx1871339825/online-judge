package com.crowoj.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crowoj.api.entity.Menu;

import java.util.List;

/**
 * (Menu)表数据库访问层
 *
 * @author crow
 * @since 2022-03-08 14:28:13
 */
public interface MenuDao extends BaseMapper<Menu> {
    List<Menu> findMenuByUid(Long uid);
    List<Menu> findMenuByRid(Long rid);
}

