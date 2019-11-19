package com.dc.cloud.common.utils.mvc;


import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented

/**
 * 指定在Controller层做映射，然后将Controller层和 Service 层合并 path
 */
@RequestMapping
public @interface ServiceMapping {

    @AliasFor(
            annotation = RequestMapping.class
    )
    RequestMethod[] method() default {};

    @AliasFor(
            annotation = RequestMapping.class
    )
    String name() default "";

    @AliasFor(
            annotation = RequestMapping.class
    )
    String[] value() default {};

    @AliasFor(
            annotation = RequestMapping.class
    )
    String[] path() default {};

    @AliasFor(
            annotation = RequestMapping.class
    )
    String[] params() default {};

    @AliasFor(
            annotation = RequestMapping.class
    )
    String[] headers() default {};

    @AliasFor(
            annotation = RequestMapping.class
    )
    String[] consumes() default {};

    @AliasFor(
            annotation = RequestMapping.class
    )
    String[] produces() default {};


    //需要处理的Service层次名称
    Class<?> mappingClass();

}
