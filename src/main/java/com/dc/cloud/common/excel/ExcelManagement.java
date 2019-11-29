package com.dc.cloud.common.excel;

import com.alibaba.excel.ExcelReader;
import com.dc.cloud.common.excel.rule.ExcelReadRule;
import com.dc.cloud.common.excel.sheet.ExcelReadListener;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

public interface ExcelManagement extends ApplicationEventPublisherAware {

    /**
     * 读取excel数据
     * @param excelReader
     * @param readRules
     * @return
     */
    ExcelManagement readExcel(ExcelReader excelReader, List<ExcelReadRule<?>> readRules);


}
