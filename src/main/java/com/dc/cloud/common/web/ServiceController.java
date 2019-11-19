package com.dc.cloud.common.web;

import com.dc.cloud.common.service.DemoService;
import com.dc.cloud.common.utils.mvc.ServiceMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 映射Service层的 路径
 */
@RestController
@ServiceMapping(mappingClass = DemoService.class,path = "/demo")
public class ServiceController {


    @RequestMapping("/hello")
    public String demoHello(){
        return "demoHello";
    }



}
