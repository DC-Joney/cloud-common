package com.dc.cloud.common.excel.rule.read;


import java.util.List;

/**
 * 读取excel数据事件
 * @param <T>
 */
public abstract class ReadExcelEvent<T> extends ExcelDataEvent<List<T>> {
    public ReadExcelEvent(List<T> dataList) {
        super(dataList);
    }
}
