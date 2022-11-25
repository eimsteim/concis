/**
 * Copyright 2022-2025 Leo (https://github.com/eimsteim/concis)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joycoho.concis.kernel.authc.aop;

import com.joycoho.concis.kernel.authc.annotation.PermittedRole;
import com.joycoho.concis.kernel.authc.api.IAuthcValidator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.naming.NoPermissionException;
import java.lang.reflect.Method;

/**
 * 权限检查的aop
 *
 * @author Leo
 * @date 2017-07-13 21:05
 */
@Aspect
@Component
@Order(200)
public class PermittedRoleAspect {

    @Autowired
    private IAuthcValidator authcValidator;

    @Pointcut(value = "@annotation(com.joycoho.concis.kernel.authc.annotation.PermittedRole)")
    private void cutPermittedRole() {

    }

    @Around("cutPermittedRole()")
    public Object authenticate(ProceedingJoinPoint point) throws Throwable {
        MethodSignature ms = (MethodSignature) point.getSignature();
        Method method = ms.getMethod();
        PermittedRole permission = method.getAnnotation(PermittedRole.class);
        String[] roles = permission.value();
        if (roles.length == 1) {
            //检查指定角色
            boolean result = authcValidator.hasRole(roles[0]);
            if (result) {
                return point.proceed();
            } else {
                throw new NoPermissionException();
            }
        } else {
            //检查全体角色
            boolean result = authcValidator.hasRoles(roles);
            if (result) {
                return point.proceed();
            } else {
                throw new NoPermissionException();
            }
        }
    }
}
