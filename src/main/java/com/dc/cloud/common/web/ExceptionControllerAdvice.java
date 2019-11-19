package com.dc.cloud.common.web;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 测试 Service层 的异常处理
 */
@RestControllerAdvice
public class ExceptionControllerAdvice{

    @ExceptionHandler(value = MvcDispatchException.class)
    public String exceptHanding(ServletWebRequest request, Exception ex){
       return ex.getMessage();
    }

}
