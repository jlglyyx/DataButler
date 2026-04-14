package com.yang.module.user.config

import com.alipay.api.AlipayClient
import com.alipay.api.AlipayConfig
import com.alipay.api.DefaultAlipayClient
import com.yang.module.user.config.AlipayManager.Companion.ALIPAY_APPID
import com.yang.module.user.config.AlipayManager.Companion.ALIPAY_CHARSET
import com.yang.module.user.config.AlipayManager.Companion.ALIPAY_CONTENT_TYPE
import com.yang.module.user.config.AlipayManager.Companion.ALIPAY_PRIVATE_KEY
import com.yang.module.user.config.AlipayManager.Companion.ALIPAY_PUBLIC_KEY
import com.yang.module.user.config.AlipayManager.Companion.ALIPAY_WEB_WAY
import com.yang.module.user.config.AlipayManager.Companion.SIGN_TYPE
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AlipayBeanConfig {

    @Bean
    fun alipayClient(): AlipayClient {
        val alipayConfig = AlipayConfig()
        alipayConfig.serverUrl = ALIPAY_WEB_WAY
        alipayConfig.appId = ALIPAY_APPID
        alipayConfig.privateKey = ALIPAY_PRIVATE_KEY
        alipayConfig.format = ALIPAY_CONTENT_TYPE
        alipayConfig.alipayPublicKey = ALIPAY_PUBLIC_KEY
        alipayConfig.charset = ALIPAY_CHARSET
        alipayConfig.signType = SIGN_TYPE
        // 返回单例的 AlipayClient
        return DefaultAlipayClient(
            alipayConfig
        ).apply { setEncryptKey("fDjS4+6vWbLFqxsT4j9PpA==") }
    }
}