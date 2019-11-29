package com.dc.cloud.common.utils;

import com.alibaba.excel.annotation.ExcelProperty;
import com.dc.cloud.common.excel.validate.ExcelValidatorFactory;
import com.dc.cloud.common.excel.validate.ValidateSqlExpressContext;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 缓存、校验、EventBus管理
 */
public class LocalContext implements InitializingBean, ApplicationContextAware {

    @Getter
    private static volatile ExcelValidatorFactory validatorFactory;

    @Getter
    private static volatile LoadingCache<Class<?>, Set<String>> excelClassCache;

    private ApplicationContext applicationContext;

    @Getter
    private static AsyncTaskExecutor excelExecutor;

    @Getter
    private static ValidateSqlExpressContext validateContext;

    private SpelParserConfiguration configuration;


    @Autowired
    private EventBusPool eventBusPool;

    private LocalContext() {
    }

    @Override
    public void afterPropertiesSet() {
        excelClassCache = CacheBuilder.newBuilder()
                .maximumSize(100).initialCapacity(10)
                .softValues().weakKeys()
                .build(new ExcelCacheLoader());
        LocalContext.validatorFactory = new ExcelValidatorFactory(applicationContext);
        excelExecutor = buildTaskExecutor();

        this.configuration = new SpelParserConfiguration(SpelCompilerMode.OFF, ClassUtils.getDefaultClassLoader());

        //init Spel context
        validateContext = new ValidateSqlExpressContext(new SpelExpressionParser(configuration));
        applicationContext.getAutowireCapableBeanFactory().initializeBean(validateContext, validateContext.getClass().getSimpleName());
    }

    private ThreadPoolTaskExecutor buildTaskExecutor() {
        int processor = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setAllowCoreThreadTimeOut(false);
        taskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        taskExecutor.setMaxPoolSize(processor * 2);
        taskExecutor.setAwaitTerminationSeconds(60);
        taskExecutor.setQueueCapacity(500);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setKeepAliveSeconds(60);
        taskExecutor.setDaemon(false);
        taskExecutor.setThreadGroup(Thread.currentThread().getThreadGroup());
        taskExecutor.setThreadNamePrefix("handle-excel-");
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    public static EventBusPool getEventBusPool() {
        return shardInstance().eventBusPool;
    }


    public static LocalContext shardInstance() {
        return LocalContextHolder.INSTANCE;
    }

    private static class LocalContextHolder {
        private static final LocalContext INSTANCE = new LocalContext();
    }


    //缓存excel表头对应的处理
    private static class ExcelCacheLoader extends CacheLoader<Class<?>, Set<String>> {

        @Override
        public Set<String> load(@ParametersAreNonnullByDefault Class<?> classKey) {

            HashSet<String> excelFileNames = Sets.newHashSet();

            ReflectionUtils.doWithFields(classKey, field -> {

                AnnotationAttributes attributes =
                        AnnotatedElementUtils.findMergedAnnotationAttributes(field, ExcelProperty.class,
                                false, true);

                String[] values;

                if (attributes != null && (values = attributes.getStringArray("value")) != null && values.length > 0) {

                    //合并字符串
                    // do something

                    Optional<String> first = Arrays.stream(values).filter(StringUtils::hasText).findFirst();
                    first.ifPresent(excelFileNames::add);

                }

            });
            return Collections.unmodifiableSet(excelFileNames);
        }
    }
}
