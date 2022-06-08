package com.crowoj.api.core.shiro;

import com.crowoj.api.entity.Menu;
import com.crowoj.api.entity.Role;
import com.crowoj.api.entity.User;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

/**
 * @author crow
 * @create 2022/2/21 18:37
 * @description
 */
@Data
@Accessors(chain = true)
public class ActivityUser{
    /**
     * 当前用户对象
     */
    private User user;
    /**
     * 当前用户具有的角色
     */
    private List<Role> roles;
    /**
     * 当前用户具有的url
     */
    private Set<String> urls;

    /**
     * 包括url+permission
     */
    private List<Menu> menus;
    /**
     * 当前用户具有的权限API:例如[user:add],[user:delete]...
     */
    private Set<String> permissions;


}
