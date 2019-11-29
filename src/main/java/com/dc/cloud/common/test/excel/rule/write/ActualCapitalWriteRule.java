package com.dc.cloud.common.test.excel.rule.write;


import com.dc.cloud.common.test.excel.bean.ActualCapital;

import java.time.LocalDate;
import java.util.List;

public class ActualCapitalWriteRule extends AbstractCommonFundsWriteRule<ActualCapital> {

    public ActualCapitalWriteRule(List<ActualCapital> dataList,LocalDate localDate) {
        super(dataList,2,localDate);
    }

    @Override
    public Class<ActualCapital> headerClass() {
        return ActualCapital.class;
    }
}
