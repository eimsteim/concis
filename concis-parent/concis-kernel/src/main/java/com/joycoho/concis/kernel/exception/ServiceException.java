package com.joycoho.concis.kernel.exception;

import com.joycoho.concis.kernel.context.IResponseEnum;

/**
 * @Classname ServiceException
 * @Description 业务异常
 * @Version 1.0.0
 * @Date 2022/11/5 16:10
 * @Created by Leo
 */

public class ServiceException extends RuntimeException {
    private Integer code;
    private String errorMessage;

    public ServiceException(Integer code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public ServiceException(IResponseEnum exception) {
        super(exception.message());
        this.code = exception.code();
        this.errorMessage = exception.message();
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

