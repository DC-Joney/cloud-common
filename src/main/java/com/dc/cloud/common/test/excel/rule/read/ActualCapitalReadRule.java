package com.dc.cloud.common.test.excel.rule.read;

import com.alibaba.excel.context.AnalysisContext;
import com.dc.cloud.common.test.excel.bean.ActualCapital;
import com.dc.cloud.common.test.excel.bean.SummaryFunds;

import java.time.LocalDate;

public class ActualCapitalReadRule extends AbstractCommonFundsReadRule<ActualCapital> {

    public ActualCapitalReadRule(LocalDate localDate) {
        super(ActualCapital.class,1,localDate);
    }


    @Override
    public boolean endCondition(AnalysisContext context) {
        Object result = context.readRowHolder().getCurrentRowAnalysisResult();
        if(result instanceof SummaryFunds){
            SummaryFunds funds = (SummaryFunds) result;
            return funds.getSeq() != null && funds.getSeq().equals("未投放");
        }
        return false;
    }


    @Override
    protected boolean isValidateData(ActualCapital capital, AnalysisContext context) {
        if(endCondition(context)){
            return false;
        }
        return !(capital.getCapitalType() == null && capital.getMoney() == null && capital.getUnit() == null);

    }


}
