package com.dc.cloud.common.excel.sheet;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.excel.read.metadata.holder.ReadSheetHolder;
import com.dc.cloud.common.excel.*;
import com.dc.cloud.common.excel.rule.ExcelReadRule;
import com.dc.cloud.common.excel.rule.read.AbstractExcelReadRule;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class ExcelReadListener<T> extends AnalysisEventListener<T> {

    @NonNull
    @Getter
    private final ExcelReadRule<T> excelReadRule;

    //是否读取结束
    private boolean readCompleted;

    @Getter
    private EventBus eventBus;

    @Getter
    private final Map<Integer, String> headerMap;

    private boolean initHeader = false;

    private int headerNum = 0;

    private boolean asyncState;

    public ExcelReadListener(ExcelReadRule<T> excelReadRule) {
        this(excelReadRule, false);
    }

    public ExcelReadListener(ExcelReadRule<T> excelReadRule, boolean asyncState) {
        this.asyncState = asyncState;
        this.excelReadRule = excelReadRule;
        this.headerMap = new HashMap<>(8);
    }


    @Override
    public void invoke(T data, AnalysisContext context) {
        if (!initHeader) {
            excelReadRule.handleHeaderExcelData(headerMap);
            headerMap.clear();
            initHeader = true;
        }

        if (!readCompleted) {
            excelReadRule.handleExcelData(data, context);
        }
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (eventBus != null) {
            eventBus.post(new ExcelReadCompletedEvent(asyncState));
            if (!asyncState) {
                eventBus.post(new SyncExcelManagement.SyncSheetSuccessEvent<>(excelReadRule.getSyncDataList(),
                        excelReadRule.excelDataClass()));
            }
            unRegister(false);
        }
    }

    //做头部解析
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        Map<Integer, String> excelHeadMap = Maps.filterValues(headMap, StringUtils::hasText);
        String headerMap = Joiner.on(",").withKeyValueSeparator("=").join(excelHeadMap);
        this.headerMap.put(headerNum++, headerMap);
    }


    @Override
    public boolean hasNext(AnalysisContext context) {

        if (readCompleted) {
            return false;
        }

        if (excelReadRule.endCondition(context)) {
            return !postSuccess(context);
        }

        return true;
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        log.error("exceptions is ## : {}", exception.getMessage());

        if (log.isDebugEnabled()) {
            log.debug("Parse excel exceptions, exception : {}", exception);
        }

        try {
            ReadSheetHolder sheetHolder = context.readSheetHolder();

            ReadRowHolder rowHolder = context.readRowHolder();

            String message = "第" + (sheetHolder.getSheetNo() + 1) + "个sheet页面名称为 : " + sheetHolder.getSheetName() + "，==> ";

            if (exception instanceof ExcelParseException || exception instanceof ExcelTitleParseException) {

                if (exception instanceof ExcelParseException) {
                    message = message + "第" + (rowHolder.getRowIndex() + 1) + "行数据格式出错: " + exception.getMessage();
                }
                if (exception instanceof ExcelTitleParseException) {
                    message = "excel标头错误，差异信息为: " + exception.getMessage();
                }

                throw new ExcelException(message);
            }

            boolean executeEnd = excelReadRule.endCondition(context);
            if (!executeEnd) {
                message = message + "第" + (rowHolder.getRowIndex() + 1) + "行信息格式出错，请比对校验";
                throw new ExcelException(message);
            }
        } catch (Exception ex) {

            //释放缓存卸载eventBus
            if (!readCompleted) {
                unRegister(true);
            }

            throw ex;
        }

        postSuccess(context);
    }


    //通知 excelRule发布数据存储
    private boolean postSuccess(AnalysisContext context) {
        doAfterAllAnalysed(context);
        return readCompleted = true;
    }

    /**
     * 注册eventBus
     *
     * @param excelManagement
     * @param eventBus
     * @throws Exception
     */
    public void register(ExcelManagement excelManagement, EventBus eventBus) throws Exception {
        this.eventBus = eventBus;
        eventBus.register(excelReadRule);
        eventBus.register(excelManagement);
    }

    /**
     * 清空缓存，然后卸载eventBus
     */
    private void unRegister(boolean errorState) {
        System.out.println("unRegister method execute !!!!!!! =====> " + excelReadRule.excelDataClass());

        //清空 excelRule 缓存
        eventBus.post(new AbstractExcelReadRule.ClearCacheEvent());
        eventBus.unregister(excelReadRule);

        //清空 manageMent 缓存
        eventBus.post(SyncExcelManagement.ClearCacheEvent.of(eventBus, errorState));
        headerMap.clear();
        eventBus = null;
    }

}
