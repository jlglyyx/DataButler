package com.yang.lib.common.util

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone


// 1. 配置 Gson 实例
val gson: Gson = GsonBuilder()
    // 2. 注册自定义适配器，手动处理 LocalDateTime
    .registerTypeAdapter(LocalDateTime::class.java, JsonSerializer<LocalDateTime> { src, _, _ ->
        JsonPrimitive(src.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
    })
    // 2. 自动把前端传来的时间戳转回 LocalDateTime
    .registerTypeAdapter(LocalDateTime::class.java, JsonDeserializer { json, _, _ ->
        LocalDateTime.ofInstant(Instant.ofEpochMilli(json.asLong), ZoneId.systemDefault())
    })
//    .serializeNulls()
    .create()

// 对象转 JSON
fun Any.toJson(): String = gson.toJson(this)

// JSON 转对象
inline fun <reified T> String.fromJson(): T =
    gson.fromJson(this, object : TypeToken<T>() {}.type)

// JSON 转 List
inline fun <reified T> String.fromJsonList(): List<T> =
    gson.fromJson(this, TypeToken.getParameterized(List::class.java, T::class.java).type)


fun Date.dateFormat(
    format: String = "yyyy.MM.dd HH:mm:ss",
    locale: Locale = Locale.getDefault(),
    timeZone: TimeZone? = null
): String {
    return SimpleDateFormat(format, locale).apply {

        if (null != timeZone) {
            this.timeZone = timeZone
        }

    }.format(this)
}


fun BigDecimal.getDecimalPlacesIncludeZero(): Int {
    val plain = this.toPlainString()
    return if (plain.contains('.')) plain.length - plain.indexOf('.') - 1 else 0
}