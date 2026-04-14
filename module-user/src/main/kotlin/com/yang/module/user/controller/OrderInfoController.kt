package com.yang.module.user.controller;

import com.alipay.api.domain.RefundInfo
import com.alipay.api.internal.util.AlipaySignature
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.yang.lib.common.data.ResultEnum
import com.yang.lib.common.helper.requestFail
import com.yang.lib.common.helper.requestSuccess
import com.yang.lib.common.util.getDecimalPlacesIncludeZero
import com.yang.module.user.config.AlipayManager
import com.yang.module.user.entity.OrderInfo
import com.yang.module.user.entity.ProductInfo
import com.yang.module.user.entity.UserInfo
import com.yang.module.user.entity.UserUsageDetail
import com.yang.module.user.service.IOrderInfoService
import com.yang.module.user.service.IProductInfoService
import com.yang.module.user.service.IUserInfoService
import jakarta.servlet.http.HttpServletRequest
import lombok.extern.slf4j.Slf4j
import org.bouncycastle.pqc.math.linearalgebra.IntegerFunctions.order
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * <p>
 * 订单信息表 前端控制器
 * </p>
 *
 * @author yyx
 * @since 2026-03-22
 */
@Slf4j
@RestController
@RequestMapping("/api/order")
class OrderInfoController(private val mIOrderInfoService: IOrderInfoService,
                          private val mIProductInfoService: IProductInfoService,
                          private val mIUserInfoService: IUserInfoService,
) {


    @PostMapping("/createOrder")
    fun createOrder(@RequestBody mOrderInfo:OrderInfo): String {

        if (null == mOrderInfo.userId || null == mOrderInfo.productId) { return requestFail() }

        val orderInfo = mIOrderInfoService.createOrder(mOrderInfo.userId!!, mOrderInfo.productId!!) ?: return requestFail()

        return requestSuccess(orderInfo)

    }

    @PostMapping("/getOrderStatus")
    fun getOrderStatus(@RequestBody mOrderInfo:OrderInfo): String {

        if (null == mOrderInfo.userId || null == mOrderInfo.id) { return requestFail() }

        val orderInfo = mIOrderInfoService.getById(mOrderInfo.id)?: return requestFail()

        return requestSuccess(orderInfo)

    }


    @PostMapping("/alpayNotice")
    fun alpayNotice(request: HttpServletRequest): String {
        val params = mutableMapOf<String, String>()
        val requestParams = request.parameterMap
        requestParams.forEach { (key, values) ->
            params[key] = values.joinToString(",")
        }

        val signVerified = AlipaySignature.rsaCheckV1(
            params,
            AlipayManager.ALIPAY_PUBLIC_KEY,
            AlipayManager.ALIPAY_CHARSET,
            AlipayManager.SIGN_TYPE
        )

        if (!signVerified) return "fail"

        val outTradeNo = params["out_trade_no"]
        val tradeStatus = params["trade_status"]
        val totalAmountStr = params["total_amount"] ?: "0.0"
        val totalAmount = totalAmountStr.toBigDecimal()

        // 假设你的 ID 类型是 Long
        val order = mIOrderInfoService.ktQuery().eq(OrderInfo::orderNo,outTradeNo).one()

        if (order != null && (tradeStatus == "TRADE_SUCCESS" || tradeStatus == "TRADE_FINISHED")) {
            // 1. 幂等校验
            if (order.status == 1) return "success"

            // 2. 状态校验 & 金额比对 (使用 compareTo)
            if (order.status == 0 && order.totalAmount?.compareTo(totalAmount) == 0) {
                val updateResult = mIOrderInfoService.ktUpdate()
                    .eq(OrderInfo::id, order.id)
                    .eq(OrderInfo::status, 0) // 乐观锁思想，确保只从0变1
                    .set(OrderInfo::status, 1)
                    .set(OrderInfo::alipayTradeNo, params["trade_no"])
                    .set(OrderInfo::buyerLogonId, params["buyer_logon_id"])
                    .update()

                if (updateResult){

                    val userInfo = mIUserInfoService.getById(order.userId)

                    val productInfo = mIProductInfoService.getById(order.productId)

                    if (productInfo.type == 0) {
                        userInfo.recoveryCount = (productInfo.count ?: 0) + (userInfo.recoveryCount ?: 0)
                    } else {
                        val daysToAdd = productInfo.durationDays?.toLong() ?: 0L

                        val currentExpireTime = userInfo.vipExpireTime

                        val now = LocalDateTime.now()

                        val baseTime = if (currentExpireTime == null || currentExpireTime.isBefore(now)) {
                            now
                        } else {
                            currentExpireTime
                        }
                        userInfo.vipExpireTime = baseTime.plusDays(daysToAdd)
                    }

                    mIUserInfoService.updateById(userInfo)

                    return "success"
                }
            }
        }
        return "fail"
    }



    @PostMapping("/getOrderList")
    fun getOrderList(@RequestBody mOrderInfo: OrderInfo): String {

        if (null == mOrderInfo.userId) { return requestFail() }

        val buildPage = mOrderInfo.buildPage<OrderInfo>()


        val result = mIOrderInfoService.ktQuery().eq(OrderInfo::userId, mOrderInfo.userId)
            .`in`(OrderInfo::status, listOf(1, 3))
            .orderByDesc(OrderInfo::createTime).page(buildPage)

        val list =  result.records

        return requestSuccess(list)

    }



    @PostMapping("/refundOrder")
    fun refundOrder(@RequestBody mOrderInfo: OrderInfo): String {

        // 1. 基础非空校验
        if (null == mOrderInfo.userId || null == mOrderInfo.orderNo || null == mOrderInfo.rebackAmount) {
            return requestFail(ResultEnum.PARAM_ERROR_FAIL)
        }

        if (mOrderInfo.rebackAmount!!.getDecimalPlacesIncludeZero() >= 3){

            return requestFail(ResultEnum.REFUND_AMOUNT_FORMATE_ERROR)
        }

        val userInfo = mIUserInfoService.getById(mOrderInfo.userId) ?: return requestFail(ResultEnum.USER_NOT_EXIST_ERROR)

        if (userInfo.userType != 2) return requestFail(ResultEnum.FORBIDDEN_FAIL)

        val orderInfo = mIOrderInfoService.ktQuery().eq(OrderInfo::orderNo, mOrderInfo.orderNo).one() ?: return requestFail(ResultEnum.ORDER_NOT_FOUND_ERROR)

        if (mOrderInfo.rebackAmount!!.compareTo(orderInfo.totalAmount) == 1) {

            return requestFail(ResultEnum.REFUND_AMOUNT_EXCEED_ERROR)
        }
        if (orderInfo.status != 1) {

            return requestFail(ResultEnum.OPERATOR_PERMISSION_DENIED)
        }

        mOrderInfo.alipayTradeNo = orderInfo.alipayTradeNo

        val refundOrder = mIOrderInfoService.refundOrder(mOrderInfo) ?: return requestFail(ResultEnum.REFUND_FAILED_CONTACT_SERVICE)

        return requestSuccess(refundOrder)
    }

}

