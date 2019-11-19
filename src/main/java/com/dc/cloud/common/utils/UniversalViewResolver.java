package com.dc.cloud.common.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Locale;
import java.util.Optional;

public class UniversalViewResolver implements InitializingBean {

    @Autowired
    private InternalResourceViewResolver viewResolver;

    private CacheLoader<CacheKey, String> cacheLoader = new UrlCacheLoader();

    private LoadingCache<CacheKey, String> urlCache;

    @Override
    public void afterPropertiesSet(){
        urlCache = CacheBuilder.newBuilder()
                .maximumSize(1024).initialCapacity(20)
                .weakKeys().softValues()
                .build(cacheLoader);
    }

    public static View forwardView(String url, Class<?> controllerClass) throws Exception {
        CacheKey cacheKey = new CacheKey(url, controllerClass);
        String controllerPath = shardInstance().urlCache.get(cacheKey);
        return shardInstance().viewResolver.
                resolveViewName(UrlBasedViewResolver.FORWARD_URL_PREFIX + controllerPath, Locale.CHINA);
    }


    public static View forwardView(String url) throws Exception {
        return forwardView(url, null);
    }

    public static View redirectView(String url) throws Exception {
        return shardInstance().viewResolver.resolveViewName(UrlBasedViewResolver.REDIRECT_URL_PREFIX + url, Locale.CHINA);
    }

    public static View redirectView(String url, Class<?> controllerClass) throws Exception {
        CacheKey cacheKey = new CacheKey(url, controllerClass);
        String controllerPath = shardInstance().urlCache.get(cacheKey);
        return shardInstance().viewResolver.
                resolveViewName(UrlBasedViewResolver.REDIRECT_URL_PREFIX + controllerPath, Locale.CHINA);
    }


    protected static UniversalViewResolver shardInstance() {
        return UniversalViewResolverHolder.INSTANCE;
    }

    private static class UniversalViewResolverHolder {
        private static UniversalViewResolver INSTANCE = new UniversalViewResolver();
    }


    @AllArgsConstructor
    private static class CacheKey {

        @NonNull
        private String url;

        private Class<?> controllerClass;

    }


    private static class UrlCacheLoader extends CacheLoader<CacheKey, String> {

        @Override
        public String load(CacheKey cacheKey) throws Exception {
            return mergeUrl(cacheKey.url, cacheKey.controllerClass);
        }

        private String mergeUrl(String url, Class<?> controllerClass) {
            return Optional.ofNullable(controllerClass)
                    .flatMap(clazz -> findControllerPath(url, controllerClass)).orElse(url);
        }

        private Optional<String> findControllerPath(String url, Class<?> controllerClass) {

            UriComponentsBuilder uriComponentsBuilder = MvcUriComponentsBuilder.fromController(controllerClass);

            UriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(uriComponentsBuilder);

            String rawPath = uriBuilderFactory.expand(url).getRawPath();

            return Optional.ofNullable(rawPath);
        }
    }


}
