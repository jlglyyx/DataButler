package com.yang.module.user.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {

        val userDir = System.getProperty("user.dir")

        println("userDir: $userDir")

        registry.addResourceHandler("/html/**")
            .addResourceLocations("file:$userDir/html/")
    }
}