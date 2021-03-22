package top.felixu.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.felixu.annotation.ApiVersion
import top.felixu.dto.RespDTO
import top.felixu.exception.BusinessException
import top.felixu.exception.ErrorCode

/**
 * @author felixu
 * @since 2020.08.13
 */
@RestController
class TestController {

//    @GetMapping("/test")
//    @ApiVersion(version = "1.0")
//    fun test0() : RespDTO<String> {
//        return RespDTO.onSuc("调用版本 1")
//    }
//
//    @GetMapping("/test")
//    @ApiVersion(version = "2.0")
//    fun test1() : RespDTO<String> {
//        return RespDTO.onSuc("调用版本 2")
//    }
//
//    @GetMapping("/test1")
//    fun testWithoutVersion() : RespDTO<String> {
//        throw BusinessException(ErrorCode.PARAM_ERROR)
//    }

    @GetMapping(value = ["/test2"], headers = ["\${version:}=1.0"])
    fun test2(): RespDTO<String> {
        return RespDTO.onSuc("调用版本 1")
    }

    @GetMapping(value = ["/test2"], headers = ["\${version:}=2.0"])
    fun test3(): RespDTO<String> {
        return RespDTO.onSuc("调用版本 2")
    }
}