package com.dc.cloud.common.test.excel.rule.read;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellData;
import com.dc.cloud.common.test.excel.bean.CapitalPlan;

import java.time.LocalDate;
import java.util.LinkedHashMap;

public class CapitalPlanReadRule extends AbstractCommonFundsReadRule<CapitalPlan> {


    public CapitalPlanReadRule(LocalDate localDate) {
        super(CapitalPlan.class,0,localDate);
    }


    @Override
    protected boolean endCondition(LinkedHashMap<Integer, CellData> floor) {
        if (floor.get(1) != null) {
            CellData cellData = floor.get(1);
            return cellData.toString().equals("合计");
        }
        return false;
    }


    @Override
    protected boolean isValidateData(CapitalPlan capitalPlan, AnalysisContext context) {
        return !(capitalPlan.getCapitalType() == null && capitalPlan.getMoney() == null && capitalPlan.getUnit() == null &&
                capitalPlan.getAppropriationDate() == null);
    }

}
