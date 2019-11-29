package com.dc.cloud.common.excel.validate;

import com.dc.cloud.common.utils.LocalContext;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class SQLCheckValidator implements ConstraintValidator<ValidateSql, String> {

    private ValidateSql validateSql;

    @Override
    public void initialize(ValidateSql validateSql) {
        this.validateSql = validateSql;
    }


    @Override
    public boolean isValid(String targetValue, ConstraintValidatorContext context) {

        ValidateSqlExpressContext expressContext = LocalContext.getValidateContext();

        if (!StringUtils.hasText(validateSql.condition())) {
            throw new RuntimeException("The condition must not null");
        }

        if (validateSql.isNull() && !StringUtils.hasText(targetValue)) {
            return true;
        }

        AnnotatedElementKey fieldKey = new AnnotatedElementKey(targetValue.getClass(), validateSql.targetClass());

//        context.disableDefaultConstraintViolation();

//        context.buildConstraintViolationWithTemplate("{}").addPropertyNode();

        return expressContext.condition(validateSql.condition(), targetValue, fieldKey);
    }

}
