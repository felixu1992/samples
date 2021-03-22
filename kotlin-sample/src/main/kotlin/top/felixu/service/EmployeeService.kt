package top.felixu.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
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
        val optional = employeeRepository.findEmployeeByNameLike(name)
        if (!optional.isPresent)
            throw BusinessException(ErrorCode.EMPLOYEE_NOT_FOUND)
        return optional.get()
    }

    fun addEmployee(employee: Employee) {
        employeeRepository.save(employee)
    }

    fun findAll(): Page<Employee> {
        return employeeRepository.findAll(PageRequest.of(1, 10))
    }
}