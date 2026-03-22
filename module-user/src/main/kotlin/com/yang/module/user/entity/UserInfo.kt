package com.yang.module.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author yyx
 * @since 2026-03-22
 */
@TableName("user_info")
class UserInfo : Serializable {

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    var id: Long? = null

    /**
     * 用户账号
     */
    var userName: String? = null

    /**
     * 加密后的密码
     */
    var password: String? = null

    /**
     * 邮箱地址
     */
    var email: String? = null

    /**
     * 手机号
     */
    var phone: String? = null

    /**
     * 用户类型: 1-普通用户, 2-管理员
     */
    var userType: Byte? = null

    /**
     * 状态: 0-禁用, 1-启用
     */
    var status: Boolean? = null

    /**
     * 会员等级
     */
    var vipLevel: Int? = null

    /**
     * 会员到期时间
     */
    var vipExpireTime: LocalDateTime? = null

    /**
     * 恢复次数
     */
    var recoveryCount: Int? = null

    /**
     * 创建时间
     */
    var createdTime: LocalDateTime? = null

    /**
     * 更新时间
     */
    var updatedTime: LocalDateTime? = null

    override fun toString(): String {
        return "UserInfo{" +
        "id=" + id +
        ", userName=" + userName +
        ", password=" + password +
        ", email=" + email +
        ", phone=" + phone +
        ", userType=" + userType +
        ", status=" + status +
        ", vipLevel=" + vipLevel +
        ", vipExpireTime=" + vipExpireTime +
        ", recoveryCount=" + recoveryCount +
        ", createdTime=" + createdTime +
        ", updatedTime=" + updatedTime +
        "}"
    }
}
