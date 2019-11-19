package com.dc.cloud.common.utils;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

// SmartInitializingSingleton
@Component
public class SharedInstanceInitializer implements SmartApplicationListener {

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return ContextRefreshedEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return ApplicationContext.class.isAssignableFrom(sourceType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        ApplicationContext applicationContext = (ApplicationContext) event.getSource();

        //configureBean
        applicationContext.getAutowireCapableBeanFactory()
                .autowireBeanProperties(UniversalViewResolver.shardInstance(), AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE,false);
        applicationContext.getAutowireCapableBeanFactory()
                .initializeBean(UniversalViewResolver.shardInstance(),UniversalViewResolver.class.getSimpleName());
    }
}
