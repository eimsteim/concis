///**
// * Copyright 2022-2025 charlie.zhang (https://github.com/eimsteim/concis)
// * <p>
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * <p>
// * http://www.apache.org/licenses/LICENSE-2.0
// * <p>
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.joycoho.concis.kernel.authc.third.shiro;
//
//import cn.hutool.core.util.StrUtil;
//import com.onefanr.recharge.core.shiro.service.UserAuthService;
//import com.onefanr.recharge.core.shiro.service.impl.UserAuthServiceServiceImpl;
//import com.onefanr.recharge.modular.system.model.User;
//import org.apache.shiro.authc.*;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.authz.SimpleAuthorizationInfo;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.subject.PrincipalCollection;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//public class ShiroDbRealm extends AuthorizingRealm {
//
//    /**
//     * 进行登录认证
//     */
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
//            throws AuthenticationException {
//        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
//        UserAuthService shiroFactory = UserAuthServiceServiceImpl.me();
//        User user = shiroFactory.user(token.getUsername());
//        ShiroUser shiroUser = shiroFactory.shiroUser(user);
//        //todo get user input password
//        String inputPassword = new String(token.getPassword());
//        //重置token中的password,替换为经过MD5加密后的值，
//        String salt = user.getSalt();
//        String inputPasswordEnc = ShiroKit.md5(inputPassword, salt);
//        token.setPassword(inputPasswordEnc.toCharArray());
//        //用于在ShiroCredentialsMatcher中与数据库中查到的密码进行对比
//        return shiroFactory.info(shiroUser, user, super.getName());
//    }
//
//    /**
//     * 进行角色权限和对应权限的添加
//     */
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        UserAuthService shiroFactory = UserAuthServiceServiceImpl.me();
//        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
//        List<Integer> roleList = shiroUser.getRoleList();
//
//        Set<String> permissionSet = new HashSet<>();
//        Set<String> roleNameSet = new HashSet<>();
//
//        for (Integer roleId : roleList) {
//            List<String> permissions = shiroFactory.findPermissionsByRoleId(roleId);
//            if (permissions != null) {
//                for (String permission : permissions) {
//                    if (StrUtil.isNotBlank(permission)) {
//                        permissionSet.add(permission);
//                    }
//                }
//            }
//            String roleName = shiroFactory.findRoleNameByRoleId(roleId);
//            roleNameSet.add(roleName);
//        }
//
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        info.addStringPermissions(permissionSet);
//        info.addRoles(roleNameSet);
//        return info;
//    }
//
//    /**
//     * 设置认证加密方式
//     */
////    @Override
////    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
////        HashedCredentialsMatcher md5CredentialsMatcher = new HashedCredentialsMatcher();
////        md5CredentialsMatcher.setHashAlgorithmName(ShiroKit.hashAlgorithmName);
////        md5CredentialsMatcher.setHashIterations(ShiroKit.hashIterations);
////        super.setCredentialsMatcher(md5CredentialsMatcher);
////    }
//}
