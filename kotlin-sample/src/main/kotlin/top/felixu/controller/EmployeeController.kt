package top.felixu.controller

import org.springframework.data.domain.Page
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
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
        return RespDTO.onSuc(employeeService.findEmployeeByName("%$name%"))
    }

    @PostMapping
    fun addEmployee(@RequestBody @Validated employee: Employee): RespDTO<Any> {
        employeeService.addEmployee(employee)
        return RespDTO.onSuc()
    }

    @GetMapping("/page")
    fun page(): RespDTO<Page<Employee>> {
        return RespDTO.onSuc(employeeService.findAll())
    }
}