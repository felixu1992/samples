//package top.felixu.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import top.felixu.entity.DataSourceEntity;
//import top.felixu.properties.DynamicDataSourceProperties;
//
///**
// * @author felixu
// * @date 2019.07.23
// */
//@Configuration
//public class TestConfig {
//
//    @Autowired
//    private DynamicDataSourceProperties dynamicDataSourceProperties;
//
//    @Bean
//    public DataSourceEntity dataSourceEntity() {
//        return dynamicDataSourceProperties.getDatasource().get(0);
//    }
//}
