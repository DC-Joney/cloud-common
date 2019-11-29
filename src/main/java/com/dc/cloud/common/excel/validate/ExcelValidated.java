package com.dc.cloud.common.excel.validate;

import java.lang.annotation.*;

/**
 * 防止和spring的数据校验冲突
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelValidated {
}
