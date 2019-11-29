package com.dc.cloud.common.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.dc.cloud.common.excel.converter.LocalDateNumberConverter;
import com.dc.cloud.common.excel.converter.LocalDateStringConverter;
import com.dc.cloud.common.excel.rule.ExcelReadRule;
import com.dc.cloud.common.excel.rule.ExcelWriteRule;
import com.dc.cloud.common.excel.sheet.ExcelReadListener;
import com.dc.cloud.common.utils.LocalContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.util.Assert;
import reactor.util.context.Context;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class ExcelUtils implements ApplicationContextAware, ApplicationEventPublisherAware {

    private ApplicationContext applicationContext;

    private final LocalDateStringConverter dateStringConverter = new LocalDateStringConverter();
    private final LocalDateNumberConverter dateNumberConverter = new LocalDateNumberConverter();


    private ExcelUtils() {
    }

    @Autowired
    private LocalContext localContext;

    private ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }


    /**
     * @param inputStream    读取的文件流
     * @param excelReadRules excel Sheet页面的读取规则
     * @return
     */
    public static <T> T readExcel(InputStream inputStream,
                                  List<ExcelReadRule<?>> excelReadRules,
                                  Function<Context, T> convertFunction) {

        Assert.notEmpty(excelReadRules, "Read excelReadRules must not be empty");

        ExcelReader excelReader = EasyExcel.read(inputStream)
                .registerConverter(getSharedInstance().dateStringConverter)
                .registerConverter(getSharedInstance().dateNumberConverter).build();

        //excel读取数据管理器
        SyncExcelManagement excelManagement = new SyncExcelManagement(LocalContext.getEventBusPool());

        excelManagement.setApplicationEventPublisher(getSharedInstance().eventPublisher);

        //返回读取的event事件
        return excelManagement.readExcel(excelReader, excelReadRules).doSync(convertFunction);


    }


    ExcelReadRule<?> initializeExcelRule(ExcelReadRule<?> excelReadRule) {
        getSharedInstance().applicationContext.getAutowireCapableBeanFactory()
                .autowireBeanProperties(excelReadRule, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
        return (ExcelReadRule<?>) getSharedInstance().applicationContext.getAutowireCapableBeanFactory()
                .initializeBean(excelReadRule, excelReadRule.getClass().getSimpleName());
    }


    /**
     * 导出 excel数据
     *
     * @param rules 导出规则
     * @return
     */

    public static void writeExcel(OutputStream stream, Collection<ExcelWriteRule<?>> rules, Path templateFile) {

        Assert.notEmpty(rules,
                "Read excel is errored，write rules must not be null");

        ExcelWriter writer = EasyExcel.write(stream).withTemplate(templateFile.toFile())
                .registerConverter(getSharedInstance().dateStringConverter)
                .registerConverter(getSharedInstance().dateNumberConverter).build();

        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();

        rules.forEach(writeRule -> writeCoreExcel(writer, fillConfig, writeRule));

        writer.finish();
    }

    private static void writeCoreExcel(ExcelWriter writer, FillConfig config, ExcelWriteRule<?> writeRule) {
        WriteSheet writeSheet = EasyExcel.writerSheet(writeRule.sheetNo()).head(writeRule.getClass())
                .registerWriteHandler(writeRule).build();
        writer.fill(writeRule.fillDataList(), config, writeSheet);
        writer.fill(writeRule.getHeaderMap(), writeSheet);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    public static ExcelUtils getSharedInstance() {
        return ExcelUtilsHolder.INSTANCE;
    }

    private static class ExcelUtilsHolder {
        private static final ExcelUtils INSTANCE = new ExcelUtils();
    }


}
