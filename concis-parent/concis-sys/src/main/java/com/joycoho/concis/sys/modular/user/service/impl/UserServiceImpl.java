package com.joycoho.concis.sys.modular.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joycoho.concis.kernel.authc.api.IAuthcValidator;
import com.joycoho.concis.kernel.authc.jwt.JJwtUtils;
import com.joycoho.concis.kernel.cache.ehcache.EhCacheUtil;
import com.joycoho.concis.sys.context.ResponseStatus;
import com.joycoho.concis.kernel.context.RtnInfo;
import com.joycoho.concis.sys.modular.user.constant.UserStatus;
import com.joycoho.concis.sys.modular.user.dao.UserMapper;
import com.joycoho.concis.sys.modular.user.entity.User;
import com.joycoho.concis.sys.modular.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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
@Service("userService")
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService
{
    @Autowired
    IAuthcValidator authcValidator;

    /**
     * 根据账号查询
     * @param username
     * @return
     */
    @Override
    public User selectByAccount(String username) {
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("account", username);
        userWrapper.eq("status", UserStatus.ACTIVE.code());

        return this.baseMapper.selectOne(userWrapper);
    }

    @Override
    public void setAuthority(Integer userId, List<Integer> newRoleIds) {
        //TODO 用户绑定角色的逻辑：将角色ID拉平，以逗号分割，存入role_id字段，这样做的好处是：后续在查询用户权限时，不需要处理角色的父子关系
        String roleIdStr = newRoleIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        User user = User.builder().id(userId).roleId(roleIdStr).build();
        log.error("roleIdStr:::{}", roleIdStr);
        user.updateById();
    }

        /**
     * 登录接口服务
     * @param username
     * @param passwordEnc
     * @return
     */
    @Override
    public RtnInfo login(String username, String passwordEnc){

        boolean isOK = authcValidator.checkPassword(username, passwordEnc);
        if (!isOK) {
            //Return error message
            return RtnInfo.error(ResponseStatus.PWD_NOT_MATCH);
        }
        //Create JWT token
        String token = JJwtUtils.generateToken(username, passwordEnc);
        //Put token to cache
        EhCacheUtil.put("loginCache", token, username);
        //Return token to client
        return RtnInfo.success(token);
    }

    /**
     * 分页查询
     * @param page
     * @param user
     * @return
     */
    @Override
    public IPage<User> selectPages(IPage page, User user){
        Map<String, Object> condition = new HashMap<>();
        if (StringUtils.isNotBlank(user.getName())) {
            condition.put("name", user.getName());
        }
        if (user.getStatus() != null) {
            condition.put("status", user.getStatus());
        }
        return this.baseMapper.selectPages(page, condition);
    }


    /**
     * 退出登录
     * @param token
     */
    @Override
    public void logout(String token) {
        EhCacheUtil.remove("loginCache", token);
    }
}
