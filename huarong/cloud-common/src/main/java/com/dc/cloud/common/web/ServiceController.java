package com.dc.rong.web;

import com.dc.rong.service.DemoService;
import com.dc.rong.utils.mvc.ServiceMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ServiceMapping(mappingClass = DemoService.class,path = "/demo")
public class ServiceController {


    @RequestMapping("/hello")
    public String demoHello(){
        return "demoHello";
    }



}
