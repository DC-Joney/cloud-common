package com.dc.cloud.common.test;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "demo.server")
public class DemoServer {
    private String name;
    private List<String> favorites;
}
