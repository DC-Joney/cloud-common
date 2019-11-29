package com.dc.cloud.common.test.excel.bean;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.dc.cloud.common.excel.validate.ValidateSql;
import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ExcelIgnoreUnannotated
public class SummaryFunds  implements Serializable  {

    private Long id;

    @ExcelProperty("序号")
    private String seq;

    @NotNull(message = "金额不能为空")
    @ExcelProperty("金额")
    @DecimalMin(value = "0.00")
    private BigDecimal money;

    @NotBlank(message = "单位不能为空")
    @ExcelProperty("单位")
    private String unit;

    @NotBlank(message = "资金类型不能为空")
    @ValidateSql(condition = "@demoService.selectOne(#this)", message = "资金类型不匹配，请按照模板进行修改")
    @ExcelProperty("资金类型")
    private String capitalType;

    @ExcelProperty("项目名称")
    private String projectName;

    @ValidateSql(condition = "@demoService.selectOne(#this)", message = "项目类型不匹配，请按照模板进行修改")
    @ExcelProperty("项目类型")
    private String projectType;

    @ValidateSql(condition = "@demoService.selectOne(#this)", message = "专项额度不匹配，请按照模板进行修改")
    @ExcelProperty("专项额度使用")
    private String specialQuota;


    private LocalDate sendDate;

    MoreObjects.ToStringHelper toString(Class<?> className) {
        return MoreObjects.toStringHelper(className)
                .add("seq", seq).add("money", money).add("unit", unit)
                .add("capitalType", capitalType)
                .add("projectName", projectName)
                .add("specialQuota", specialQuota);
    }
}
