package com.yang.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.yang.module.user.entity.OrderInfo
import com.yang.module.user.mapper.OrderInfoMapper
import com.yang.module.user.mapper.ProductInfoMapper
import com.yang.module.user.service.IOrderInfoService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * <p>
 * 订单信息表 服务实现类
 * </p>
 *
 * @author yyx
 * @since 2026-03-22
 */
@Service
open class OrderInfoServiceImpl(private val mProductInfoMapper: ProductInfoMapper) : ServiceImpl<OrderInfoMapper, OrderInfo>(), IOrderInfoService {

    @Transactional(rollbackFor = [Exception::class])
    override fun createOrder(userId: Long, productId: Long): OrderInfo? {
        // 1. 从数据库查询商品信息（确保价格真实）
        val product = mProductInfoMapper.selectById(productId)?: return null

        // 2. 获取数据库里的真实价格
        val realAmount = product.price

        // 3. 生成订单号
        val orderNo = "ORD${System.currentTimeMillis()}${userId % 100}"

        // 4. 构建并保存订单
        val order = OrderInfo().apply {
            this.orderNo = orderNo
            this.userId = userId
            this.productId = productId
            this.productName = product.name // 冗余一份快照名，防止以后商品改名
            this.totalAmount = realAmount   // 使用后端查询到的金额
            this.status = 0
            this.createTime = LocalDateTime.now()
        }

        this.save(order)
        return order
    }

}
