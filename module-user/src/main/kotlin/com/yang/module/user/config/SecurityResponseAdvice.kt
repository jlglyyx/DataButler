package com.yang.module.user.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.yang.lib.common.util.HttpDecryptServer
import com.yang.lib.common.util.toJson
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@ControllerAdvice
class SecurityResponseAdvice : ResponseBodyAdvice<Any> {

    override fun supports(returnType: org.springframework.core.MethodParameter, converterType: Class<out org.springframework.http.converter.HttpMessageConverter<*>>): Boolean = true

    override fun beforeBodyWrite(body: Any?, returnType: org.springframework.core.MethodParameter, selectedContentType: org.springframework.http.MediaType, selectedConverterType: Class<out org.springframework.http.converter.HttpMessageConverter<*>>, request: org.springframework.http.server.ServerHttpRequest, response: org.springframework.http.server.ServerHttpResponse): Any? {
        val servletRequest = (request as org.springframework.http.server.ServletServerHttpRequest).servletRequest
        val sessionKey = servletRequest.getAttribute("SESSION_KEY") as? ByteArray

        return if (sessionKey != null && body != null) {
            val cipherText = HttpDecryptServer.aesEncrypt(body.toString(), sessionKey)
            mapOf("data" to cipherText).toJson()
        } else {
            body
        }
    }
}