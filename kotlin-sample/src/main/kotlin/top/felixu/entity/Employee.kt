package top.felixu.entity

import javax.persistence.*

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
    var name: String = "",

    @field: Column(nullable = false)
    var workerNo: String = ""
)