package com.dc.cloud.common.test.excel;

import com.dc.cloud.common.test.excel.bean.ActualCapital;
import com.dc.cloud.common.test.excel.bean.CapitalPlan;
import lombok.*;

import java.util.List;

@Getter @ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SummaryFundsEvent{

    private List<CapitalPlan> capitalPlans;

    private List<ActualCapital> actualCapitals;
}
