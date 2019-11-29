package com.dc.cloud.common.test.excel.rule.write;



import com.dc.cloud.common.test.excel.bean.CapitalPlan;

import java.time.LocalDate;
import java.util.List;

public class CapitalPlanWriteRule extends AbstractCommonFundsWriteRule<CapitalPlan> {

    public CapitalPlanWriteRule(List<CapitalPlan> dataList, LocalDate localDate) {
        super(dataList,0,localDate);
    }

    @Override
    public Class<CapitalPlan> headerClass() {
        return CapitalPlan.class;
    }
}
