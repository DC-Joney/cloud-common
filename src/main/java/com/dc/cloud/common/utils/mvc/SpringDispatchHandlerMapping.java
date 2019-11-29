package com.dc.cloud.common.utils.mvc;

import org.springframework.aop.support.AopUtils;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 扫描所有的Controller层,然后合并Controller曾和 Service层的Path
 * 见案例 com.dc.cloud.common.test.web.ServiceController
 *          com.dc.cloud.common.test.service.DemoService
 */
@Component
public class SpringDispatchHandlerMapping extends RequestMappingHandlerMapping {

    public SpringDispatchHandlerMapping() {
        setOrder(-100);
    }

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return AnnotatedElementUtils.hasAnnotation(beanType, ServiceMapping.class);
    }


    @Override
    protected void detectHandlerMethods(Object handler) {
        Class<?> handlerType = (handler instanceof String ?
                obtainApplicationContext().getType((String) handler) : handler.getClass());

        if (handlerType != null) {

            Class<?> userType = ClassUtils.getUserClass(handlerType);
            AnnotationAttributes attributes = AnnotatedElementUtils.findMergedAnnotationAttributes(handlerType, ServiceMapping.class.getName(),
                    false, true);

            if (attributes != null) {

                Class<?> mappingClass = attributes.getClass("mappingClass");

                if (mappingClass != null) {

                    Object handlerBean = obtainApplicationContext().getBean(mappingClass);

                    if (handlerBean != null) {

                        Class<?> beanType = ClassUtils.getUserClass(handlerBean);

                        Map<Method, RequestMappingInfo> methods = MethodIntrospector.selectMethods(beanType,
                                (MethodIntrospector.MetadataLookup<RequestMappingInfo>) method -> {
                                    try {
                                        return getMappingForMethod(method, handlerType);
                                    } catch (Throwable ex) {
                                        throw new IllegalStateException("Invalid mapping on handler class [" +
                                                beanType.getName() + "]: " + method, ex);
                                    }
                                });

                        if (logger.isTraceEnabled()) {
                            logger.trace(formatMappings(userType, methods));
                        }

                        methods.forEach((method, mapping) -> {
                            Method invocableMethod = AopUtils.selectInvocableMethod(method, beanType);
                            registerHandlerMethod(handlerBean, invocableMethod, mapping);
                        });
                    }
                }
            }
        }
    }


    private <T> String formatMappings(Class<?> userType, Map<Method, T> methods) {
        String formattedType = Arrays.stream(ClassUtils.getPackageName(userType).split("\\."))
                .map(p -> p.substring(0, 1))
                .collect(Collectors.joining(".", "", "." + userType.getSimpleName()));
        Function<Method, String> methodFormatter = method -> Arrays.stream(method.getParameterTypes())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(",", "(", ")"));
        return methods.entrySet().stream()
                .map(e -> {
                    Method method = e.getKey();
                    return e.getValue() + ": " + method.getName() + methodFormatter.apply(method);
                })
                .collect(Collectors.joining("\n\t", "\n\t" + formattedType + ":" + "\n\t", ""));
    }

}
