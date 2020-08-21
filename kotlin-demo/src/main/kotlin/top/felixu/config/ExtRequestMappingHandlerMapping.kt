package top.felixu.config

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.stereotype.Component
import org.springframework.util.StringValueResolver
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.condition.RequestCondition
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import top.felixu.annotation.ApiVersion
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Method
import javax.annotation.Nullable

/**
 * @author felixu
 * @since 2020.08.21
 */
@Configuration
class RequestMappingHandlerMappingRegister : WebMvcRegistrations {
    override fun getRequestMappingHandlerMapping(): RequestMappingHandlerMapping {
        return ExtRequestMappingHandlerMapping()
    }
}

class ExtRequestMappingHandlerMapping : RequestMappingHandlerMapping() {

    override fun getCustomMethodCondition(method: Method): RequestCondition<*>? {
        return getCustomCondition(method)
    }

    override fun getCustomTypeCondition(handlerType: Class<*>): RequestCondition<*>? {
        return getCustomCondition(handlerType)
    }

    private fun getCustomCondition(element: AnnotatedElement): RequestCondition<*>? {
        val version = AnnotatedElementUtils.findMergedAnnotation(element, ApiVersion::class.java) ?: return null
        val key = resolveEmbeddedValuesInPatterns(arrayOf("\${version:}"))[0]
        return CustomRequestCondition(version = version.version, key = key)
    }

    private val config = RequestMappingInfo.BuilderConfiguration()

//    override fun createRequestMappingInfo(
//            requestMapping: RequestMapping, @Nullable customCondition: RequestCondition<*>?): RequestMappingInfo {
//        val builder = RequestMappingInfo
//                .paths(*resolveEmbeddedValuesInPatterns(requestMapping.path))
//                .methods(*requestMapping.method)
//                .params(*requestMapping.params)
//                .headers(*resolveEmbeddedValuesInPatterns(requestMapping.headers))
//                .consumes(*requestMapping.consumes)
//                .produces(*requestMapping.produces)
//                .mappingName(requestMapping.name)
//        if (customCondition != null) {
//            builder.customCondition(customCondition)
//        }
//        return builder.options(this.config).build()
//    }

    private val embeddedValueResolver: StringValueResolver? = null

    override fun getMappingForMethod(method: Method, handlerType: Class<*>): RequestMappingInfo? {
        var info = this.createRequestMappingInfo(method)
        if (info != null) {
            val typeInfo = this.createRequestMappingInfo(handlerType)
            if (typeInfo != null) {
                info = typeInfo.combine(info)
            }
            val prefix = getPathPrefix(handlerType)
            if (prefix != null) {
                info = RequestMappingInfo.paths(prefix).options(this.config).build().combine(info)
            }
        }
        return info
    }

    fun getPathPrefix(handlerType: Class<*>?): String? {
        for (entry in pathPrefixes.entries) {
            if (entry.value.test(handlerType)) {
                var prefix = entry.key
                if (embeddedValueResolver != null) {
                    prefix = embeddedValueResolver!!.resolveStringValue(prefix!!)
                }
                return prefix
            }
        }
        return null
    }

    private fun createRequestMappingInfo(element: AnnotatedElement): RequestMappingInfo? {
        val requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping::class.java)
        val condition = if (element is Class<*>) getCustomTypeCondition(element) else getCustomMethodCondition((element as Method))
        return requestMapping?.let { createRequestMappingInfo(it, condition, element) }
    }

    fun createRequestMappingInfo(
            requestMapping: RequestMapping, @Nullable customCondition: RequestCondition<*>?, element: AnnotatedElement): RequestMappingInfo {
        val apiVersion = AnnotatedElementUtils.findMergedAnnotation(element, ApiVersion::class.java)
        var versionHeader: Array<String>? = null
        if (apiVersion != null) {
            versionHeader = this.resolveEmbeddedValuesInPatterns(arrayOf("\${version:}=" + apiVersion.version))
        }
        var builder = RequestMappingInfo
                .paths(*resolveEmbeddedValuesInPatterns(requestMapping.path))
                .methods(*requestMapping.method)
                .params(*requestMapping.params)
                .consumes(*requestMapping.consumes)
                .produces(*requestMapping.produces)
                .mappingName(requestMapping.name)
        if (versionHeader != null) {
            builder = builder.headers(*requestMapping.headers, *versionHeader)
        } else {
            builder = builder.headers(*requestMapping.headers)
        }
        if (customCondition != null) {
            builder.customCondition(customCondition)
        }
        return builder.options(this.config).build()
    }
}
