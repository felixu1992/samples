package top.felixu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import top.felixu.dynamic.DynamicDataSourceRegister1;

@SpringBootApplication
//@EnableDynamicDataSource
@MapperScan("top.felixu.mapper")
@Import(DynamicDataSourceRegister1.class)
public class DynamicDatasourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicDatasourceApplication.class, args);
    }

}
