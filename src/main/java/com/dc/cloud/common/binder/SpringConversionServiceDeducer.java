package com.dc.cloud.common.binder;/*
package com.aop.md.filter.resource;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpringConversionServiceDeducer {

    private final ApplicationContext applicationContext;

    SpringConversionServiceDeducer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ConversionService getConversionService() {
        try {
            return this.applicationContext.getBean(
                    ConfigurableApplicationContext.CONVERSION_SERVICE_BEAN_NAME,
                    ConversionService.class);
        } catch (NoSuchBeanDefinitionException ex) {
            return new SpringConversionServiceDeducer.Factory(this.applicationContext.getAutowireCapableBeanFactory())
                    .create();
        }
    }

    private static class Factory {

        @SuppressWarnings("rawtypes")
        private final List<Converter> converters;

        private final List<GenericConverter> genericConverters;

        Factory(BeanFactory beanFactory) {
            this.converters = beans(beanFactory, Converter.class,
                    ConfigurationPropertiesBinding.VALUE);
            this.genericConverters = beans(beanFactory, GenericConverter.class,
                    ConfigurationPropertiesBinding.VALUE);
        }

        private <T> List<T> beans(BeanFactory beanFactory, Class<T> type,
                                  String qualifier) {
            if (beanFactory instanceof ListableBeanFactory) {
                return beans(type, qualifier, (ListableBeanFactory) beanFactory);
            }
            return Collections.emptyList();
        }

        private <T> List<T> beans(Class<T> type, String qualifier,
                                  ListableBeanFactory beanFactory) {
            return new ArrayList<T>(BeanFactoryAnnotationUtils
                    .qualifiedBeansOfType(beanFactory, type, qualifier).values());
        }

        public ConversionService create() {
            if (this.converters.isEmpty() && this.genericConverters.isEmpty()) {
                return ApplicationConversionService.getSharedInstance();
            }
            ApplicationConversionService conversionService = new ApplicationConversionService();
            for (Converter<?, ?> converter : this.converters) {
                conversionService.addConverter(converter);
            }
            for (GenericConverter genericConverter : this.genericConverters) {
                conversionService.addConverter(genericConverter);
            }
            return conversionService;
        }

    }

}
*/
