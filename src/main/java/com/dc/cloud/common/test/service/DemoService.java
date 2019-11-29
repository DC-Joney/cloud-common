package com.dc.cloud.common.test.service;

import com.dc.cloud.common.test.web.MvcDispatchException;
import com.dc.cloud.common.utils.mvc.ResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;

//普通的Service类型，如果方法内容果语简单不需要在Controller层做映射的
@Slf4j
@ResponseService
public class DemoService {

    @RequestMapping("/test")
    public String test(){
        throw new MvcDispatchException("跳转出错");
    }

    public boolean selectOne(String template){
        log.info("validate template is {}",template);
        return true;
    }


}
