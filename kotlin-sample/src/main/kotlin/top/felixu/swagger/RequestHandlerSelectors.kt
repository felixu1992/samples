package top.felixu.swagger

import com.google.common.base.Optional
import com.google.common.base.Predicate
import com.google.common.base.Function
import springfox.documentation.RequestHandler
import java.util.regex.Pattern

/**
 * @author felixu
 * @since 2020.08.19
 */
class RequestHandlerSelectors {

    companion object {
        fun basePackage(basePackage: String): Predicate<RequestHandler> {
            return Predicate { input: RequestHandler? ->
                assert(input != null)
                declaringClass(input).transform(handlerPackage(basePackage)).or(true)
            }
        }

        private fun handlerPackage(basePackage: String): Function<Class<*>, Boolean> {
            return Function { input: Class<*>? ->
                assert(input != null)
                val inputPackageName = input!!.getPackage().name
                for (packageName in basePackage.split(",").toTypedArray()) {
                    var isMatch = inputPackageName.startsWith(packageName)
                    if (isMatch)
                        return@Function true
                    val pattern = Pattern.compile(packageName)
                    isMatch = pattern.matcher(inputPackageName).find()
                    if (isMatch)
                        return@Function true
                }
                false
            }
        }

        private fun declaringClass(input: RequestHandler?): Optional<out Class<*>> {
            return Optional.fromNullable(input!!.declaringClass());
        }
    }
}