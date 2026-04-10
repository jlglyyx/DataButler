package com.yang.module.user

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@MapperScan("com.yang.module.user.mapper")
@ComponentScan(basePackages = ["com.yang.lib.common", "com.yang.module.user"])
class ModuleUserApplication

fun main(args: Array<String>) {
    runApplication<ModuleUserApplication>(*args)
}
