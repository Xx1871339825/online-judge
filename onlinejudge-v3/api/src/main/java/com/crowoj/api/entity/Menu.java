package com.crowoj.api.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.crowoj.api.core.enums.BanFlag;
import com.crowoj.api.core.enums.MenuType;
import lombok.*;
import lombok.experimental.Accessors;
/**
 * (Menu)表实体类
 *
 * @author crow
 * @since 2022-03-08 14:28:14
 */
@Data
@Accessors(chain = true)   
public class Menu{
    //菜单id
    private Long mid;
    //菜单名称
    private String menuName;
    //菜单权限标识
    private String permissions;
    //前端跳转url
    private String menuPath;
    //组件名称
    private String component;
    //父菜单id
    private Long parentId;
    //菜单类型
    private MenuType menuType;
    //创建时间
    private Timestamp createTime;
    //更新时间
    private Timestamp updateTime;
    //图标（element-plus）
    private String icon;
    //状态，0=可用，1=不可用
    private BanFlag status;

    @TableField(exist = false)
    private List<Menu> children = new ArrayList<>();
}
