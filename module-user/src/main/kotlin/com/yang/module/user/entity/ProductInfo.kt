package com.yang.module.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品信息表
 * </p>
 *
 * @author yyx
 * @since 2026-03-22
 */
@TableName("product_info")
class ProductInfo : Serializable {

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    var id: Int? = null

    /**
     * 业务编码 (原p10, m30等)
     */
    var productCode: String? = null

    /**
     * 唯一SKU编码 (对接支付渠道使用)
     */
    var sku: String? = null

    /**
     * 商品名称
     */
    var name: String? = null

    /**
     * 商品描述
     */
    var description: String? = null

    /**
     * 当前售价
     */
    var price: BigDecimal? = null

    /**
     * 划线原价 (用于展示折扣)
     */
    var originalPrice: BigDecimal? = null

    /**
     * 货币符号
     */
    var currency: String? = null

    /**
     * 类型: 0-次数, 1-时长
     */
    var type: Int? = null

    /**
     * 包含次数
     */
    var count: Int? = null

    /**
     * 有效天数
     */
    var durationDays: Int? = null

    /**
     * 显示单位 (次/天/月)
     */
    var unit: String? = null

    /**
     * 标签 (体验/热销/限时)
     */
    var tag: String? = null

    /**
     * 排序权重
     */
    var sort: Int? = null

    /**
     * 是否默认选中
     */
    var isSelected: Boolean? = null

    /**
     * 上下架状态: 0-下架, 1-上架
     */
    var status: Boolean? = null

    /**
     * 是否前端可见: 0-隐藏, 1-显示
     */
    var isVisible: Boolean? = null

    /**
     * 创建时间
     */
    var createTime: LocalDateTime? = null

    /**
     * 更新时间
     */
    var updateTime: LocalDateTime? = null

    override fun toString(): String {
        return "ProductInfo{" +
        "id=" + id +
        ", productCode=" + productCode +
        ", sku=" + sku +
        ", name=" + name +
        ", description=" + description +
        ", price=" + price +
        ", originalPrice=" + originalPrice +
        ", currency=" + currency +
        ", type=" + type +
        ", count=" + count +
        ", durationDays=" + durationDays +
        ", unit=" + unit +
        ", tag=" + tag +
        ", sort=" + sort +
        ", isSelected=" + isSelected +
        ", status=" + status +
        ", isVisible=" + isVisible +
        ", createdTime=" + createTime +
        ", updatedTime=" + updateTime +
        "}"
    }
}
