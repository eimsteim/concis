package com.joycoho.concis.sys.modular.user.constant;

/**
 * @Classname UserStatus
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/10/5 21:30
 * @Created by Leo
 */
public enum UserStatus {

    ACTIVE(1, "正常"),
    FROZEN(2, "冻结"),
    DELETED(3, "已删除");

    UserStatus(int code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    private Integer code;
    private String remark;

    public int code() {
        return this.code;
    }
    public String remark() {
        return this.remark;
    }
}
