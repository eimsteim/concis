package com.joycoho.concis.sys.application;

import com.joycoho.concis.kernel.context.RtnInfo;
import com.joycoho.concis.sys.modular.menu.entity.Menu;
import com.joycoho.concis.sys.modular.menu.service.IMenuService;
import com.joycoho.concis.sys.modular.menu.service.impl.MenuServiceImpl;
import com.joycoho.concis.sys.modular.role.entity.Role;
import com.joycoho.concis.sys.modular.role.service.IRoleService;
import com.joycoho.concis.sys.modular.user.entity.User;
import com.joycoho.concis.sys.modular.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Classname GrantUserApplication
 * @Description 用户授权相关业务
 * @Version 1.0.0
 * @Date 2022/10/22 19:56
 * @Created by Leo
 */
@Slf4j
@Service
public class GrantUserApplication {

    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IMenuService menuService;

    /**
     * 查询系统用户的菜单树
     * @param user
     * @return
     */
    public List<Menu> queryUserMenus(User user) {

        return null;
    }

    /**
     * 修改用户角色树
     * @param user
     * @param roles
     * @return
     */
    public RtnInfo updateUserRoles(User user, List<Role> roles) {

        return null;
    }

}
