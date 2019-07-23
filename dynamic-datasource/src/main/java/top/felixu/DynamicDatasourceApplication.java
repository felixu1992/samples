package top.felixu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.felixu.annotation.EnableDynamicDataSource;

@SpringBootApplication
@EnableDynamicDataSource
@MapperScan("top.felixu.mapper")
public class DynamicDatasourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicDatasourceApplication.class, args);
    }

}
