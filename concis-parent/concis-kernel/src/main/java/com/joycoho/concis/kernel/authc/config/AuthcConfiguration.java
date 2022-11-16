package com.joycoho.concis.kernel.authc.config;

import com.joycoho.concis.kernel.authc.api.IAuthcValidator;

public abstract class AuthcConfiguration {
    /**
     * 定义的用于返回鉴权类型的钩子方法
     * @return
     */
    public abstract IAuthcValidator authcValidator();
}
