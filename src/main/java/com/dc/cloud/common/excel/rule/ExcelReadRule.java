package com.dc.cloud.common.excel.rule;

import com.alibaba.excel.context.AnalysisContext;
import com.dc.cloud.common.excel.rule.read.ReadExcelEvent;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.List;
import java.util.Map;

public interface ExcelReadRule<T> extends ApplicationEventPublisherAware {


    /**
     * 一行一行的处理excel的数据
     * @param data
     * @param context
     */
    void handleExcelData(T data, AnalysisContext context);


    /**
     * 处理excel header数据
     * @param headerMap
     */
    void handleHeaderExcelData(Map<Integer, String> headerMap);


    /**
     * 绑定的java类型
     * @return
     */
    Class<T> excelDataClass();

    /**
     * 判断excel表格是否解析到末尾
     * @param context
     * @return
     */
    boolean endCondition(AnalysisContext context);


    /**
     * 同步返回的list类型
     * @return
     */
    List<T> getSyncDataList();


    /**
     * 返回的对读取事件
     * @return
     */
    ReadExcelEvent<T> getReadEvent();


    /**
     * 忽略的header行数
     * @return
     */
    int headerNum();


    /**
     * 绑定的sheet页面
     * @return
     */
    int sheetIndex();

}
