package com.joycoho.concis.sys.modular.menu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joycoho.concis.sys.modular.menu.entity.Menu;
import com.joycoho.concis.sys.modular.menu.entity.MenuNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据条件查询菜单
     *
     * @return
     * @date 2017年2月12日 下午9:14:34
     */
    List<Map<String, Object>> selectMenus(@Param("condition") String condition, @Param("level") String level);

    /**
     * 根据条件查询菜单
     *
     * @return
     * @date 2017年2月12日 下午9:14:34
     */
    List<Long> getMenuIdsByRoleId(@Param("roleId") Integer roleId);

    /**
     * 删除menu关联的relation
     *
     * @param menuId
     * @return
     * @date 2017年2月19日 下午4:10:59
     */
    int deleteRelationByMenu(Integer menuId);

    /**
     * 获取资源url通过角色id
     *
     * @param roleId
     * @return
     * @date 2017年2月19日 下午7:12:38
     */
    List<String> getResUrlsByRoleId(Integer roleId);

    /**
     * 根据角色获取菜单
     *
     * @param roleIds
     * @return
     * @date 2017年2月19日 下午10:35:40
     */
    List<MenuNode> getMenusByRoleIds(@Param("list") List<Integer> roleIds, @Param("ismenu") Integer ismenu);

    List<MenuNode> getMenusByRoleIds(@Param("appCode") String appCode, @Param("list") List<Integer> roleIds, @Param("ismenu") Integer ismenu);

    /**
     * 查询某个角色包含的所有菜单ID
     * @param roleId
     * @return
     */
    List<Integer> selectMenuIdsByRoleId(int roleId);

    /**
     * 查询所有菜单
     * @return
     */
    List<MenuNode> getAllMenus();
}
