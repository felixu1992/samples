package top.felixu.constants;

/**
 * @author felixu
 * @date 2019.07.23
 */
public interface DataSourceConstants {

    interface Property {
        String TYPE = "type";
        String DRIVER_CLASS_NAME = "driver-class-name";
        String URL = "url";
        String USERNAME = "username";
        String PASSWORD = "password";
    }

    interface Name {
        String DEFAULT_DATA_SOURCE = "ds0";
        String BEAN_NAME = "dataSource";
        String DEFAULT_TARGET_DATA_SOURCE = "defaultTargetDataSource";
        String TARGET_DATA_SOURCES = "targetDataSources";
    }

    interface Prefix {

        String PREFIX = "spring.datasource";
        String SUB_PREFIX = ".";
    }
}
