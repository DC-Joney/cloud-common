package com.dc.cloud.common.utils;

import com.google.common.eventbus.EventBus;
import lombok.Getter;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.commons.pool2.impl.SoftReferenceObjectPool;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * EventBus 对象 缓存池
 * config 配置信息有问题，需要重新配置
 */
@Component
public class EventBusPool implements InitializingBean {

    @Getter
    private ObjectPool<EventBus> objectPool;

    @Override
    public void afterPropertiesSet() throws Exception {

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();

        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(6);
        poolConfig.setMaxWaitMillis(1000 * 3);

        poolConfig.setTestWhileIdle(true);
        poolConfig.setJmxEnabled(false);
        poolConfig.setBlockWhenExhausted(false);

        EventBusPoolFactory poolFactory = new EventBusPoolFactory();

        objectPool = new SoftReferenceObjectPool<>(poolFactory);
    }


}
