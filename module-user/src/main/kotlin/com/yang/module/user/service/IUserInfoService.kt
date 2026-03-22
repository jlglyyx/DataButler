package com.yang.module.user.service;

import com.baomidou.mybatisplus.extension.service.IService
import com.yang.module.user.entity.UserInfo

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author yyx
 * @since 2026-03-22
 */
interface IUserInfoService : IService<UserInfo> {

    fun login(userInfo: UserInfo): UserInfo?

}
