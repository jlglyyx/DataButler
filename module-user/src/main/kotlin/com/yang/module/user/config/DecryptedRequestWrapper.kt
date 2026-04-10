package com.yang.module.user.config

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import java.io.ByteArrayInputStream

class DecryptedRequestWrapper(request: HttpServletRequest, body: String) : HttpServletRequestWrapper(request) {
    private val bodyBytes = body.toByteArray(Charsets.UTF_8)

    override fun getInputStream(): ServletInputStream {
        val bis = ByteArrayInputStream(bodyBytes)
        return object : ServletInputStream() {
            override fun read(): Int = bis.read()
            override fun isFinished(): Boolean = bis.available() == 0
            override fun isReady(): Boolean = true
            override fun setReadListener(listener: ReadListener?) {}
        }
    }

    override fun getContentLength(): Int = bodyBytes.size
    override fun getContentLengthLong(): Long = bodyBytes.size.toLong()

    override fun getContentType(): String {
        return "application/json;charset=UTF-8"
    }
}