package com.dc.cloud.common.excel;


/**
 * excel 表头匹配错误
 */
public class ExcelTitleParseException extends RuntimeException {
    public ExcelTitleParseException(String message) {
        super(message);
    }

    public ExcelTitleParseException(String message, Throwable cause) {
        super(message, cause);
    }


}
