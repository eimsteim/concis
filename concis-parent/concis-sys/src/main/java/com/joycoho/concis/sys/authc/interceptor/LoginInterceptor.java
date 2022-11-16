package com.joycoho.concis.sys.authc.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joycoho.concis.kernel.authc.api.IAuthcValidator;
import com.joycoho.concis.kernel.cache.ehcache.EhCacheUtil;
import com.joycoho.concis.kernel.context.BaseResponseStatus;
import com.joycoho.concis.kernel.context.SpringContextHolder;
import com.joycoho.concis.kernel.context.RtnInfo;
import com.joycoho.concis.sys.context.ResponseStatus;
import com.joycoho.concis.sys.context.UserContext;
import com.joycoho.concis.sys.modular.user.entity.User;
import com.joycoho.concis.sys.modular.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname LoginInterceptor
 * @Description 登录校验拦截器
 * @Version 1.0.0
 * @Date 2022/10/5 23:32
 * @Created by Leo
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    /***
     * 在请求处理之前进行调用(Controller方法调用之前)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("执行了LoginInterceptor拦截器的preHandle方法");
        //只有通过这种方式才能拿到 authcValidator 的实例
        IAuthcValidator authcValidator = SpringContextHolder.getBean("authcValidator");
        IUserService userService = SpringContextHolder.getBean("userService");
        try {
            String token = request.getHeader("Authorization");
            //统一拦截
            boolean isLogin = authcValidator.isUserLogin(token);
            if (isLogin) {
                //将当前请求的用户信息存放在上下文中
                String username = EhCacheUtil.get("loginCache", token);
                User user = userService.selectByAccount(username);
                UserContext.putUser(user);
                //TODO 对Cache进行续期，但是在这里续期会不会太频繁了？毕竟无法通过ehcache获取对象的过期时间
                //renew("loginCache", token);
                return true;
            }
            //response.sendRedirect(request.getContextPath() + "login");
            returnError(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
        //如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
        //如果设置为true时，请求将会继续执行后面的操作
    }

    /***
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("执行了LoginInterceptor拦截器的postHandle方法");
    }

    /***
     * 整个请求结束之后被调用，也就是在DispatchServlet渲染了对应的视图之后执行（主要用于进行资源清理工作）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("执行了LoginInterceptor拦截器的afterCompletion方法");
    }

    public void returnError(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        RtnInfo resultBody = RtnInfo.error(ResponseStatus.NOT_LOGIN);
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().println(objectMapper.writeValueAsString(resultBody));
        return;
    }
}
