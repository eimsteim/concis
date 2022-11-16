package com.joycoho.concis.kernel.exception;

import com.joycoho.concis.kernel.context.RtnInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.joycoho.concis.kernel.context.HttpContext.getRequest;

/**
 * @Classname GlobalExceptionHandler
 * @Description 全局的的异常拦截器（拦截所有的控制器）（带有@RequestMapping注解的方法上都会拦截）
 * @Version 1.0.0
 * @Date 2022/11/5 21:30
 * @Created by Leo
 */
@Slf4j
@Order(-1)
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 拦截业务异常
     */
    @ExceptionHandler(ServiceException.class)
    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RtnInfo bussiness(ServiceException exception) {
        getRequest().setAttribute("tip", exception.getMessage());
        log.error("业务异常:", exception);
        return RtnInfo.error(exception.getCode(), exception.getErrorMessage());
    }
}
