package top.felixu.dto

import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import top.felixu.exception.ErrorCode
import java.io.Serializable
import java.util.stream.Collectors

/**
 * @author felixu
 * @since 2020.08.13
 */
data class RespDTO<T>(val code: Int, val message: String, val data: T?) : Serializable {
    companion object {

        fun <T> onSuc(): RespDTO<T> {
            return onSuc(null)
        }

        fun <T> onSuc(data: T?): RespDTO<T> {
            return build(ErrorCode.OK.ordinal, ErrorCode.OK.name, data)
        }

        fun <T> onFail(errorCode: ErrorCode): RespDTO<T> {
            return onFail(errorCode.code, errorCode.msg)
        }

        fun <T> onFail(code: Int, msg: String): RespDTO<T> {
            return build(code, msg, null)
        }

        fun <T> onValidFail(result: BindingResult): RespDTO<T>? {
            val errorMsg = result.allErrors
                    .stream()
                    .map { objectError: ObjectError ->
                        val error = objectError as FieldError
                        error.field + ", " + error.defaultMessage
                    }
                    .collect(Collectors.joining("\n"))
            return build(ErrorCode.PARAM_ERROR.code, errorMsg, null)
        }

        private fun <T> build(ret: Int, msg: String, data: T?): RespDTO<T> {
            return RespDTO(ret, msg, data)
        }
    }
}