package com.dc.cloud.common.test.excel.event;



import com.dc.cloud.common.excel.rule.read.ReadExcelEvent;
import com.dc.cloud.common.test.excel.bean.CapitalPlan;

import java.util.List;

public class CapitalPlanEvent extends ReadExcelEvent<CapitalPlan> {
    public CapitalPlanEvent(List<CapitalPlan> source) {
        super(source);
    }
}
