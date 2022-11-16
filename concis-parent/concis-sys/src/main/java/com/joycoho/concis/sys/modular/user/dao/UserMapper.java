package com.joycoho.concis.sys.modular.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.joycoho.concis.sys.modular.user.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface UserMapper extends BaseMapper<User> {

    /**
     * 修改用户状态
     */
    int setStatus(@Param("userId") Integer userId, @Param("status") int status);

    /**
     * 修改密码
     */
    int changePwd(@Param("userId") Integer userId, @Param("pwd") String pwd);

    /**
     * 设置用户的角色
     */
    int setRoles(@Param("userId") Integer userId, @Param("roleIds") String roleIds);

    /**
     * 通过账号获取用户
     */
    User getByAccount(@Param("account") String account);

    /**
     * 通过部门和账号获取用户
     */
    User getUserByDept(@Param("dept") String dept, @Param("account") String account);

    /**
     * 查找某个部门下的某个用户，递归钻取
     * @param deptid
     * @param account
     * @return
     */
    User findUserInDept(@Param("account") String account, @Param("deptid") Integer deptid);

    /**
     * 系统用户带条件分页查询
     * @param page
     * @param condition
     * @return
     */
    IPage<User> selectPages(IPage page, @Param("condition") Map<String, Object> condition);
}
