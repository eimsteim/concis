package com.joycoho.concis.sys.modular.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.joycoho.concis.kernel.context.RtnInfo;
import com.joycoho.concis.sys.modular.user.entity.User;

import java.util.List;

/**
 * @Classname IUserService
 * @Description 系统用户服务接口
 * @Version 1.0.0
 * @Date 2022/10/31 20:48
 * @Created by Leo
 */
public interface IUserService extends IService<User> {
    RtnInfo login(String username, String password);

    IPage<User> selectPages(IPage page, User user);

    void logout(String token);

    User selectByAccount(String username);

    /**
     * 设置某个用户的角色
     *
     * @param userId        用户id
     * @param newRoleIds    新的角色ids
     * @date 2017年2月13日 下午8:26:53
     */
    void setAuthority(Integer userId, List<Integer> newRoleIds);
}
