package com.yang.module.user.entity;

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName;
import com.yang.lib.common.data.PageRequest
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单信息表
 * </p>
 *
 * @author yyx
 * @since 2026-03-22
 */
@TableName("order_info")
class OrderInfo : Serializable, PageRequest() {

    /**
     * 主键ID (雪花算法生成)
     */
    @TableId(type = IdType.ASSIGN_ID)
    var id: Long? = null

    /**
     * 业务订单号 (发给支付宝的唯一编号)
     */
    var orderNo: String? = null

    /**
     * 关联用户ID
     */
    var userId: Long? = null

    /**
     * 关联商品ID
     */
    var productId: Long? = null

    /**
     * 商品快照名称
     */
    var productName: String? = null

    /**
     * 单位
     */
    var currency: String? = null
    /**
     * 订单总金额
     */
    var totalAmount: BigDecimal? = null
    /**
     * 退款总金额
     */
    var rebackAmount: BigDecimal? = null

    /**
     * 订单状态: 0-待支付, 1-已支付, 2-已取消, 3-已退款
     */
    var status: Int? = null

    /**
     * 支付方式: 1-支付宝, 2-微信
     */
    var payType: Int? = null

    /**
     * 支付宝交易流水号 (回调时存入)
     */
    var alipayTradeNo: String? = null

    /**
     * 买家支付宝账号 (对账用)
     */
    var buyerLogonId: String? = null

    /**
     * 创建时间
     */
    var createTime: LocalDateTime? = null

    /**
     * 更新时间
     */
    var updateTime: LocalDateTime? = null

    @TableField(exist = false)
    var alpayInfo : String? = null

    override fun toString(): String {
        return "OrderInfo{" +
        "id=" + id +
        ", orderNo=" + orderNo +
        ", userId=" + userId +
        ", productId=" + productId +
        ", productName=" + productName +
        ", currency=" + currency +
        ", totalAmount=" + totalAmount +
        ", status=" + status +
        ", payType=" + payType +
        ", alipayTradeNo=" + alipayTradeNo +
        ", buyerLogonId=" + buyerLogonId +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}"
    }
}
