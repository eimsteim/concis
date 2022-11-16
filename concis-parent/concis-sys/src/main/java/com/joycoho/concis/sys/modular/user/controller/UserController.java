package com.joycoho.concis.sys.modular.user.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joycoho.concis.kernel.authc.third.shiro.ShiroKit;
import com.joycoho.concis.kernel.context.PageFactory;
import com.joycoho.concis.kernel.exception.ServiceException;
import com.joycoho.concis.sys.context.ResponseStatus;
import com.joycoho.concis.kernel.context.RtnInfo;
import com.joycoho.concis.sys.context.UserContext;
import com.joycoho.concis.sys.modular.user.constant.UserStatus;
import com.joycoho.concis.sys.modular.user.entity.form.CreateUserForm;
import com.joycoho.concis.sys.modular.user.entity.form.LoginForm;
import com.joycoho.concis.sys.modular.user.entity.User;
import com.joycoho.concis.sys.modular.user.entity.form.SetAuthorityForm;
import com.joycoho.concis.sys.modular.user.entity.form.UpdateUserForm;
import com.joycoho.concis.sys.modular.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Classname UserController
 * @Description 系统用户控制层
 * @Version 1.0.0
 * @Date 2022/6/19 23:52
 * @Created by Leo
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;
    /**
     * 账号登录入口
     * @return
     */
    @RequestMapping(value = "/login/account", method = RequestMethod.POST)
    public Object loginByAccount(@RequestBody LoginForm form) {
        RtnInfo rtnInfo = userService.login(form.getUsername(), form.getPassword());
        return rtnInfo;
    }

    /**
     * 退出登录接口
     * @return
     */
    @RequestMapping("/logout")
    public Object logout(@RequestHeader("Authorization") String token) {
        userService.logout(token);
        return RtnInfo.SUCCESS;
    }

    /**
     * 分页查询用户列表
     * @param user
     * @return
     */
    @RequestMapping("/list")
    public Object list(User user) {
        Page<User> pageInfo = new PageFactory<User>().defaultPage();
        IPage<User> currentPage = userService.selectPages(pageInfo, user);

        RtnInfo result = RtnInfo.success(currentPage);
        return result;
    }

    /**
     * 新增系统用户
     * @return
     */
    @RequestMapping("/add")
    public Object add(@RequestBody CreateUserForm createUserForm) {
        //TODO 格式校验

        // 判断账号是否重复
        User theUser = userService.selectByAccount(createUserForm.getAccount());
        if (theUser != null) {
            throw new ServiceException(ResponseStatus.DUNPLI_USER);
        }
        User user = createUserForm.toUser();
        // 完善账号信息
        user.setSalt(ShiroKit.getRandomSalt(5));
        user.setPassword(ShiroKit.md5(user.getPassword(), user.getSalt()));
        user.setStatus(UserStatus.ACTIVE.code());
        user.setCreateTime(new Date());
        //插入数据库
        user.insert();
        return RtnInfo.SUCCESS;
    }

    /**
     * 修改系统用户
     * @return
     */
    @RequestMapping("/update")
    public Object update(@RequestBody UpdateUserForm updateUserForm) {
        //格式校验
        if (updateUserForm.getId() == null) {
            throw new ServiceException(ResponseStatus.MISSING_PARAMETERS);
        }
        User user = updateUserForm.toUser();
        // 完善账号信息
        user.setSalt(ShiroKit.getRandomSalt(5));
        user.setPassword(ShiroKit.md5(user.getPassword(), user.getSalt()));
        user.setStatus(UserStatus.ACTIVE.code());
        user.setCreateTime(new Date());
        //插入数据库
        user.updateById();
        return RtnInfo.SUCCESS;
    }

    /**
     * 批量删除用户（逻辑删除）
     * @return
     */
    @RequestMapping("/remove")
    public Object remove(@RequestBody List<Integer> selected) {
        //防御
        if (CollectionUtil.isEmpty(selected)) {
            return RtnInfo.SUCCESS;
        }
        //批量删
        List<User> users = new ArrayList<>();
        for (Integer id : selected) {
            users.add(User.builder().id(id).status(UserStatus.DELETED.code()).build());
        }
        userService.updateBatchById(users);
        return RtnInfo.SUCCESS;
    }

    @RequestMapping("/current")
    public Object currentUser() {

        User user = UserContext.getUser();
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);

        Map<String, Object> data = new HashMap<>();
        data.put("name",   user.getName());
        data.put("avatar", user.getAvatar());
        data.put("userid", user.getId());

        result.put("data", data);
        return result;
    }

    @RequestMapping("/notices")
    public Object notices() {
        JSONArray result = new JSONArray();

        JSONObject o1 = new JSONObject();
        o1.put("avatar", "https://gw.alipayobjects.com/zos/rmsportal/ThXAXghbEsBCCSDihZxY.png");
        o1.put("datetime", "2017-08-09");
        o1.put("id", "000000001");
        o1.put("title", "你收到了 14 份新周报");
        o1.put("type", "notification");

        result.add(o1);
        return result;
    }

    /**
     * 配置权限
     */
    @RequestMapping("/setAuthority")
    //@PermittedRole(Const.ADMIN_NAME)
    public Object setAuthority(@RequestBody SetAuthorityForm params) {
        if (params == null || params.getUserId() == null || CollectionUtil.isEmpty(params.getRoleIds())) {
            throw new ServiceException(ResponseStatus.MISSING_PARAMETERS);
        }
        this.userService.setAuthority(params.getUserId(), params.getRoleIds());
        return RtnInfo.SUCCESS;
    }
}
