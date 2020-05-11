package top.felixu.sqlite;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("top.felixu.sqlite.mapper")
public class SqliteDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SqliteDemoApplication.class, args);
    }

}
