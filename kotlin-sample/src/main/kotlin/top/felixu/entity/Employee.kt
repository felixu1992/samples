package top.felixu.entity

import javax.persistence.*
import javax.validation.constraints.NotBlank

/**
 * @author felixu
 * @since 2020.09.08
 */
@Entity
data class Employee(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 1,

    @Column(nullable = false)
    @field: NotBlank
    var name: String = "",

    @field: Column(nullable = false)
    @field: NotBlank
    var workerNo: String = ""
)