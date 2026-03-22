package com.yang.module.user

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
    @MapperScan("com.yang.module.user.mapper")
class ModuleUserApplication

fun main(args: Array<String>) {
    runApplication<ModuleUserApplication>(*args)
}
