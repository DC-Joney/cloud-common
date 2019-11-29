package com.dc.cloud.common.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.dc.cloud.common.excel.rule.ExcelReadRule;
import com.dc.cloud.common.excel.rule.read.ReadExcelEvent;
import com.dc.cloud.common.excel.sheet.ExcelReadListener;
import com.dc.cloud.common.utils.EventBusPool;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.Assert;
import org.springframework.web.context.request.async.DeferredResult;
import reactor.util.context.Context;

import java.util.*;
import java.util.function.Function;

/**
 * 同步Excel管理器
 */
@Slf4j
public class SyncExcelManagement implements ExcelManagement {

    private int successState = 0;

    private int sheetSize;

    private ApplicationEventPublisher publisher;

    private Set<ExcelReadListener<?>> listeners = Sets.newHashSet();

    private EventBusPool eventBusPool;

    private Context excelContext = Context.empty();

    SyncExcelManagement(EventBusPool eventBusPool) {
        this.eventBusPool = eventBusPool;
    }

    @Subscribe
    public void excelReadSuccess(SyncSheetSuccessEvent event) {
        successState += event.getNum();
        if (event.dataList.size() > 0) {
            excelContext = excelContext.put(event.dataClass, event.dataList);
        }
    }


    public <T, E> T doSync(Function<Context, T> convertFunction) {
        try {
            if (successState == sheetSize) {
                Objects.requireNonNull(convertFunction, "The convertFunction must not be null");
                return Objects.requireNonNull(convertFunction.apply(excelContext), "Custom event must not be null");
            }
        } finally {
            clearCache();
        }

        throw new ExcelException("数据解析异常");
    }


    private void clearCache() {
        listeners.clear();
        excelContext = null;
        //清除缓存
    }


    @Subscribe
    public void clearCache(ClearCacheEvent cacheEvent) {
        try {
            cacheEvent.eventBus.unregister(this);
            eventBusPool.getObjectPool().returnObject(cacheEvent.eventBus);
        } catch (Exception e) {
            log.error("卸载eventBus出错，错误信息为 : ", e);
            throw new ExcelException("解析excel数据出错,请联系管理员解决");
        }
        Optional.of(cacheEvent).filter(event-> event.errorState).ifPresent(event-> clearCache());
    }


    @Override
    public SyncExcelManagement readExcel(ExcelReader excelReader,List<ExcelReadRule<?>> readRules) {
        this.sheetSize = readRules.size();
        readRules.forEach(excelRule->{
            Assert.notNull(excelRule, "The read excelRule must not be null");
            ExcelUtils.getSharedInstance().initializeExcelRule(excelRule);
            ExcelReadListener<?> listener = new ExcelReadListener<>(excelRule);
            excelReader.read(addListener(listener).buildSheet(excelRule,listener));
        });
        excelReader.finish();
        return this;
    }

    private ReadSheet buildSheet(ExcelReadRule<?> excelReadRule, ExcelReadListener<?> listener) {
        return EasyExcel.readSheet(excelReadRule.sheetIndex()).head(excelReadRule.excelDataClass())
                .headRowNumber(excelReadRule.headerNum()).registerReadListener(listener).build();
    }


    public SyncExcelManagement addListener(ExcelReadListener readListener) {
        try {
            EventBus eventBus = eventBusPool.getObjectPool().borrowObject();
            readListener.register(this, eventBus);
            listeners.add(readListener);
        } catch (Exception e) {
            log.error("注册sheet单元出错，错误信息为 : {} ", e);
            throw new ExcelException("解析EXCEL表格出错，请及时联系管理员");
        }
        return this;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }


    @Getter
    @RequiredArgsConstructor
    public static class SyncSheetSuccessEvent<T> {

        private int num = 1;

        @NonNull
        private List<T> dataList;

        @NonNull
        private Class<?> dataClass;
    }


    @RequiredArgsConstructor(staticName = "of")
    public static class ClearCacheEvent {

        @NonNull
        private EventBus eventBus;

        @NonNull
        private boolean errorState;

    }

}
