package com.joycoho.concis.sys.context;

import com.joycoho.concis.sys.modular.user.entity.User;

/**
 * @Classname UserContext
 * @Description 通过ThreadLocal存储当前会话的用户信息
 * @Version 1.0.0
 * @Date 2022/10/23 00:10
 * @Created by Leo
 */
public class UserContext {

    private static ThreadLocal<User> shiroUserThreadLocal = new ThreadLocal<>();

    public static void putUser(User user){
        shiroUserThreadLocal.set(user);
    }

    public static User getUser(){
        return shiroUserThreadLocal.get();
    }

    public static void cleanUser(){
        shiroUserThreadLocal.remove();
    }
}
