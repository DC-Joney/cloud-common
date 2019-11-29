package com.dc.cloud.common;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class CloudCommonApplication {

    public static void main(String[] args) {

        SpringApplicationBuilder builder = new SpringApplicationBuilder(CloudCommonApplication.class);

        builder.web(WebApplicationType.SERVLET).run(args);

    }

}
