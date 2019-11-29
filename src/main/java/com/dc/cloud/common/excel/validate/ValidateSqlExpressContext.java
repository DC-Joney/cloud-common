package com.dc.cloud.common.excel.validate;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ValidateSqlExpressContext extends CachedExpressionEvaluator implements BeanFactoryAware {

    private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(64);

    private BeanFactory beanFactory;

    private SpelExpressionParser expressionParser;

    public ValidateSqlExpressContext(SpelExpressionParser parser) {
        super(parser);
        this.expressionParser = parser;
    }

    public boolean condition(String conditionExpression, Object target,
                             AnnotatedElementKey fieldKey) {

        StandardEvaluationContext evaluationContext = new StandardEvaluationContext(target);

        if (beanFactory != null) {
            evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }

        return (Boolean.TRUE.equals(getExpression(this.conditionCache, fieldKey, conditionExpression).getValue(
                evaluationContext, Boolean.class)));
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
