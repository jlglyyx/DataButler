package com.yang.lib.common.data

data class HttpResult<T>(val data:T?, val code:Int, val message:String,val success:Boolean)
