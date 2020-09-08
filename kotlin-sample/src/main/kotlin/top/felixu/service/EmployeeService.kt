package top.felixu.service

import org.springframework.stereotype.Service
import top.felixu.entity.Employee
import top.felixu.exception.BusinessException
import top.felixu.exception.ErrorCode
import top.felixu.repository.EmployeeRepository

/**
 * @author felixu
 * @since 2020.09.08
 */
@Service
class EmployeeService(private val employeeRepository: EmployeeRepository) {

    fun findEmployeeByName(name: String): Employee {
        val optional = employeeRepository.findEmployeeByName(name)
        if (!optional.isPresent)
            throw BusinessException(ErrorCode.EMPLOYEE_NOT_FOUND)
        return optional.get()
    }
}