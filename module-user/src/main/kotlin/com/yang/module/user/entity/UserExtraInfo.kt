package com.yang.module.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户扩展信息表
 * </p>
 *
 * @author yyx
 * @since 2026-03-22
 */
@TableName("user_extra_info")
class UserExtraInfo : Serializable {

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    var id: Long? = null

    /**
     * 关联 user_info 表的 id
     */
    var userId: Long? = null

    /**
     * 是否首次登录: 1-是, 0-否
     */
    var isFirstLogin: Boolean? = null

    /**
     * 最近一次登录时间
     */
    var lastLoginTime: LocalDateTime? = null

    /**
     * 当前使用的 App 版本号
     */
    var appVersion: String? = null

    /**
     * 手机型号 (如: iPhone 15 Pro, Xiaomi 14)
     */
    var deviceModel: String? = null

    /**
     * 操作系统版本 (如: iOS 17.4, Android 14)
     */
    var osVersion: String? = null

    /**
     * 当前登录 IP 地址
     */
    var currentIp: String? = null

    /**
     * 预留 JSON 字段，存放不确定的扩展属性
     */
    var extraData: String? = null

    /**
     * 记录创建时间
     */
    var createdTime: LocalDateTime? = null

    /**
     * 记录更新时间
     */
    var updatedTime: LocalDateTime? = null

    override fun toString(): String {
        return "UserExtraInfo{" +
        "id=" + id +
        ", userId=" + userId +
        ", isFirstLogin=" + isFirstLogin +
        ", lastLoginTime=" + lastLoginTime +
        ", appVersion=" + appVersion +
        ", deviceModel=" + deviceModel +
        ", osVersion=" + osVersion +
        ", currentIp=" + currentIp +
        ", extraData=" + extraData +
        ", createdTime=" + createdTime +
        ", updatedTime=" + updatedTime +
        "}"
    }
}
