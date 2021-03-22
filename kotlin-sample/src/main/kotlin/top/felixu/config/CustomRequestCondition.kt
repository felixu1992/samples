package top.felixu.config

import org.springframework.web.servlet.mvc.condition.AbstractRequestCondition
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * @author felixu
 * @since 2020.08.21
 */
class CustomRequestCondition(val version: String, val key: String) : AbstractRequestCondition<CustomRequestCondition>() {

    private val patterns: Set<Any> = setOf(key, version)

    private val defaultVersion: String = "1.0"

    override fun getMatchingCondition(request: HttpServletRequest): CustomRequestCondition? {
        if (version == request.getHeader(key) || request.getHeader(key) == null)
            return this
        return null
    }

    override fun compareTo(other: CustomRequestCondition, request: HttpServletRequest): Int {
        return other.version.compareTo(request.getHeader(key) ?: defaultVersion)
    }

    override fun combine(other: CustomRequestCondition): CustomRequestCondition {
        return other
    }

    override fun getContent(): Collection<*> {
        return patterns
    }

    override fun getToStringInfix(): String {
        return "||"
    }
}