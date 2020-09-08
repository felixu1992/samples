package top.felixu.repository

import org.springframework.data.repository.PagingAndSortingRepository
import top.felixu.entity.Employee
import java.util.*

/**
 * @author felixu
 * @since 2020.09.08
 */
interface EmployeeRepository : PagingAndSortingRepository<Employee, Int> {

    fun findEmployeeByName(name: String): Optional<Employee>

    fun findEmployeeByWorkerNo(workNo: String): Optional<Employee>
}