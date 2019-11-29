package com.dc.cloud.common.binder;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.bind.*;
import org.springframework.boot.context.properties.bind.handler.IgnoreTopLevelConverterNotFoundBindHandler;
import org.springframework.boot.context.properties.bind.handler.NoUnboundElementsBindHandler;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.context.properties.source.UnboundElementsSourceFilter;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.*;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.function.SingletonSupplier;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;


// EnableConfigurationPropertiesImportSelector ，
// ConfigurationPropertiesBindingPostProcessorRegistrar   ，
// ConfigurationPropertiesBindingPostProcessor


/**
 * yml 文件自动绑定
 */

@Log4j2
@Component
public class SpringResourceLoader implements ResourceLoaderAware, InitializingBean,
        ApplicationContextAware, EnvironmentAware {

    private static final String DEFAULT_CLASSPATH_PREFIX = "classpath:";

    private Set<PropertySourceLoader> loaderCache = new CopyOnWriteArraySet<>();

    private SingletonSupplier<PropertySourceLoader> yamlSourceLoader = SingletonSupplier.of(YamlPropertySourceLoader::new);
    private SingletonSupplier<PropertySourceLoader> propertiesPropertySourceLoader = SingletonSupplier.of(PropertiesPropertySourceLoader::new);

    private ResourceLoader resourceLoader;

    private ApplicationContext applicationContext;

    private ConfigurableEnvironment environment;


    private Iterator<PropertySource<?>> propertySources;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        List<PropertySourceLoader> sourceLoaders = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class, ClassUtils.getDefaultClassLoader());
        loaderCache.addAll(sourceLoaders);
        if(loaderCache.size() <= 0){
            loaderCache.add(yamlSourceLoader.get());
            loaderCache.add(propertiesPropertySourceLoader.get());
        }
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    @Override
    public void setEnvironment(Environment environment) {
        Assert.isInstanceOf(ConfigurableEnvironment.class,environment,"The environment must be instance of ConfigurableEnvironment");
        this.environment = (ConfigurableEnvironment) environment;
        this.propertySources  = this.environment.getPropertySources().iterator();
    }


    public <T> BindResult<T> bindSpringProperty(Class<T> propertyClass, String prefix){
        return Binder.get(environment)
                .bind(prefix, propertyClass);
    }


    public <T> BindResult<T> bindSpringProperty(T instance, String prefix){
        return Binder.get(environment)
                .bind(prefix, Bindable.ofInstance(instance));
    }



    @SuppressWarnings("unchecked")
    public <T> BindResult<T> bindProperty(String resourceLocation,T instance, String prefix){
        return bindProperty(resourceLocation,(Class<T>) instance.getClass(), prefix, instance);
    }


    public <T> BindResult<T> bindProperty(String resourceLocation,Class<T> propertyClass,String prefix){
        return bindProperty(resourceLocation,propertyClass ,prefix,null);
    }

    public <T> BindResult<T> bindProperty(String resourceLocation, Class<T> clazz, String prefix, T instance){

        try {

            if(resourceLocation.startsWith(DEFAULT_CLASSPATH_PREFIX)){
                throw new IllegalAccessException("The resource location can not start with : " + DEFAULT_CLASSPATH_PREFIX);
            }

            Resource resource = resourceLoader.getResource(DEFAULT_CLASSPATH_PREFIX + resourceLocation);

            for (PropertySourceLoader sourceLoader : loaderCache) {

                if(canLoadFileExtension(sourceLoader,resourceLocation)){

                    List<PropertySource<?>> loaderSource = sourceLoader.load(resourceLocation, resource);

                    if(loaderSource.size() > 0 ){
                        Binder binder = new Binder(getConfigurationPropertySources(loaderSource),
                                getPropertySourcesPlaceholdersResolver(loaderSource), getConversionService(),
                                getPropertyEditorInitializer());

                        BindHandler handler = new IgnoreTopLevelConverterNotFoundBindHandler();

                        UnboundElementsSourceFilter filter = new UnboundElementsSourceFilter();

                        BindHandler noUnboundElementsBindHandler = new NoUnboundElementsBindHandler(handler, filter);

                        Bindable<T> bindAble = Bindable.of(ResolvableType.forClass(clazz));

                        if(instance != null){
                            bindAble.withExistingValue(instance);
                        }

                        return binder.bind(prefix, bindAble , noUnboundElementsBindHandler);
                    }

//                    loaderSource.stream()
//                            .filter(Objects::nonNull)
//                            .map(source-> (Map<String,Object>)source.getSource())
//                            .forEach(propertyMap::putAll);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }



    private Iterable<ConfigurationPropertySource> getConfigurationPropertySources(Iterable<PropertySource<?>> propertySources) {
        return ConfigurationPropertySources.from(propertySources);
    }

    private PropertySourcesPlaceholdersResolver getPropertySourcesPlaceholdersResolver(Iterable<PropertySource<?>> propertySources) {
        return new PropertySourcesPlaceholdersResolver(propertySources);
    }

    private ConversionService getConversionService() {
        return ApplicationConversionService.getSharedInstance();
    }

    private Consumer<PropertyEditorRegistry> getPropertyEditorInitializer() {
        if (this.applicationContext instanceof ConfigurableApplicationContext) {
            return ((ConfigurableApplicationContext) this.applicationContext)
                    .getBeanFactory()::copyRegisteredEditorsTo;
        }
        return null;
    }


    private boolean canLoadFileExtension(PropertySourceLoader loader, String name) {
        return Arrays.stream(loader.getFileExtensions())
                .anyMatch((fileExtension) -> StringUtils.endsWithIgnoreCase(name,
                        fileExtension));
    }

}
