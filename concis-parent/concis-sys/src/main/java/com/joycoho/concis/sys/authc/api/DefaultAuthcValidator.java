package com.joycoho.concis.sys.authc.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.joycoho.concis.kernel.authc.api.IAuthcValidator;
import com.joycoho.concis.kernel.authc.third.shiro.ShiroKit;
import com.joycoho.concis.kernel.cache.ehcache.EhCacheUtil;
import com.joycoho.concis.kernel.context.SpringContextHolder;
import com.joycoho.concis.sys.modular.user.constant.UserStatus;
import com.joycoho.concis.sys.modular.user.dao.UserMapper;
import com.joycoho.concis.sys.modular.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Base64;

/**
 * 默认的鉴权控制器
 * 如果用户没有在配置文件中通过concis.authc-validator声明自定义鉴权控制器，则默认初始化这个
 */
@Slf4j
public class DefaultAuthcValidator implements IAuthcValidator {

    UserMapper userMapper;

    public DefaultAuthcValidator() {
        this.userMapper = SpringContextHolder.getBean("userMapper");
    }


    /**
     * 检查用户输入的密码是否匹配，用于登录时比对密码
     * @param username 用户输入的账号
     * @param passwordEnc 用户输入的密码(base64加密)
     * @return
     */
    @Override
    public boolean checkPassword(String username, String passwordEnc) {
        String password = new String(Base64.getDecoder().decode(passwordEnc));
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("account", username);
        userWrapper.eq("status", UserStatus.ACTIVE.code());

        User user = userMapper.selectOne(userWrapper);
        if (user == null) {
            return false;
        }
        //两种MD5对比
        String inputPasswordEnc = ShiroKit.md5(password, user.getSalt());
        if (user.getPassword().equals(inputPasswordEnc)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isUserLogin(String token) {
        //如果缓存中存在token，说明其是生效的
        String username = EhCacheUtil.get("loginCache", token);
        //如果username是空，说明作为key的token是不存在的
        if (StringUtils.isBlank(username)) {
            return false;
        }
        //否则，说明token是存在的
        return true;
    }

    @Override
    public boolean hasRole(String role) {
        log.info(":::DefaultAuthcValidator.hasRole");
        return false;
    }

    @Override
    public boolean hasRoles(String[] roles) {
        log.info(":::DefaultAuthcValidator.hasRoles");
        return false;
    }
}
