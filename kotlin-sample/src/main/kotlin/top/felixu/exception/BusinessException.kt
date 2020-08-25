package top.felixu.exception

/**
 * @author felixu
 * @since 2020.08.13
 */
class BusinessException(val error: ErrorCode, params: Array<Any>?) : BaseException(error.code, error.msg, params) {

    constructor(error: ErrorCode) : this(error, null)
}