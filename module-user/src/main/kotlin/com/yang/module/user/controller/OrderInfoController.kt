package com.yang.module.user.controller;

import com.yang.lib.common.helper.requestFail
import com.yang.lib.common.helper.requestSuccess
import com.yang.module.user.entity.OrderInfo
import com.yang.module.user.service.IOrderInfoService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单信息表 前端控制器
 * </p>
 *
 * @author yyx
 * @since 2026-03-22
 */
@RestController
@RequestMapping("/api/order")
class OrderInfoController(private val mIOrderInfoService: IOrderInfoService) {


    @PostMapping("/createOrder")
    fun createOrder(@RequestBody mOrderInfo:OrderInfo): String {

        if (null == mOrderInfo.userId || null == mOrderInfo.productId) { return requestFail() }

        val orderInfo = mIOrderInfoService.createOrder(mOrderInfo.userId!!,mOrderInfo.productId!!)

        if (null == orderInfo) return requestFail()

        return requestSuccess(orderInfo)

    }
}

