package com.joycoho.concis.sys.modular.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joycoho.concis.sys.modular.menu.dao.MenuMapper;
import com.joycoho.concis.sys.modular.menu.entity.Relation;
import com.joycoho.concis.sys.modular.role.dao.RoleMapper;
import com.joycoho.concis.sys.modular.role.entity.Role;
import com.joycoho.concis.sys.modular.role.entity.RoleNode;
import com.joycoho.concis.sys.modular.role.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Classname RoleServiceImpl
 * @Description 角色服务层实现
 * @Version 1.0.0
 * @Date 2022/11/3 01:29
 * @Created by Leo
 */
@Slf4j
@Service("roleService")
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    @Transactional
    public void setAuthority(Integer roleId, List<Integer> newMenuIds) {

        List<Integer> insertIds = new ArrayList<>();
        List<Integer> deleteIds = new ArrayList<>();
        //TODO 需要做性能优化
        //1.查询出该角色所拥有的权限
        List<Integer> oldMenuIds = this.menuMapper.selectMenuIdsByRoleId(roleId);
        //3.1 对比，找出old中没有的（新增）
        for (Integer a : newMenuIds) {
            if (!oldMenuIds.contains(a)) {
                insertIds.add(a);
            }
        }
        //3.2 和new中不存在的（删除）
        for (Integer d : oldMenuIds) {
            if (!newMenuIds.contains(d)){
                deleteIds.add(d);
            }
        }
        //4.将新增的插入
        for (Integer id : insertIds) {
            Relation relation = new Relation();
            relation.setRoleId(roleId);
            relation.setMenuId(id);
            relation.insert();
        }
        //5.将多余的删除
        Relation relation = new Relation();
        for (Integer id : deleteIds) {
            UpdateWrapper<Relation> wrapper = new UpdateWrapper<>();
            wrapper.eq("role_id", roleId).eq("menu_id", id);
            relation.delete(wrapper);
        }
    }

    @Override
    @Transactional
    public void delRoleById(Integer roleId) {
        //删除角色
        this.baseMapper.deleteById(roleId);

        // 删除该角色所有的权限
        this.baseMapper.deleteRolesById(roleId);
    }

    @Override
    public List<Map<String, Object>> selectRoles(String condition) {
        return this.baseMapper.selectRoles(condition);
    }

    @Override
    public int deleteRolesById(Integer roleId) {
        return this.baseMapper.deleteRolesById(roleId);
    }

    @Override
    public List<RoleNode> listTree() {
        List<RoleNode> list = this.baseMapper.listTree();
        //在查询出来的列表中，加入顶级节点
        //list.add(RoleNode.createParent());
        return RoleNode.buildTitle(list);
    }

}
