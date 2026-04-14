package com.yang.module.user.service;

import com.baomidou.mybatisplus.extension.service.IService
import com.yang.module.user.entity.OrderInfo

/**
 * <p>
 * 订单信息表 服务类
 * </p>
 *
 * @author yyx
 * @since 2026-03-22
 */
interface IOrderInfoService : IService<OrderInfo>{

    fun createOrder(userId: Long, productId: Long): OrderInfo?

    fun refundOrder(mOrderInfo: OrderInfo): OrderInfo?
}
