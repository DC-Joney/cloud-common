package com.dc.cloud.common.test.excel.event;



import com.dc.cloud.common.excel.rule.read.ReadExcelEvent;
import com.dc.cloud.common.test.excel.bean.ActualCapital;

import java.util.List;

public class ActualCapitalEvent extends ReadExcelEvent<ActualCapital> {

    public ActualCapitalEvent(List<ActualCapital> dataList) {
        super(dataList);
    }
}
