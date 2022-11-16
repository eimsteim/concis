package com.joycoho.concis.sys.modular.role.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.joycoho.concis.kernel.authc.annotation.PermittedRole;
import com.joycoho.concis.kernel.context.RtnInfo;
import com.joycoho.concis.kernel.exception.ServiceException;
import com.joycoho.concis.sys.constant.Const;
import com.joycoho.concis.sys.context.ResponseStatus;
import com.joycoho.concis.sys.modular.menu.entity.Relation;
import com.joycoho.concis.sys.modular.role.entity.Role;
import com.joycoho.concis.sys.modular.role.entity.RoleNode;
import com.joycoho.concis.sys.modular.role.entity.form.SetAuthorityForm;
import com.joycoho.concis.sys.modular.role.service.IRoleService;
import com.joycoho.concis.sys.modular.user.entity.User;
import com.joycoho.concis.sys.modular.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname RoleController
 * @Description 系统角色控制层
 * @Version 1.0.0
 * @Date 2022/11/3 01:32
 * @Created by Leo
 */
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    /**
     * 新增角色
     */
    @RequestMapping(value = "/add")
    //@PermittedRole(Const.ADMIN_NAME)
    public Object add(@RequestBody Role role) {
        if (StringUtils.isBlank(role.getName()) || StringUtils.isBlank(role.getCode())) {
            throw new ServiceException(ResponseStatus.MISSING_PARAMETERS);
        }
        //判断是否有重复的菜单编码
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", role.getCode());
        List<Role> list = role.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            throw new ServiceException(ResponseStatus.EXISTED_DUNPLICATE_CODE);
        }
        //防止前端传入错误的ID
        role.setId(null);
        //填充levels
        if (role.getPid() == null) {
            role.setPid(0);
            role.setLevels(1);
        } else {
            Role parent = role.selectById(role.getPid());
            role.setLevels(parent.getLevels() + 1);//在父级的levels上增加1
        }
        role.insert();
        return RtnInfo.SUCCESS;
    }

    /**
     * 角色修改
     */
    @RequestMapping(value = "/update")
    //@PermittedRole(Const.ADMIN_NAME)
    public Object update(@RequestBody Role role) {
        if (role.getId() == null || StringUtils.isBlank(role.getName()) || StringUtils.isBlank(role.getCode())) {
            throw new ServiceException(ResponseStatus.MISSING_PARAMETERS);
        }
        //判断是否pid是否和自己的id相同
        if (role.getId() == role.getPid()) {
            throw new ServiceException(ResponseStatus.MENU_PCODE_COINCIDENCE);
        }
        //判断是否有重复的菜单编码
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", role.getCode()).ne("id", role.getId());
        List<Role> list = role.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            throw new ServiceException(ResponseStatus.EXISTED_DUNPLICATE_CODE);
        }
        //填充levels
        if (role.getPid() == null) {
            role.setPid(0);
            role.setLevels(1);
        } else {
            //Find parent
            Role parent = role.selectById(role.getPid());
            role.setLevels(parent.getLevels() + 1);//在父级的levels上增加1
        }
        //Do update
        role.updateById();
        return RtnInfo.SUCCESS;
    }

    /**
     * 批量删除用户（物理删除）
     * @return
     */
    @RequestMapping("/remove")
    public Object remove(@RequestBody List<Integer> selected) {
        //防御
        if (CollectionUtil.isEmpty(selected)) {
            throw new ServiceException(ResponseStatus.MISSING_PARAMETERS);
        }
        //不能删除超级管理员角色
        if (selected.contains(Const.ADMIN_ROLE_ID)) {
            throw new ServiceException(ResponseStatus.CANT_DELETE_ADMIN);
        }
        //批量删
        roleService.removeBatchByIds(selected);
        return RtnInfo.SUCCESS;
    }

    /**
     * 配置权限
     */
    @RequestMapping("/setAuthority")
    //@PermittedRole(Const.ADMIN_NAME)
    public Object setAuthority(@RequestBody SetAuthorityForm params) {
        if (params == null || params.getRoleId() == null || CollectionUtil.isEmpty(params.getMenuIds())) {
            throw new ServiceException(ResponseStatus.MISSING_PARAMETERS);
        }
        this.roleService.setAuthority(params.getRoleId(), params.getMenuIds());
        return RtnInfo.SUCCESS;
    }

    /**
     * 获取角色列表
     */
    @RequestMapping(value = "/listTree")
    public Object listTree() {
        List<RoleNode> roleTreeList = roleService.listTree();
        return RtnInfo.success(roleTreeList);
    }

    /**
     * 获取某个用户的所有角色列表
     */
    @RequestMapping(value = "/listRoleIdsByUserId")
    public Object listRoleIdsByUserId(Integer userId) {
        if(userId == null) {
            throw new ServiceException(ResponseStatus.MISSING_PARAMETERS);
        }
        User theUser = userService.getById(userId);
        String roleId = theUser.getRoleId();//roleId就是以逗号分割的所有角色ID了
        if (StringUtils.isBlank(roleId)) {
            return RtnInfo.success(new ArrayList());
        } else {
            String[] strArray = roleId.split(",");
            return RtnInfo.success(strArray);
        }
    }
}
