package com.dc.cloud.common.excel.rule;

import com.alibaba.excel.write.handler.WriteHandler;

import java.util.List;
import java.util.Map;

public interface ExcelWriteRule<T> extends WriteHandler {


    /**
     * 填充的头部 以及其他类型
     * @return
     */
    Map<String,Object> getHeaderMap();


    /**
     * 填充的数据
     * @return
     */
    List<T> fillDataList();


    /**
     * 对应的excel sheet页数
     * @return
     */
    int sheetNo();


    Class<T> headerClass();

}
