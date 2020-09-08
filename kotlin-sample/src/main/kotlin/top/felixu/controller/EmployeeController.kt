package top.felixu.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import top.felixu.dto.RespDTO
import top.felixu.entity.Employee
import top.felixu.service.EmployeeService

/**
 * @author felixu
 * @since 2020.09.08
 */
@RestController
@RequestMapping("/employee")
class EmployeeController(val employeeService: EmployeeService) {

    @GetMapping("/{name}")
    fun findByName(@PathVariable name: String): RespDTO<Employee> {
        return RespDTO.onSuc(employeeService.findEmployeeByName(name))
    }
}