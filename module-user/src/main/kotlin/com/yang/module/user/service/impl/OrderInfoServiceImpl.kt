package com.yang.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.yang.lib.common.data.ResultEnum
import com.yang.lib.common.helper.requestFail
import com.yang.lib.common.helper.requestSuccess
import com.yang.module.user.config.AlipayManager
import com.yang.module.user.entity.OrderInfo
import com.yang.module.user.entity.UserInfo
import com.yang.module.user.mapper.OrderInfoMapper
import com.yang.module.user.mapper.ProductInfoMapper
import com.yang.module.user.service.IOrderInfoService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.time.Duration
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
open class OrderInfoServiceImpl(private val mProductInfoMapper: ProductInfoMapper,private val mAlipayManager: AlipayManager) : ServiceImpl<OrderInfoMapper, OrderInfo>(), IOrderInfoService {

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
            this.currency = product.currency   // 使用后端查询到的金额
            this.totalAmount = realAmount   // 使用后端查询到的金额
            this.status = 0
            this.createTime = LocalDateTime.now()
        }

        this.save(order)

        val createPayInfo = mAlipayManager.createPayInfo(orderNo, product)

        if (createPayInfo.isEmpty()) {

            return null
        }

        val orderInfo = OrderInfo()

        orderInfo.id = order.id

        orderInfo.alpayInfo = createPayInfo

        return orderInfo
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun refundOrder(mOrderInfo: OrderInfo): OrderInfo? {

        // 1. 先调用支付宝
        val isAlipaySuccess = mAlipayManager.reBackAlipay(mOrderInfo)

        if (isAlipaySuccess) {
            // 2. 执行数据库更新
            // 建议增加 version 或 status 的乐观锁检查，防止并发
            val update = ktUpdate().eq(OrderInfo::orderNo, mOrderInfo.orderNo)
                .eq(OrderInfo::status, 1) // 确保状态还是待退款
                .set(OrderInfo::rebackAmount, mOrderInfo.rebackAmount)
                .set(OrderInfo::status, 3) // 3-已退款
                .update()

            if (update) {
                return ktQuery().eq(OrderInfo::orderNo, mOrderInfo.orderNo).one()
            } else {
                // 实际开发中这里应该抛出自定义异常，触发事务回滚，并由定时任务/人工介入对账
                throw RuntimeException("支付宝退款成功但数据库更新失败，单号：${mOrderInfo.orderNo} 金额${mOrderInfo.rebackAmount}")
            }
        }
        return null
    }


}
