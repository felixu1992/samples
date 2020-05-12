package top.felixu.sqlite.properties;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author felixu
 * @since 2020.05.12
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sqlite")
public class SqliteProperties {

    private long timeout = 1;

    private TimeUnit timeUnit = TimeUnit.SECONDS;
}
