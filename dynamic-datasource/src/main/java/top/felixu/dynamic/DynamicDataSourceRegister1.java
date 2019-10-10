package top.felixu.dynamic;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ObjectUtils;
import top.felixu.constants.DataSourceConstants;
import top.felixu.entity.DataSourceEntity;
import top.felixu.ext.RelaxedDataBinder;
import top.felixu.ext.RelaxedPropertyResolver;
import top.felixu.properties.DynamicDataSourceProperties;
import top.felixu.utils.ApplicationUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 多数据源注册
 * 需要在启动类上加 {@link top.felixu.annotation.EnableDynamicDataSource} 来开启
 *
 * @author felixu
 * @date 2019.07.23
 * @see top.felixu.annotation.EnableDynamicDataSource Enable 模式加载动态数据源
 */
public class DynamicDataSourceRegister1 implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    /**
     * 类型转换器
     */
    private static ConversionService conversionService = new DefaultConversionService();

    /**
     * 对象属性存储
     */
    private static PropertyValues dataSourcePropertyValues;

    /**
     * 默认数据源
     */
    private static DataSource defaultDataSource;

    /**
     * 所有数据源
     */
    private static Map<Object, Object> targetDataSources = new ConcurrentHashMap<>();

    @Override
    public void setEnvironment(Environment env) {
        // 初始化默认数据源以及自定义数据源
        initDefaultDataSource(env);
//        initCustomDataSources(env);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        registerBeanDefinitions(registry);
    }

    /**
     * 可以被其他地方调用动态加载数据源的勾子方法
     *
     * @param properties 数据源配置实体
     */
    public static void hook(DataSourceEntity properties) {
        // 注册需要被加载的数据源
        doRegister(ApplicationUtils.getBean(Environment.class), properties);
        // 获取当前已经被加载过的动态数据源 DynamicDataSource
        DynamicDataSource dynamicDataSource = (DynamicDataSource) ApplicationUtils.getBean(DataSource.class);
        // 设置新增后的 targetDataSources 到 AbstractRoutingDataSource#targetDataSources中
        dynamicDataSource.setTargetDataSources(targetDataSources);
        // 将 AbstractRoutingDataSource#targetDataSources 所持有的 dataSource 刷到 AbstractRoutingDataSource#resolvedDataSources
        // 这一步操作是因为根据 id 取对应 DataSource 其实最终是取的 resolvedDataSources
        dynamicDataSource.afterPropertiesSet();
    }

    /**
     * 初始化默认数据源
     *
     * @param env 当前运行环境
     */
    private void initDefaultDataSource(Environment env) {
        // 获取主数据源配置
        RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(env,
                DataSourceConstants.Prefix.PREFIX + DataSourceConstants.Prefix.SUB_PREFIX);
        // 构建主数据源并绑定
        DataSourceEntity entity = new DataSourceEntity();
        entity.setType(resolver.getProperty(DataSourceConstants.Property.TYPE));
        entity.setUrl(resolver.getProperty(DataSourceConstants.Property.URL));
        entity.setUsername(resolver.getProperty(DataSourceConstants.Property.USERNAME));
        entity.setPassword(resolver.getProperty(DataSourceConstants.Property.PASSWORD));
        entity.setDriverClassName(resolver.getProperty(DataSourceConstants.Property.DRIVER_CLASS_NAME));
        entity.setId(DataSourceConstants.Name.DEFAULT_DATA_SOURCE);
        defaultDataSource = buildDataSource(entity);
        dataSourceHold(entity.getId(), defaultDataSource);
        bind(defaultDataSource, env);
    }

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

    /**
     * 注册动态数据源的 BeanDefinition
     */
    private void registerBeanDefinitions(BeanDefinitionRegistry registry) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue(DataSourceConstants.Name.DEFAULT_TARGET_DATA_SOURCE, defaultDataSource);
        mpv.addPropertyValue(DataSourceConstants.Name.TARGET_DATA_SOURCES, targetDataSources);
        registry.registerBeanDefinition(DataSourceConstants.Name.BEAN_NAME, beanDefinition);
    }

    /**
     * 通过数据源配置构建数据源
     * 被环境持有
     * 绑定
     *
     * @param env  当前运行环境
     * @param properties 数据源配置实体
     */
    private static void doRegister(Environment env, DataSourceEntity properties) {
        DataSource dataSource = buildDataSource(properties);
        dataSourceHold(properties.getId(), dataSource);
        bind(dataSource, env);
    }

    /**
     * 将当前数据源放到变量 targetDataSources 中
     * 并将 id 保存到 {@link DynamicDataSourceContextHolder#DATA_SOURCE_IDS} 中
     *
     * @param id 数据源 id
     * @param dataSource 数据源
     */
    private static void dataSourceHold(String id, DataSource dataSource) {
        targetDataSources.put(id, dataSource);
        DynamicDataSourceContextHolder.DATA_SOURCE_IDS.add(id);
    }

    /**
     * 构建数据源
     *
     * @param properties 数据源配置实体
     * @return 数据源
     */
    private static DataSource buildDataSource(DataSourceEntity properties) {
        // 获取环境中的数据源，初次加载为 DruidDataSource
        DataSource defaultDataSource;

        if (ObjectUtils.isEmpty(ApplicationUtils.getContext())) {
            defaultDataSource = new DruidDataSource();
        } else {
            defaultDataSource = ApplicationUtils.getBean(DataSource.class);
        }

        // 后期通过 hook 方法加载的时候为 DynamicDataSource
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
    private static void bind(DataSource dataSource, Environment env){
        RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSource);
//        dataBinder.setValidator(new LocalValidatorFactory().run(this.applicationContext));
        dataBinder.setConversionService(conversionService);
        dataBinder.setIgnoreNestedProperties(false);
        dataBinder.setIgnoreInvalidFields(false);
        dataBinder.setIgnoreUnknownFields(true);
        if(dataSourcePropertyValues == null){
            Map<String, Object> rpr = new RelaxedPropertyResolver(env, DataSourceConstants.Prefix.PREFIX)
                    .getSubProperties(DataSourceConstants.Prefix.SUB_PREFIX);
            Map<String, Object> values = new HashMap<>(rpr);
            // 排除已经设置的属性
            values.remove(DataSourceConstants.Property.TYPE);
            values.remove(DataSourceConstants.Property.DRIVER_CLASS_NAME);
            values.remove(DataSourceConstants.Property.URL);
            values.remove(DataSourceConstants.Property.USERNAME);
            values.remove(DataSourceConstants.Property.PASSWORD);
            dataSourcePropertyValues = new MutablePropertyValues(values);
        }
        dataBinder.bind(dataSourcePropertyValues);
    }
}
