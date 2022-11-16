package com.joycoho.concis.sys.context;

import com.joycoho.concis.kernel.context.IResponseEnum;

public enum ResponseStatus implements IResponseEnum {

    SUCCESS(200, "成功"),
    MISSING_PARAMETERS(500, "参数缺失"),
    CUSTOMER_NOT_FOUND(500, "用户不存在"),
    PWD_NOT_MATCH(500, "密码不匹配"),
    DUNPLI_USER(500, "用户名已存在"),
    NOT_IN_WHITELIST(500, "非法地址访问，不在白名单中"),
    CANT_DELETE_ADMIN(500, "不能删除超级管理员"),
    MENU_PCODE_COINCIDENCE(400, "菜单编号不能与其父编号相同"),
    EXISTED_DUNPLICATE_CODE(400, "存在重复的编号"),

    NOT_LOGIN(400, "用户未登录");


    ResponseStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;

    private String message;

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }
}
