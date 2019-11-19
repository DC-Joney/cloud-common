package com.dc.cloud.common.service;

import com.dc.cloud.common.web.MvcDispatchException;
import com.dc.cloud.common.utils.mvc.ResponseService;
import org.springframework.web.bind.annotation.RequestMapping;

//普通的Service类型，如果方法内容果语简单不需要在Controller层做映射的
@ResponseService
public class DemoService {

    @RequestMapping("/test")
    public String test(){
        throw new MvcDispatchException("跳转出错");
    }


}
