package com.dc.cloud.common.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@Data
//@ConfigurationProperties(prefix = "spring.arch.web")
public class MvcUrlProperties {

    private Map<String, String> simpleUrlMap;

    private List<UrlClassMapping> validateUrlMap;

    @Data
    public static class UrlClassMapping {
        private Class<?> serviceClass;
        private String url;
    }
}
