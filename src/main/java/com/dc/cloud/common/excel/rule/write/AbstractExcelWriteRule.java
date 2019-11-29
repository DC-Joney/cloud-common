package com.dc.cloud.common.excel.rule.write;


import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;
import com.dc.cloud.common.excel.rule.ExcelWriteRule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;


@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public abstract class AbstractExcelWriteRule<T> extends AbstractCellStyleStrategy
        implements ExcelWriteRule<T> {

    @Getter
    int sheetNo;


    @Override
    public int sheetNo() {
        return sheetNo;
    }


    protected void initCellStyle(Workbook workbook) {

    }


    protected void setHeadCellStyle(Cell cell, Head head, Integer relativeRowIndex) {

    }

    protected abstract void setContentCellStyle(Cell cell, Head head, Integer relativeRowIndex);


}
