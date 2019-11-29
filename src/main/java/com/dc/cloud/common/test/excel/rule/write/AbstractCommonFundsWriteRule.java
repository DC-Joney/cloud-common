package com.dc.cloud.common.test.excel.rule.write;

import com.alibaba.excel.metadata.Head;
import com.dc.cloud.common.excel.rule.write.AbstractExcelWriteRule;
import com.dc.cloud.common.test.excel.bean.SummaryFunds;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;


@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public abstract class AbstractCommonFundsWriteRule<T extends SummaryFunds>
        extends AbstractExcelWriteRule<T> {

    LocalDate localDate;

    List<T> dataList;

    AbstractCommonFundsWriteRule(List<T> dataList, int sheetNo, LocalDate localDate) {
        super(sheetNo);
        this.dataList = dataList;
        this.localDate = localDate;
    }

    @Override
    protected void setContentCellStyle(Cell cell, Head head, Integer relativeRowIndex) {

    }

    @Override
    public List<T> fillDataList() {

        IntStream.range(0, dataList.size())
                .forEach(i -> dataList.get(i).setSeq(String.valueOf(i + 1)));

        return Collections.unmodifiableList(dataList);
    }

    @Override
    public Map<String, Object> getHeaderMap() {

        HashMap<String, Object> hashMap = Maps.newHashMap();

        hashMap.put("dateTime", LocalDate.now());

        return Collections.unmodifiableMap(hashMap);
    }


}
