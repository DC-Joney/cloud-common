package com.dc.cloud.common.test.excel.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.dc.cloud.common.excel.validate.ExcelValidated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Getter
@Setter
@ExcelValidated
@EqualsAndHashCode(callSuper = true)
public class CapitalPlan extends SummaryFunds {

    @NotNull(message = "计划拨款日期不能为空")
    @ExcelProperty("计划拨款日期")
    @DateTimeFormat("yyyy/MM/dd")
    private LocalDate appropriationDate;

    @ExcelProperty("请示文号")
    private String invitationNumber;

    @ExcelProperty("审批进展")
    private String approvalProgress;

    @ExcelProperty("申请日期")
    @DateTimeFormat("yyyy/MM/dd")
    private LocalDate filingDate;


    @DateTimeFormat("yyyy/MM/dd")
    @ExcelProperty("计划还款日")
    private LocalDate repaymentDate;

    @Override
    public String toString() {
        return super.toString(this.getClass())
                .add("appropriationDate", appropriationDate)
                .add("invitationNumber", invitationNumber)
                .add("approvalProgress", approvalProgress)
                .add("filingDate", filingDate)
                .add("repaymentDate", repaymentDate)
                .toString();
    }


}
