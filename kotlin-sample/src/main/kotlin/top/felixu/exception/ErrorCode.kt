package top.felixu.exception

/**
 * @author felixu
 * @since 2020.08.13
 */
enum class ErrorCode(val code: Int, val msg: String) {
    OK(1, "success"),
    FAIL(-1, "unknown.error"),
    PARAM_ERROR(100, "param.error")
}