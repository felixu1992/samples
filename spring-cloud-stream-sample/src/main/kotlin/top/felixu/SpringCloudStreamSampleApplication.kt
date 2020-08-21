package top.felixu

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringCloudStreamSampleApplication

fun main(args: Array<String>) {
    runApplication<SpringCloudStreamSampleApplication>(*args)
}
