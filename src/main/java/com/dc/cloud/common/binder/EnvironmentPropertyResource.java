package com.dc.cloud.common.binder;


import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;


//@Component
public class EnvironmentPropertyResource implements EnvironmentAware {

    private ConfigurableEnvironment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
        MutablePropertySources propertySources = ((ConfigurableEnvironment) environment).getPropertySources();
        propertySources.forEach(source->{
            System.out.println(source.getName());
            System.out.println(source.getSource());
        });

    }




}
