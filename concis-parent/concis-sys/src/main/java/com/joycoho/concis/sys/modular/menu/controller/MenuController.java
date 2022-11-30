package com.joycoho.concis.sys.modular.menu.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.joycoho.concis.kernel.context.RtnInfo;
import com.joycoho.concis.kernel.exception.ServiceException;
import com.joycoho.concis.sys.context.ResponseStatus;
import com.joycoho.concis.sys.context.UserContext;
import com.joycoho.concis.sys.modular.menu.entity.Menu;
import com.joycoho.concis.sys.modular.menu.entity.MenuNode;
import com.joycoho.concis.sys.modular.menu.entity.constant.MenuStatus;
import com.joycoho.concis.sys.modular.menu.entity.form.MenuForm;
import com.joycoho.concis.sys.modular.menu.service.impl.MenuServiceImpl;
import com.joycoho.concis.sys.modular.role.entity.Role;
import com.joycoho.concis.sys.modular.user.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname MenuController
 * @Description 菜单控制器
 * @Version 1.0.0
 * @Date 2022/10/20 13:58
 * @Created by Leo
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuServiceImpl menuService;

    /**
     * 新增菜单
     */
    //@PermittedRole(Const.ADMIN_NAME)
    @RequestMapping(value = "/add")
    public Object add(@RequestBody MenuForm form) {
        if (StringUtils.isBlank(form.getName()) || StringUtils.isBlank(form.getCode())) {
            throw new ServiceException(ResponseStatus.MISSING_PARAMETERS);
        }
        //判断是否有重复的菜单编码
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", form.getCode());
        List<Menu> list = menuService.list(queryWrapper);

        if (CollectionUtil.isNotEmpty(list)) {
            throw new ServiceException(ResponseStatus.EXISTED_DUNPLICATE_CODE);
        }
        //设置父级菜单编号pcode
        Menu menu = transFrom2Menu(form);
        //防止前端传入错误的ID
        menu.setId(null);
        //设置初始状态为启用
        menu.setStatus(MenuStatus.ENABLE.getCode());
        //保存到数据库
        menu.insert();

        return RtnInfo.SUCCESS;
    }

    /**
     * 修改系统用户
     * @return
     */
    @RequestMapping("/update")
    public Object update(@RequestBody MenuForm form) {
        //格式校验
        if (form.getId() == null) {
            throw new ServiceException(ResponseStatus.MISSING_PARAMETERS);
        }
        if (StringUtils.isBlank(form.getName()) || StringUtils.isBlank(form.getCode())) {
            throw new ServiceException(ResponseStatus.MISSING_PARAMETERS);
        }
        //判断是否有重复的菜单编码
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", form.getCode()).ne("id", form.getId());
        List<Menu> list = menuService.list(queryWrapper);

        if (CollectionUtil.isNotEmpty(list)) {
            throw new ServiceException(ResponseStatus.EXISTED_DUNPLICATE_CODE);
        }
        //设置父级菜单编号pcode
        Menu menu = transFrom2Menu(form);
        //设置初始状态为启用
        menu.setStatus(MenuStatus.ENABLE.getCode());
        //保存到数据库
        menu.updateById();

        return RtnInfo.SUCCESS;
    }


    /**
     * 删除菜单
     */
    //@PermittedRole(Const.ADMIN_NAME)
    @RequestMapping(value = "/remove")
    public Object remove(@RequestBody List<Integer> selected) {
        //防御
        if (CollectionUtil.isEmpty(selected)) {
            return RtnInfo.SUCCESS;
        }
        for (Integer id : selected) {
            //把当前菜单及其子菜单全部删除
            this.menuService.delMenuContainSubMenus(id);
        }
        return RtnInfo.SUCCESS;
    }

    /**
     * 用户登录后加载的左侧菜单树
     * @return
     */
    @RequestMapping("/listTree")
    public List<MenuNode> listTree(String appCode) {
        //查询当前登录用户
        User currentUser = UserContext.getUser();
        //查询用户角色集，roleid可能是多个，以逗号分隔
        String roleId = currentUser.getRoleId();
        //将roleid转为List
        List<Integer> roleIds = new ArrayList<>();
        if (roleId.indexOf(",") > -1) {//如果有多个，拆分添加
            String[] a = roleId.split(",");
            for (String s : a) {
                roleIds.add(Integer.valueOf(s));
            }
        } else {//如果只有一个，添加自己
            roleIds.add(Integer.valueOf(roleId));
        }
        return menuService.getMenusByRoleIds(appCode, roleIds, true);
    }

    @RequestMapping("/listAllByTree")
    public Object listAllByTree() {
        return RtnInfo.success(menuService.getAllMenusByTree(false));
    }

    /**
     * 查询角色对应的所有菜单权限
     * @param role
     * @return
     */
    @RequestMapping("/listTreeByRoleId")
    public Object listTreeByRoleId(Role role) {
        if (role.getId() == null) {
            return RtnInfo.error(ResponseStatus.MISSING_PARAMETERS);
        }
        //查询用户角色集，roleid可能是多个，以逗号分隔
        int roleId = role.getId();
        //将roleid转为List
        List<Integer> roleIds = new ArrayList<>();
        roleIds.add(Integer.valueOf(roleId));
        return RtnInfo.success(menuService.getMenusByRoleIds(roleIds, false));
    }

    /**
     * 查询角色对应的所有菜单权限
     * @param role
     * @return
     */
    @RequestMapping("/listTreeByRoleParentId")
    public Object listTreeByRoleParentId(Role role) {
        Role me = role.selectById(role.getId());
        int queryId;
        if (me.getPid() == null || me.getPid() == 0) {
            //如果本身已经是顶级了，那么返回所有角色
            queryId = 1;
        } else {
            //如果本身不是顶级，那么取其父级，以限制其权限范围
            queryId = me.getPid();
        }
        List<Integer> roleIds = new ArrayList<>();
        roleIds.add(Integer.valueOf(queryId));
        return RtnInfo.success(menuService.getMenusByRoleIds(roleIds, false));
    }

    /**
     * 查询角色对应的所有菜单权限
     * @param role
     * @return
     */
    @RequestMapping("/listMenuIdsByRoleId")
    public Object listMenuIdsByRoleId(Role role) {
        if (role.getId() == null) {
//            return RtnInfo.error(ReqStatus.MISSING_PARAMETERS);
            role.setId(1);
        }
        //查询用户角色集，roleid可能是多个，以逗号分隔
        int roleId = role.getId();
        //将roleid转为List
        List<Integer> roleIds = new ArrayList<>();
        roleIds.add(Integer.valueOf(roleId));
        return RtnInfo.success(menuService.getMenuIdsByRoleIds(roleIds, false));
    }


    /**
     * 将form对象转化为Menu对象
     */
    private Menu transFrom2Menu(MenuForm form) {
        Menu menu = Menu.builder().id(form.getId()).name(form.getName()).code(form.getCode()).icon(form.getIcon())
                .ismenu(form.getIsmenu()).num(form.getNum()).path(form.getPath()).build();
        if (form.getParentId() == null || form.getParentId() == 0) {
            menu.setPcode("0");
            menu.setPcodes("[0],");
            menu.setLevels(1);
        } else {
            //如果parentId不为空，说明是建立在某个上级菜单下的子菜单
            Integer parentId = form.getParentId();
            Menu pMenu = menu.selectById(parentId);

            //如果编号和父编号一致会导致无限递归
            if (menu.getCode().equals(pMenu.getCode())) {
                throw new ServiceException(ResponseStatus.MENU_PCODE_COINCIDENCE);
            }
            menu.setPcode(pMenu.getCode());

            //那么需要根据其上级菜单的levels递增
            Integer pLevels = pMenu.getLevels();
            menu.setLevels(pLevels + 1);
            //开始设置pcodes
            menu.setPcodes(pMenu.getPcodes() + "[" + pMenu.getCode() + "],");
        }
        return menu;
    }
}
