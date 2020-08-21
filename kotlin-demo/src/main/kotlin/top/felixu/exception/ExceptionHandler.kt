package top.felixu.exception

import com.google.common.base.MoreObjects
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import top.felixu.dto.RespDTO

/**
 * @author felixu
 * @since 2020.08.13
 */
@RestControllerAdvice
class ExceptionHandler(val messageSource: MessageSource) {

    @ExceptionHandler(BaseException::class)
    fun handleException(e: Exception): ResponseEntity<RespDTO<Any>> {
        val exception: BaseException = e as BaseException
        val message: String = MoreObjects.firstNonNull(null, messageSource.getMessage(exception.msg, exception.args, exception.msg, LocaleContextHolder.getLocale()))
        return ResponseEntity(RespDTO.onFail(exception.code, message), HttpStatus.OK)
    }
}

