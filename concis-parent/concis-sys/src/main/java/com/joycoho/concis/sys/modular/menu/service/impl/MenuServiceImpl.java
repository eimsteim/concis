package com.joycoho.concis.sys.modular.menu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joycoho.concis.sys.modular.menu.dao.MenuMapper;
import com.joycoho.concis.sys.modular.menu.entity.Menu;
import com.joycoho.concis.sys.modular.menu.entity.MenuNode;
import com.joycoho.concis.sys.modular.menu.service.IMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 */
@Slf4j
@Service
@Transactional
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Autowired
    MenuMapper menuMapper;

    @Override
    @Transactional
    public void delMenu(Integer menuId) {

        //删除菜单
        this.menuMapper.deleteById(menuId);

        //删除关联的relation
        this.menuMapper.deleteRelationByMenu(menuId);
    }
    @Override
    @Transactional
    public void delMenuContainSubMenus(Integer menuId) {
        Menu menu = menuMapper.selectById(menuId);

        //删除当前菜单
        delMenu(menuId);

        //删除所有子菜单
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper = wrapper.like("pcodes", "%[" + menu.getCode() + "]%");
        List<Menu> menus = menuMapper.selectList(wrapper);
        for (Menu temp : menus) {
            delMenu(temp.getId());
        }
    }
    @Override
    public List<Map<String, Object>> selectMenus(String condition, String level) {
        return menuMapper.selectMenus(condition, level);
    }
    @Override
    public List<Long> getMenuIdsByRoleId(Integer roleId) {
        return menuMapper.getMenuIdsByRoleId(roleId);
    }

    @Override
    public int deleteRelationByMenu(Integer menuId) {
        return menuMapper.deleteRelationByMenu(menuId);
    }
    @Override
    public List<String> getResUrlsByRoleId(Integer roleId) {
        return menuMapper.getResUrlsByRoleId(roleId);
    }

    @Override
    public List<MenuNode> getAllMenusByTree(boolean onlyMenu) {
        List<MenuNode> menus = this.baseMapper.getAllMenus();
        //将menuNodes转换为树形结构
        List<MenuNode> titles = MenuNode.buildTitle(menus);
        return titles;
    }
    @Override
    public List<MenuNode> getMenusByRoleIds(List<Integer> roleIds, boolean onlyMenu) {
        Integer ismenu = onlyMenu ? 1 : null;
        List<MenuNode> menus = menuMapper.getMenusByRoleIds(roleIds, ismenu);
        //将menuNodes转换为树形结构
        List<MenuNode> titles = MenuNode.buildTitle(menus);
        return titles;
    }

    @Override
    public List<String> getMenuIdsByRoleIds(List<Integer> roleIds, boolean onlyMenu) {
        Integer ismenu = onlyMenu ? 1 : null;
        List<MenuNode> menus = menuMapper.getMenusByRoleIds(roleIds, ismenu);
        //提取菜单id
        List<String> menuIds = menus.stream().map(o -> String.valueOf(o.getId())).collect(Collectors.toList());
        return menuIds;
    }
}
