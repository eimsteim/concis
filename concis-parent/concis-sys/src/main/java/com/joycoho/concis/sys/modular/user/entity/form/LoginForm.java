package com.joycoho.concis.sys.modular.user.entity.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname LoginForm
 * @Description 登录表单参数
 * @Version 1.0.0
 * @Date 2022/10/5 22:05
 * @Created by Leo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginForm {
    /**
     * 登录名
     */
    private String username;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 登录类型
     */
    private String type;
    /**
     * 是否自动登录
     */
    private Boolean autoLogin;
}
