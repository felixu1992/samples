package top.felixu.dynamic;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import top.felixu.entity.DataSourceEntity;
import top.felixu.ext.RelaxedDataBinder;
import top.felixu.ext.RelaxedPropertyResolver;
import top.felixu.properties.DynamicDataSourceProperties;
import top.felixu.utils.ApplicationUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 多数据源注册
 *
 * @author felixu
 * @date 2019.07.23
 */
public class DynamicDataSourceRegister {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceRegister.class);

    private ConversionService conversionService = new DefaultConversionService();

    private PropertyValues dataSourcePropertyValues;

    /**
     * 数据源
     */
    private DataSource defaultDataSource;

    /**
     * 自定义数据源
     */
    private Map<String, DataSource> customDataSources = new HashMap<>();

    public DynamicDataSourceRegister() {
        registerBeanDefinitions();
    }

    public void registerBeanDefinitions() {
        Environment env = ApplicationUtils.getBean(Environment.class);
        initDefaultDataSource(env);
        initCustomDataSources(env);
        Map<Object, Object> targetDataSources = new HashMap<>();
        // 主数据源
        targetDataSources.put("dataSource", defaultDataSource);
        // 自定义数据源
        targetDataSources.putAll(customDataSources);
        DynamicDataSourceContextHolder.DATA_SOURCE_IDS.add("dataSource");
        DynamicDataSourceContextHolder.DATA_SOURCE_IDS.addAll(customDataSources.keySet());

        // 创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) ApplicationUtils.getContext();
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) context.getBeanFactory();
        factory.removeBeanDefinition("dataSource");
        factory.registerBeanDefinition("dataSource", beanDefinition);
        logger.info("Dynamic DataSource Registry");
    }


    /**
     * 初始化默认数据源
     *
     * @param env 当前运行环境
     */
    private void initDefaultDataSource(Environment env) {
        // 读取主数据源
        RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(env, "spring.datasource.");
        DataSourceEntity entity = new DataSourceEntity();
        entity.setDriverClassName(resolver.getProperty("driver-class-name"));
        entity.setUsername(resolver.getProperty("username"));
        entity.setPassword(resolver.getProperty("password"));
        entity.setType(resolver.getProperty("type"));
        entity.setUrl(resolver.getProperty("url"));
        entity.setId("dataSource");
        defaultDataSource = buildDataSource(entity);
        bind(defaultDataSource, env);
    }

    /*------------------------------------------------------------------*/


    /**
     * 初始化自定义数据源
     *
     * @param env 当前运行环境
     */
    private void initCustomDataSources(Environment env) {
        DynamicDataSourceProperties dynamicDataSourceProperties = ApplicationUtils.getBean(DynamicDataSourceProperties.class);
        Optional.ofNullable(dynamicDataSourceProperties)
                .ifPresent(properties -> properties.getDatasource().forEach(p -> doRegister(env, p)));
    }

    public void hook(DataSourceEntity properties) {
        doRegister(ApplicationUtils.getBean(Environment.class), properties);
        DynamicDataSource dynamicDataSource = (DynamicDataSource) ApplicationUtils.getBean(DataSource.class);
        Map<Object, Object> targetDataSources = new HashMap<>();
        // 主数据源
        targetDataSources.put("dataSource", defaultDataSource);
        // 自定义数据源
        targetDataSources.putAll(customDataSources);
        DynamicDataSourceContextHolder.DATA_SOURCE_IDS.add("dataSource");
        DynamicDataSourceContextHolder.DATA_SOURCE_IDS.addAll(customDataSources.keySet());
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.afterPropertiesSet();
    }

    public void doRegister(Environment env, DataSourceEntity properties) {
        DataSource dataSource = buildDataSource(properties);
        customDataSources.put(properties.getId(), dataSource);
        bind(dataSource, env);
    }

    public DataSource buildDataSource(DataSourceEntity properties) {
        return fillDruidDataSource(properties);
    }

    public DataSource fillDruidDataSource(DataSourceEntity properties) {
        DataSource defaultDataSource = ApplicationUtils.getBean(DataSource.class);
        if (defaultDataSource instanceof DynamicDataSource) {
            DynamicDataSource dynamicDataSource = (DynamicDataSource) defaultDataSource;
            defaultDataSource = dynamicDataSource.determineTargetDataSource();
        }
        DruidDataSource dataSource = new DruidDataSource();
        BeanUtils.copyProperties(defaultDataSource, dataSource);
        dataSource.setDriverClassName(properties.getDriverClassName());
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        return dataSource;
    }

    /**
     * 向当前环境中绑定数据源
     *
     * @param dataSource 需要被绑定的数据源
     * @param env 当前运行环境
     */
    private void bind(DataSource dataSource, Environment env){
        RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSource);
//        dataBinder.setValidator(new LocalValidatorFactory().run(this.applicationContext));
        dataBinder.setConversionService(conversionService);
        dataBinder.setIgnoreNestedProperties(false);
        dataBinder.setIgnoreInvalidFields(false);
        dataBinder.setIgnoreUnknownFields(true);
        if(dataSourcePropertyValues == null){
            Map<String, Object> rpr = new RelaxedPropertyResolver(env, "spring.datasource").getSubProperties(".");
            Map<String, Object> values = new HashMap<>(rpr);
            // 排除已经设置的属性
            values.remove("type");
            values.remove("driver-class-name");
            values.remove("url");
            values.remove("username");
            values.remove("password");
            dataSourcePropertyValues = new MutablePropertyValues(values);
        }
        dataBinder.bind(dataSourcePropertyValues);
    }
}
