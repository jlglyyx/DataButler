package com.yang.lib.common.config

import com.baomidou.mybatisplus.annotation.DbType
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MybatisPlusConfig {

    /**
     * 添加分页插件
     */
    @Bean
    fun mybatisPlusInterceptor(): MybatisPlusInterceptor {
        val interceptor = MybatisPlusInterceptor()
        val paginationInnerInterceptor = PaginationInnerInterceptor(DbType.MYSQL)
        paginationInnerInterceptor.isOverflow = false
        interceptor.addInnerInterceptor(paginationInnerInterceptor)
        return interceptor
    }
}