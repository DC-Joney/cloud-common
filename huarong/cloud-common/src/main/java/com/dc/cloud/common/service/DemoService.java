package com.dc.rong.service;

import com.dc.rong.utils.mvc.MvcDispatchException;
import com.dc.rong.utils.mvc.ResponseService;
import org.springframework.web.bind.annotation.RequestMapping;

//普通的Service类型
@ResponseService
public class DemoService {

    @RequestMapping("/test")
    public String test(){
        throw new MvcDispatchException("跳转出错");
    }


}
