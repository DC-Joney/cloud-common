package com.dc.cloud.common.excel;


import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

//excel 读取完成事件
@RequiredArgsConstructor
public class ExcelReadCompletedEvent {

    //同步 vs  半同步
    @NonNull
    @Getter
    private boolean asyncState;

}
