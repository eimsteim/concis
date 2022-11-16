package com.joycoho.concis.kernel.context;

/**
 * @Classname ResponseStatus
 * @Description 基础的响应状态枚举类
 * @Version 1.0.0
 * @Date 2022/11/5 23:53
 * @Created by Leo
 */
public enum BaseResponseStatus implements IResponseEnum {
    SUCCESS(200, "成功"),
    ERROR(500, "失败");


    BaseResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;

    @Override
    public Integer code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }
}
