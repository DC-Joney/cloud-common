package com.dc.cloud.common.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * 如果是针对java8的所有date建议使用spring的 Formaater和 Conversionce
 */
public class LocalDateStringConverter implements Converter<LocalDate> {

    @Override
    public Class supportJavaTypeKey() {
        return LocalDate.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }


    private DefaultFormattingConversionService conversionService;


    @Override
    public LocalDate convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
                                  GlobalConfiguration globalConfiguration) throws ParseException {
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return LocalDate.parse(cellData.getStringValue(),DateTimeFormatter.ISO_LOCAL_DATE);
        } else {
            return LocalDate.parse(cellData.getStringValue(),DateTimeFormatter.ofPattern(contentProperty.getDateTimeFormatProperty().getFormat()));
        }
    }

    @Override
    public CellData convertToExcelData(LocalDate localDate, ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration) {
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
             return new CellData(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        } else {
            return new CellData(localDate.format(DateTimeFormatter.ofPattern(contentProperty.getDateTimeFormatProperty().getFormat())));
        }
    }
}
