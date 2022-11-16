package com.joycoho.concis.sys.modular.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joycoho.concis.sys.modular.role.entity.Role;
import com.joycoho.concis.sys.modular.role.entity.RoleNode;

import java.util.List;
import java.util.Map;

/**
 * @Classname RoleService
 * @Description 角色服务层
 * @Version 1.0.0
 * @Date 2022/10/22 20:24
 * @Created by Leo
 */
public interface IRoleService extends IService<Role> {

    /**
     * 设置某个角色的权限
     *
     * @param roleId        角色id
     * @param newMenuIds    新的权限ids
     * @date 2017年2月13日 下午8:26:53
     */
    void setAuthority(Integer roleId, List<Integer> newMenuIds);

    /**
     * 删除角色
     *
     * @author stylefeng
     * @Date 2017/5/5 22:24
     */
    void delRoleById(Integer roleId);

    /**
     * 根据条件查询角色列表
     *
     * @return
     * @date 2017年2月12日 下午9:14:34
     */
    List<Map<String, Object>> selectRoles(String condition);

    /**
     * 删除某个角色的所有权限
     *
     * @param roleId 角色id
     * @return
     * @date 2017年2月13日 下午7:57:51
     */
    int deleteRolesById(Integer roleId);

    /**
     * 获取角色列表树
     *
     * @return
     * @date 2017年2月18日 上午10:32:04
     */
    List<RoleNode> listTree();

}
