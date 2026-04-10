package com.yang.module.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户功能使用明细及流水表
 * </p>
 *
 * @author yyx
 * @since 2026-03-30
 */
@TableName("user_usage_detail")
class UserUsageDetail : Serializable {

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    var id: Long? = null

    /**
     * 用户唯一标识
     */
    var userId: Long? = null


    var title: String = ""

    var operation: String = "-"

    /**
     * 功能具体类型: 1-照片恢复, 2-视频恢复, 3-画质增强
     */
    var usageType: String? = null

    /**
     * 操作时是否为会员
     */
    var isVip: Boolean? = null

    /**
     * 操作是否成功(如果失败通常不扣次数)
     */
    var isSuccess: Boolean? = null

    /**
     * 本次操作消耗的次数 (会员一般为0)
     */
    var consumeCount: Int = 0

    /**
     * 操作前剩余总次数
     */
    var beforeCount: Int = 0

    /**
     * 操作后剩余总次数
     */
    var afterCount: Int = 0

    /**
     * 关联的订单号(如果是按次购买的消耗)
     */
    var orderNo: String? = null

    /**
     * 设备标识
     */
    var deviceId: String? = null

    /**
     * 请求IP
     */
    var ipAddress: String? = null

    /**
     * 系统类型: iOS/Android
     */
    var osType: String? = null

    /**
     * 客户端版本号
     */
    var appVersion: String? = null

    /**
     * 备注或错误详情
     */
    var remark: String? = null

    /**
     * 创建时间
     */
    var createTime: LocalDateTime? = null

    /**
     * 更新时间
     */
    var updateTime: LocalDateTime? = null

    override fun toString(): String {
        return "UserUsageDetail{" +
        "id=" + id +
        ", userId=" + userId +
        ", title=" + title +
        ", operation=" + operation +
        ", usageType=" + usageType +
        ", isVip=" + isVip +
        ", isSuccess=" + isSuccess +
        ", consumeCount=" + consumeCount +
        ", beforeCount=" + beforeCount +
        ", afterCount=" + afterCount +
        ", orderNo=" + orderNo +
        ", deviceId=" + deviceId +
        ", ipAddress=" + ipAddress +
        ", osType=" + osType +
        ", appVersion=" + appVersion +
        ", remark=" + remark +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}"
    }
}
