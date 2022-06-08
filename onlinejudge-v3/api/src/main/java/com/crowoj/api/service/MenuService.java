package com.crowoj.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crowoj.api.entity.Menu;

import java.util.List;

/**
 * (Menu)表服务接口
 *
 * @author crow
 * @since 2022-03-08 14:28:15
 */
public interface MenuService extends IService<Menu> {
    List<Menu> findMenuByUid(Long uid);
    List<Menu> findMenuByRid(Long rid);
}

