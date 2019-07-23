package top.felixu.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import top.felixu.entity.DataSourceEntity;

import java.util.List;

/**
 * @author felixu
 * @date 2019.07.23
 */
@Primary
@Component
@ConfigurationProperties(prefix = "test")
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
public class DynamicDataSourceProperties {

    private List<DataSourceEntity> datasource;

    public List<DataSourceEntity> getDatasource() {
        return datasource;
    }

    public void setDatasource(List<DataSourceEntity> datasource) {
        this.datasource = datasource;
    }
}
