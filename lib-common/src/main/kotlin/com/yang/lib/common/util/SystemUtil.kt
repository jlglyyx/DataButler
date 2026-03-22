package com.yang.lib.common.util

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
// 1. 配置 Gson 实例
val gson: Gson = GsonBuilder()
    // 2. 注册自定义适配器，手动处理 LocalDateTime
    .registerTypeAdapter(LocalDateTime::class.java, JsonSerializer<LocalDateTime> { src, _, _ ->
        JsonPrimitive(formatter.format(src))
    })
    .registerTypeAdapter(LocalDateTime::class.java, JsonDeserializer { json, _, _ ->
        LocalDateTime.parse(json.asString, formatter)
    })
    .serializeNulls()
    .create()

// 对象转 JSON
fun Any.toJson(): String = gson.toJson(this)

// JSON 转对象
inline fun <reified T> String.fromJson(): T =
    gson.fromJson(this, object : TypeToken<T>() {}.type)

// JSON 转 List
inline fun <reified T> String.fromJsonList(): List<T> =
    gson.fromJson(this, TypeToken.getParameterized(List::class.java, T::class.java).type)