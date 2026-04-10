package com.yang.module.user.config

import com.alibaba.fastjson.JSONObject
import com.yang.lib.common.data.HttpResult
import com.yang.lib.common.data.ResultEnum
import com.yang.lib.common.util.HttpDecryptServer
import com.yang.lib.common.util.HttpDecryptServer.parseQueryString
import com.yang.lib.common.util.fromJson
import com.yang.lib.common.util.toJson
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import jakarta.servlet.*
import jakarta.servlet.http.HttpServletResponse
import org.bouncycastle.asn1.crmf.POPOPrivKey.encryptedKey
import kotlin.math.abs

@Component
class SecurityFilter : Filter {

    // 换成你的 PKCS#8 私钥
    private val RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCzxPS7ouwReufCjNZ43EWnFXlxNR3p4JuIINmGmBIcIIvewpNa53cz69aP+DHsmHxmrXetoJDy8qDOT5uvH0Fpct3fgBPhdvwLH22FrflSxs9Hg6qwZs3stZAH1j/YlayuInFSDTqdmX0uMhKA2DiHZNvCx8SRU0suP+ThvpQgEwEjQZRgUdKwsWoypPDni9H+3MfZnr87g374t64ktUjjvhtRkIrFNcBV4E9Oi8lKBO/Da0IpnS8vk/i2ygQNvtyRvGkrQAsD59SqTmtoYvJpWKi4xWveyFdUVHhJapaMIir08wRoU0hi5nS+q/T0JGvDGWv/klMcl+AOGW2Y1+YzAgMBAAECggEAJtybiVqZjNfUdS7MCHmZxxpx0+LHMjF/feiYDawfMhxpEEFg6IRkNQG1Mw9U/5AhSHMUjaDJ5PNFhwQXpD5t/hU6WKAP7Cw0JVVocD8hSF85HP0eL58xwgun9MHAsJn+p/BDLhk1BC/Tq2JgtKDJa5+GkG9uE3p9pKRkXta7n8kPCtE6xLWBP8EkK7Y8NZ5H3GW5OHwWIMrX4le3/j/7Y2ggEpPt6oe1bFqQF4yIogYgaOP8BacyC2qpYqqm5/+YAKRkjRkNpOhocrjoHhgQ4u1DOJjHiKIcBR0kX7baK/5lNywlwZ/LgzCeAzjF2mjLdZWIsE9G4gAkb/xqYNt4jQKBgQDjig0qcZ90Xfcb8e+8o4GV3J37URFrF1YFq9UHdbl8MH+fy0RZcF6pWoEoaMiZ6Kchtcl95R48SYJN4B1/oruT7ytqvAkTj4/+n1ZR6XbjCkzLEWDxe90l2GaJYje+Wee781hGrf9eus4Uu0t5gGIGifIGZ+0ZinVxAuVL/Ce0/wKBgQDKQUhVepO5KbrgT7ddaakxYRJPRY3Co377dfbtlsMIHWJcJKiugj8k27IxAWEi9Nz/WwstRF/f7HY+8tNWm+sB3tkkWn8BZLZHceCxg8Ls1N2yDf3CbmGZof+9rzAi2m1eKgvpbuCnXmxlChf1dp7mcxytO1jOLbsRc0lpHgYKzQKBgGzLzY/F1GBnrYgHHoIsDAIEAuXv1UxHVDJR/wIaIB5pchyNrJi3Sgvf4nHX6FLvEzrjGPzBWLuqyktCJdipjtclPLVEQ3K+6jLtLPDtmJLmkeFm5duRRiSaXCzEcbLqegs8R7FD8R7yYyc2htgJstU9kCPP/WaA9babk9fJMZMxAoGBALHuIquIbaMM0il6F89nIACf7FKu1tqvqQuhqL01z5g4+Yokef0j+lb7q9Su6PyuvCktuDR3nXzKcvBIAaKFAocfvJndvzPDnRRXW5vvVbGyRuLYqjC9mMFmoi0GjOtsi6C2jsoq0BqN2AA/k/pPA9Wq2USRHDwuQQ9GBAQk9J3VAoGAAKYoiySZ97tRg82sJgY4N8T7thJRRbkpLps4+ozm+JSSI6zZrbs1ZFw0uaxTCRPWHtIL2FW4ATEeglt2iId3nkbb9Tgx8C47mUnoRXE9xBHEp71rN3Q+cqUKq03cVQuDCy9kdkho7x8ZlOds9aHG3DEVhc3aEV/zN7ps0cEEVMs="

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val req = request as HttpServletRequest

        try {
            // 1. 获取解密钥匙
            val encryptedKey = req.getHeader("key") ?: return chain.doFilter(request, response)

//            println("接收到的Key长度: ${encryptedKey.length}")
//            println("接收到的内容: $encryptedKey")

            val sessionKey = HttpDecryptServer.rsaDecryptKey(encryptedKey, RSA_PRIVATE_KEY)

            // 存入 Request 属性，后续 Advice 加密响应时要用
            req.setAttribute("SESSION_KEY", sessionKey)

            // 2. 解密并校验 Info (Header 里的元数据)
            val encryptedInfo = req.getHeader("info")
            if (!encryptedInfo.isNullOrEmpty()) {
                try {
                    // 1. 解密元数据
                    val infoStr = HttpDecryptServer.aesDecrypt(encryptedInfo, sessionKey)

                    val infoMap = parseQueryString(infoStr)

//                    val token = infoMap["token"] ?: throw RuntimeException("Missing timestamp")

//                    // 2. 获取客户端时间戳 (对应 Android 端的 "timestamp")
//                    val clientTime = infoMap["timestamp"]?.toLongOrNull()
//                        ?: throw RuntimeException("Missing timestamp")
//
//                    val currentTime = System.currentTimeMillis()
//
//                    // 3. 校验时间偏差：允许前后 60 秒误差（考虑网络延迟和手机时钟不准）
//                    val timeDiff = abs(currentTime - clientTime)
//                    if (timeDiff > 60 * 1000) {
//                        throw RuntimeException("Request expired. Time diff: ${timeDiff}ms")
//                    }

                    // 5. 将解析后的信息存入 Request 方便后续业务使用
                    req.setAttribute("CLIENT_INFO", infoMap)

                } catch (e: Exception) {
                    (response as HttpServletResponse).sendError(401, "Security check failed: ${e.message}")
                    e.printStackTrace()
                    return // 拦截，不执行后续逻辑
                }
            }

            // 3. 处理 Body 解密
            val contentType = req.contentType
            if (contentType != null && contentType.contains("application/json")) {
                val encryptedBody = req.reader.readText()


                val content = encryptedBody.fromJson<HttpResult<String>>()

                val encryptedData = content.data.toString()

                val plainBody = HttpDecryptServer.aesDecrypt(encryptedData, sessionKey)

                val wrappedRequest = DecryptedRequestWrapper(req, plainBody)

                chain.doFilter(wrappedRequest, response)
            } else {
                chain.doFilter(req, response)
            }
        } catch (e: Exception) {
            // 解密失败或数据篡改
            (response as HttpServletResponse).sendError(ResultEnum.REQUEST_FAIL.code, ResultEnum.REQUEST_FAIL.message)
            e.printStackTrace()
        }
    }
}