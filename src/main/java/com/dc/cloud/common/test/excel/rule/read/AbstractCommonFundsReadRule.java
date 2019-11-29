package com.dc.cloud.common.test.excel.rule.read;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellData;
import com.dc.cloud.common.excel.rule.read.AbstractExcelReadRule;
import com.dc.cloud.common.excel.rule.read.ReadExcelEvent;
import com.dc.cloud.common.test.excel.bean.SummaryFunds;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;


// 处理 SummaryFunds 数据
public abstract class AbstractCommonFundsReadRule<T extends SummaryFunds> extends AbstractExcelReadRule<T> {

    private int sheetIndex;

    private LocalDate localDate;


    AbstractCommonFundsReadRule(Class<T> dataClass, int sheetIndex, LocalDate localDate) {
        super(Collections.singletonList(HeaderKey.of(3, "title")), dataClass);
        this.sheetIndex = sheetIndex;
        this.localDate = localDate;
    }

    /**
     * 读取excel表格什么时候停止
     * @param context
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean endCondition(AnalysisContext context) {
        Object result = context.readRowHolder().getCurrentRowAnalysisResult();
        if (result instanceof LinkedHashMap) {
            LinkedHashMap<Integer, CellData> linkedHashMap = (LinkedHashMap<Integer, CellData>) result;
            return linkedHashMap.size() > 0 && endCondition(linkedHashMap);
        }
        return false;
    }


    @Override
    public int sheetIndex() {
        return sheetIndex;
    }

    @Override
    public int headerNum() {
        return 4;
    }

    protected boolean endCondition(LinkedHashMap<Integer, CellData> floor) {
        return true;
    }

    @Override
    protected void handleData(T data) {

        //对数据做一部分修改
        data.setSendDate(localDate);

    }

    @Override
    public ReadExcelEvent<T> getExcelEvent(List<T> dataList) {
        return null;
    }
}
