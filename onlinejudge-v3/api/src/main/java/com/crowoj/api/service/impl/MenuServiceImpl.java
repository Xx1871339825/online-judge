package com.crowoj.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crowoj.api.core.enums.MenuType;
import com.crowoj.api.dao.MenuDao;
import com.crowoj.api.entity.Menu;
import com.crowoj.api.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * (Menu)表服务实现类
 *
 * @author crow
 * @since 2022-03-08 14:28:16
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuDao, Menu> implements MenuService {

    @Override
    public List<Menu> findMenuByUid(Long uid) {
        var list = getBaseMapper().findMenuByUid(uid);
        return list == null? new ArrayList<>():list;
    }

    private List<Menu> getTree(List<Menu> list){
        var root = new ArrayList<Menu>();
        var map = new HashMap<String,Menu>();
        list.forEach(tree -> map.put(tree.getMid()+"",tree));
        list.forEach(menu -> {
            var m = map.get(menu.getParentId()+"");
            if (m != null&&menu.getParentId() != 0){
                var parent = map.get(menu.getParentId()+"");
                parent.getChildren().add(menu);
            }else {
                root.add(map.get(menu.getMid()+""));
            }
        });
        return root;
    }

    @Override
    public List<Menu> findMenuByRid(Long rid) {
        var list = getBaseMapper().findMenuByRid(rid);
        return list == null ? new ArrayList<>():list;
    }
}

