package com.dc.rong.web;

import com.dc.rong.utils.mvc.MvcDispatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

@RestControllerAdvice
public class ExceptionControllerAdvice{

    @ExceptionHandler(value = MvcDispatchException.class)
    public String exceptHanding(ServletWebRequest request, Exception ex){
       return ex.getMessage();
    }

}
