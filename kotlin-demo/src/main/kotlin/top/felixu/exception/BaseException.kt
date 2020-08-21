package top.felixu.exception

import java.lang.RuntimeException

/**
 * @author felixu
 * @since 2020.08.19
 */
open class BaseException(val code: Int, val msg: String, val args: Array<Any>?) : RuntimeException(msg) {

}