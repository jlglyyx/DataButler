package com.yang.lib.common.util

import java.security.KeyFactory
import java.security.SecureRandom
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object HttpDecryptServer {

    private const val RSA_TRANSFORMATION = "RSA/ECB/PKCS1Padding"
    private const val AES_TRANSFORMATION = "AES/GCM/NoPadding"
    private const val IV_SIZE = 12
    private const val TAG_SIZE = 128

    /**
     * 1. RSA 解密：还原出客户端生成的 32 字节原始 AES 密钥
     * @param encryptedKeyBase64 Header 中的 "key" 字段
     * @param privateKeyBase64 服务端保存的 PKCS#8 格式私钥（不带头尾）
     */
    fun rsaDecryptKey(encryptedKeyBase64: String, privateKeyBase64: String): ByteArray {

        val encryptedKey = Base64.getDecoder().decode(encryptedKeyBase64)

        val keyBytes = Base64.getDecoder().decode(privateKeyBase64)

        // 注意：服务端解密必须使用 PKCS8 规范
        val spec = PKCS8EncodedKeySpec(keyBytes)
        val kf = KeyFactory.getInstance("RSA")
        val privateKey = kf.generatePrivate(spec)

        val cipher = Cipher.getInstance(RSA_TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        return cipher.doFinal(encryptedKey)
    }

    /**
     * 2. AES-GCM 解密：还原 info 或 body 数据
     * @param encryptedData 客户端发来的 Base64(IV + CipherText + Tag)
     * @param sessionKey 上一步 RSA 解出来的 32 字节 ByteArray
     */
    fun aesDecrypt(encryptedData: String, sessionKey: ByteArray): String {
        val combined = Base64.getDecoder().decode(encryptedData)
        if (combined.size < IV_SIZE) throw IllegalArgumentException("数据长度异常")

        // 提取前 12 字节作为 IV
        val iv = combined.copyOfRange(0, IV_SIZE)
        // 剩余部分是 密文 + 16 字节的 GCM Tag
        val cipherTextWithTag = combined.copyOfRange(IV_SIZE, combined.size)

        val cipher = Cipher.getInstance(AES_TRANSFORMATION)
        val spec = GCMParameterSpec(TAG_SIZE, iv)
        val keySpec = SecretKeySpec(sessionKey, "AES")

        cipher.init(Cipher.DECRYPT_MODE, keySpec, spec)
        val decrypted = cipher.doFinal(cipherTextWithTag)

        return String(decrypted, Charsets.UTF_8)
    }

    /**
    * 3. AES-GCM 加密：加密返回给 Android 的数据
    * @param plainText 服务端原始 JSON 字符串
    * @param sessionKey 从 Header "key" 中解密出来的 32 字节 ByteArray
    * @return Base64(IV + CipherText + Tag)
    */
    fun aesEncrypt(plainText: String, sessionKey: ByteArray): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")

        // 生成随机 IV (12字节)
        val iv = ByteArray(12)
        SecureRandom().nextBytes(iv)

        val spec = GCMParameterSpec(128, iv)
        val keySpec = SecretKeySpec(sessionKey, "AES")

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec)
        val cipherText = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        // 拼接 IV + 密文 (GCM 自动包含 Tag)
        val combined = ByteArray(iv.size + cipherText.size)
        System.arraycopy(iv, 0, combined, 0, iv.size)
        System.arraycopy(cipherText, 0, combined, iv.size, cipherText.size)

        return Base64.getEncoder().encodeToString(combined)
    }


    fun parseQueryString(query: String): Map<String, String> {
        return query.split("&").associate {
            val (key, value) = it.split("=", limit = 2)
            key to value
        }
    }
}