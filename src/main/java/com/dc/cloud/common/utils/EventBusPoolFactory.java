package com.dc.cloud.common.utils;

import com.google.common.eventbus.EventBus;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.time.LocalDateTime;

/**
 * EventBus 对象 缓存池
 * 配置信息有问题
 */
public class EventBusPoolFactory extends BasePooledObjectFactory<EventBus> {

    @Override
    public EventBus create() throws Exception {
        return new EventBus(LocalDateTime.now().toString());
    }

    @Override
    public PooledObject<EventBus> wrap(EventBus eventBus) {
        return new DefaultPooledObject<>(eventBus);
    }
}
