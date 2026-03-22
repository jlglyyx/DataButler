package com.yang.lib.common.helper

import com.yang.lib.common.data.HttpResult
import com.yang.lib.common.data.ResultEnum
import com.yang.lib.common.util.toJson

fun requestSuccess(data:Any?,enum: ResultEnum = ResultEnum.REQUEST_SUCCESS): String{

    return HttpResult(data,enum.code,enum.message,enum.success).toJson()

}

fun requestFail(enum: ResultEnum = ResultEnum.REQUEST_FAIL):String{

    return HttpResult(null,enum.code,enum.message,enum.success).toJson()
}