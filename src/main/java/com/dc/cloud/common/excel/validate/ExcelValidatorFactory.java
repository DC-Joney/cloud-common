package com.dc.cloud.common.excel.validate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Slf4j
public class ExcelValidatorFactory extends LocalValidatorFactoryBean {

    public ExcelValidatorFactory(ApplicationContext applicationContext) {
        setApplicationContext(applicationContext);
        setMessageInterpolator(new MessageInterpolatorFactory().getObject());
        afterPropertiesSet();
    }


    @Override
    public boolean supports(Class<?> type) {
        if (!super.supports(type)) {
            return false;
        }
        if (AnnotatedElementUtils.hasAnnotation(type, ExcelValidated.class)) {
            return true;
        }
        if (getConstraintsForClass(type).isBeanConstrained()) {
            log.warn("The @ConfigurationProperties bean " + type
                    + " contains validation constraints but had not been annotated "
                    + "with @Validated.");
        }
        return true;
    }


}
