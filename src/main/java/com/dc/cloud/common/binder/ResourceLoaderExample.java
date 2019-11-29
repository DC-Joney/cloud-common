package com.dc.cloud.common.binder;

import com.dc.cloud.common.test.DemoServer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Log4j2
@Component
public class ResourceLoaderExample  {

    @Autowired
    private SpringResourceLoader resourceLoader;

//    @PostConstruct
    public void init(){
        BindResult<DemoServer> bindResult = resourceLoader
                .bindProperty("demo.yml", DemoServer.class, "demo.server");

        if(bindResult.isBound()){
            bindResult.ifBound(log::info);
        }

        BindResult<DemoServer> bindResult1 = resourceLoader
                .bindSpringProperty(DemoServer.class, "demo.server");

        if(bindResult1.isBound()){
            bindResult1.ifBound(log::info);
        }

        BindResult<DemoServer> bindResult2 = resourceLoader
                .bindSpringProperty(new DemoServer(), "demo.server");

        if(bindResult2.isBound()){
            bindResult2.ifBound(log::info);
        }
    }
}
