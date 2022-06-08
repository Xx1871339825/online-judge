package com.crowoj.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.api.dao.FavoriteDao;
import com.crowoj.api.entity.Favorite;
import com.crowoj.api.service.FavoriteService;
import org.springframework.stereotype.Service;

/**
 * (Favorite)表服务实现类
 *
 * @author crow
 * @since 2022-02-22 16:43:39
 */
@Service("favoriteService")
public class FavoriteServiceImpl extends ServiceImpl<FavoriteDao, Favorite> implements FavoriteService {

}

