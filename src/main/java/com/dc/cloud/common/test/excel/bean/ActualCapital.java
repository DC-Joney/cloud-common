package com.dc.cloud.common.test.excel.bean;


import com.alibaba.excel.annotation.ExcelProperty;
import com.dc.cloud.common.excel.validate.ExcelValidated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;



@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@ExcelValidated
public class ActualCapital extends SummaryFunds {

    @ExcelProperty("说明")
    private String actualExplain;

    @Override
    public String toString() {
        return super.toString(this.getClass())
                .add("actualExplain", actualExplain).toString();
    }
}
