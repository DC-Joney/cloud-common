package com.dc.cloud.common.service;

import com.dc.cloud.common.web.MvcDispatchException;
import com.dc.cloud.common.utils.mvc.ResponseService;
import org.springframework.web.bind.annotation.RequestMapping;

//普通的Service类型
@ResponseService
public class DemoService {

    @RequestMapping("/test")
    public String test(){
        throw new MvcDispatchException("跳转出错");
    }


}
