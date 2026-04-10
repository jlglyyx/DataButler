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
 * @since 2026-04-01
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
     * 应用名称标识 (如: DataButler)
     */
    var appName: String? = null

    /**
     * 平台 (Android/iOS)
     */
    var platform: String? = null

    /**
     * 应用版本 (Name.Code)
     */
    var appVersion: String? = null

    /**
     * 是否首次登录: 1-是, 0-否
     */
    var isFirstLogin: Boolean? = null

    /**
     * 最近一次登录时间
     */
    var lastLoginTime: LocalDateTime? = null

    /**
     * 手机品牌 (如: Xiaomi, Huawei)
     */
    var deviceBrand: String? = null

    /**
     * 手机型号 (如: Mi 14)
     */
    var deviceModel: String? = null

    /**
     * 操作系统版本号
     */
    var osVersion: String? = null

    /**
     * 安卓系统原生 ID
     */
    var androidId: String? = null

    /**
     * 当前登录 IP 地址
     */
    var currentIp: String? = null

    /**
     * 系统语言 (如: zh)
     */
    var language: String? = null

    /**
     * 设备时区 (如: Asia/Shanghai)
     */
    var timezone: String? = null

    /**
     * 网络类型 (4G, 5G, WIFI)
     */
    var networkType: String? = null

    /**
     * 运营商名称
     */
    var operator: String? = null

    /**
     * 预留 JSON 字段
     */
    var extraData: String? = null

    /**
     * 记录创建时间
     */
    var createTime: LocalDateTime? = null

    /**
     * 记录更新时间
     */
    var updateTime: LocalDateTime? = null

    override fun toString(): String {
        return "UserExtraInfo{" +
        "id=" + id +
        ", userId=" + userId +
        ", appName=" + appName +
        ", platform=" + platform +
        ", appVersion=" + appVersion +
        ", isFirstLogin=" + isFirstLogin +
        ", lastLoginTime=" + lastLoginTime +
        ", deviceBrand=" + deviceBrand +
        ", deviceModel=" + deviceModel +
        ", osVersion=" + osVersion +
        ", androidId=" + androidId +
        ", currentIp=" + currentIp +
        ", language=" + language +
        ", timezone=" + timezone +
        ", networkType=" + networkType +
        ", operator=" + operator +
        ", extraData=" + extraData +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}"
    }
}
