package com.dc.cloud.common.test.web;

import com.dc.cloud.common.utils.UniversalViewResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;

/**
 * 测试 路径跳转问题
 */
@Slf4j
@RequestMapping("/tt/aa")
public class UserController {


    @GetMapping("/hello1")
    public View redirectHello() throws Exception {
       return UniversalViewResolver.forwardView("/hello",this.getClass());
    }


    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
