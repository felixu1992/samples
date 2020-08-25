package top.felixu.swagger

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j
import com.google.common.collect.Sets
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import springfox.documentation.swagger2.web.Swagger2Controller

/**
 * @author felixu
 * @since 2020.08.19
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
//@Import(BeanValidatorPluginsConfiguration::class)
@EnableConfigurationProperties(SwaggerProperties::class)
@ConditionalOnClass(Docket::class, Swagger2Controller::class, SwaggerProperties::class)
class SwaggerAutoConfiguration(val properties: SwaggerProperties) {

    @Bean
    fun api(): Docket? {
        return Docket(DocumentationType.SWAGGER_2)
                .produces(Sets.newHashSet("application/json"))
                .consumes(Sets.newHashSet("application/json"))
                .protocols(Sets.newHashSet("http", "https"))
                .apiInfo(apiInfo(properties))
                .forCodeGeneration(true)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage(properties.basePackage))
                .paths(PathSelectors.any())
                .build()
    }

    private fun apiInfo(properties: SwaggerProperties): ApiInfo? {
        return ApiInfoBuilder()
                .title(properties.title)
                .description(properties.description)
                .license(properties.license)
                .licenseUrl(properties.licenseUrl)
                .termsOfServiceUrl(properties.termsOfServiceUrl)
                .contact(Contact(properties.contact.name,
                        properties.contact.url, properties.contact.email))
                .version(properties.version)
                .build()
    }
}