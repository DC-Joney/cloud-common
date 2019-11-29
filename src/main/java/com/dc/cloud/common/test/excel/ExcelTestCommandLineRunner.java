package com.dc.cloud.common.test.excel;

import com.dc.cloud.common.excel.ExcelUtils;
import com.dc.cloud.common.excel.rule.ExcelReadRule;
import com.dc.cloud.common.test.excel.bean.ActualCapital;
import com.dc.cloud.common.test.excel.bean.CapitalPlan;
import com.dc.cloud.common.test.excel.rule.read.ActualCapitalReadRule;
import com.dc.cloud.common.test.excel.rule.read.CapitalPlanReadRule;
import com.google.common.collect.ImmutableList;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.util.context.Context;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.List;

@Component
public class ExcelTestCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        LocalDate sendDate = LocalDate.now();

        ImmutableList<ExcelReadRule<?>> excelReadRules = ImmutableList.of(new CapitalPlanReadRule(sendDate),
                new ActualCapitalReadRule(sendDate));

        URI uriPath = ClassLoader.getSystemResource("").toURI();

        Path excelPath = Paths.get(uriPath).resolve("excel模板.xlsx");

        InputStream inputStream = Files.newInputStream(excelPath, StandardOpenOption.READ);

        SummaryFundsEvent fundsEvent = ExcelUtils.readExcel(inputStream, excelReadRules, this::convertEvent);

        System.out.println(fundsEvent);
    }

    private SummaryFundsEvent convertEvent(Context context){

        SummaryFundsEvent.SummaryFundsEventBuilder eventBuilder = SummaryFundsEvent.builder();

        context.<List<CapitalPlan>>getOrEmpty(CapitalPlan.class)
                .ifPresent(eventBuilder::capitalPlans);

        context.<List<ActualCapital>>getOrEmpty(ActualCapital.class)
                .ifPresent(eventBuilder::actualCapitals);

        return eventBuilder.build();
    }

}
