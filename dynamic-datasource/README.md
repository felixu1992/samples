# 快速开始

基于`Spring Boot 2.x`实现的动态数据源`demo`，具备以下功能：

1. 多数据源加载
2. 动态添加数据源
3. 通过注解指定数据源
4. 拦截自定义切换规则

## 初始化数据库

1. 自行创建多个测试库
2. 执行`test.sql`文件
3. 自行随便插入几条测试数据

## 设计实现

### 载入多数据源功能

- 采用`Enable`模式，需要在启动类上使用`@EnableDynamicDataSource`以开启多数据源功能，否则不会被加载。

### 多数据源实现

- 核心实现位于`dynamic`包下
- `DynamicDataSource`：继承自`AbstractRoutingDataSource`类，此类是`Spring JDBC`实现动态数据源的核心，类注释上有相关介绍，且说明通常使用线程绑定事件在`getConnection`时通过获取对应`key`去完成切换，当下也采用此实现方式。
- `DynamicDataSourceContextHolder`：用于存储线程本地变量，即需要被绑定`DataSource`的`key`，或者称之为`DataSource`的`id`，也存储了所有数据源的`id`。
- `DynamicDataSourceAspect`：`AOP`切入配置，通过拦截`@TargetDataSource`注解，从而实现自定义的切换规则。
- `DynamicDataSourceRegister`：核心注册多数据功能，通过`@EnableDynamicDataSource`被加载时的`@ImportAutoConfiguration`来载入此类，此类被初始化时，会触发无参构造中相关操作，会首先加载默认数据源(即主数据源)，然后加载其他数据源，然后构造`DynamicDataSource`作为`IOC`容器中的`dataSource`，此时使用`@TargetDataSource`便可以实现切换数据源功能了。

### 动态增加数据源

```java
/**
 * 可以被其他地方调用动态加载数据源的勾子方法
 *
 * @param properties 数据源配置实体
 */
public void hook(DataSourceEntity properties) {
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
```

具体实现代码如上，想法是，当该方法可以被调用时，当前环境中存在的比然是`DynamicDataSource`作为`dataSource`的，首先需要将当前被添加的数据源注册到`DynamicDataSourceContextHolder`中，以及构建相应的`DataSource(当前实现其实是DruidDataSource)`作为`targetDataSource`，其次获取当前环境中的`DynamicDataSource`，为其添加`targetDataSources`，同时刷入到`resolvedDataSources`中，即可完成新数据源的添加。

其原理是创建数据库连接前会通过`DynamicDataSource#determineCurrentLookupKey`去查找当前线程上下文要使用的`dataSource`的`key/id`，然后根据对应`key`在`AbstractRoutingDataSource#resolvedDataSources`中查找对应数据源，如果数据源存在，则使用该数据源，否则使用`AbstractRoutingDataSource#defaultTargetDataSource`作为数据源，这里的默认数据源使用的是主数据。

### 注解

- `@EnableDynamicDataSource`：用于开启多数据源功能。
- `@TargetDataSource`：指定该方法使用多数据源，`name`为数据源`id`，当下实现是，如果该值设置，则强制使用该值对应数据源，不存在则使用默认数据源，如果该值不设置，则需要在`DynamicDataSourceAspect#dataSourceName`中自行实现切换规则，因为需要定制切换规则，完全可以重新实现`DynamicDataSourceAspect#dataSourceName`即可。

## 说明

这里主要针对`ext`包，即为拓展包，其来源于`Spring Boot 1.x`版本，在`Spring Boot 1.x`版本中存在一个`bind`包，其包下类的主要功能用于操作配置文件，获取相应属性的，在`Spring Boot 2.x`版本中则完全移除了该包，可以通过`Environment`类等其他相关操作来获取配置文件的相关属性，这里这个包下的文件完全来源于`Spring Boot 1.5.9`版本，主要是懒。。。

