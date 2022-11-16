package com.joycoho.concis.kernel.context;

/**
 * @Classname AbstractBaseExceptionEnum
 * @Description 抽象的基础异常枚举
 * @Version 1.0.0
 * @Date 2022/11/5 16:10
 * @Created by Leo
 */
public interface IResponseEnum {
    Integer code();

    String message();
}

