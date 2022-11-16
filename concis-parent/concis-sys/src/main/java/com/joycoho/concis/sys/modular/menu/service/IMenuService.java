package com.joycoho.concis.sys.modular.menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joycoho.concis.sys.modular.menu.entity.Menu;
import com.joycoho.concis.sys.modular.menu.entity.MenuNode;

import java.util.List;
import java.util.Map;

/**
 * @Classname IMenuService
 * @Description 菜单服务层
 * @Version 1.0.0
 * @Date 2022/10/22 20:24
 * @Created by Leo
 */
public interface IMenuService extends IService<Menu> {
    void delMenu(Integer menuId);

    void delMenuContainSubMenus(Integer menuId);

    List<Map<String, Object>> selectMenus(String condition, String level);

    List<Long> getMenuIdsByRoleId(Integer roleId);

    int deleteRelationByMenu(Integer menuId);

    List<String> getResUrlsByRoleId(Integer roleId);

    /**
     * 不根据角色ID限定查询结果
     * @param onlyMenu
     * @return
     */
    List<MenuNode> getAllMenusByTree(boolean onlyMenu);

    /**
     * 根据角色集查询所有资源
     * @param roleIds  角色ID集
     * @param onlyMenu 仅查询菜单（不查询功能型资源）
     * @return
     */
    List<MenuNode> getMenusByRoleIds(List<Integer> roleIds, boolean onlyMenu);

    List<String> getMenuIdsByRoleIds(List<Integer> roleIds, boolean onlyMenu);
}
