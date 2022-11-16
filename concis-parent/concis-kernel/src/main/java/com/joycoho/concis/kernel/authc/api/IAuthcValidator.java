package com.joycoho.concis.kernel.authc.api;

/**
 * 定义了用户鉴权的方法接口
 */
public interface IAuthcValidator {
    /**
     * 检查用户输入的密码是否匹配，用于登录时比对密码
     * @param username 用户输入的账号
     * @param passwordEnc 用户输入的密码(已加密)
     * @return
     */
    boolean checkPassword(String username, String passwordEnc);

    /**
     * 判断当前用户的登录状态
     * @param token 用户凭证
     * @return
     */
    boolean isUserLogin(String token);

    /**
     * 判断当前用户是否具备资源访问的角色
     * @param role 访问该所需要的角色名称
     * @return
     */
    boolean hasRole(String role);

    /**
     * 判断当前用户是否具备资源访问的角色
     * @param roles 访问该所需要的角色名称List
     * @return
     */
    boolean hasRoles(String[] roles);
}
