package top.felixu.swagger

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author felixu
 * @since 2020.08.19
 */
@ConfigurationProperties(prefix = "swagger")
class SwaggerProperties(val enabled: Boolean = true,
                        val basePackage: String = "top.felixu.*.controller",
                        val description: String = "",
                        val version: String = "",
                        val license: String = "",
                        val licenseUrl: String = "",
                        val termsOfServiceUrl: String = "",
                        val contact: Contact = Contact()) {
    var title: String? = null
        get() = field
        set(title) {
            field = title
        }
}

class Contact {
    var name: String? = null
        get() = field
        set(name) {
            field = name
        }
    var url: String = ""
        get() = field
        set(url) {
            field = url
        }
    var email: String = ""
        get() = field
        set(email) {
            field = email
        }
}