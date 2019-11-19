package com.dc.cloud.common.utils.mvc;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
@ResponseBody
public @interface ResponseService {
    @AliasFor(annotation = Component.class)
    String value() default "";
}
