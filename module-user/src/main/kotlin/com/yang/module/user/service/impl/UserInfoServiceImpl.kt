package com.yang.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.yang.module.user.entity.UserExtraInfo
import com.yang.module.user.entity.UserInfo
import com.yang.module.user.mapper.UserExtraInfoMapper
import com.yang.module.user.mapper.UserInfoMapper
import com.yang.module.user.service.IUserInfoService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author yyx
 * @since 2026-03-22
 */
@Service
open class UserInfoServiceImpl(private val mUserExtraInfoMapper: UserExtraInfoMapper) :
    ServiceImpl<UserInfoMapper, UserInfo>(), IUserInfoService {


    @Transactional(rollbackFor = [Exception::class]) // 开启事务，遇到任何异常回滚
    override fun login(userInfo: UserInfo): UserInfo? {
        // 1. 根据用户名查询是否存在
        var user = ktQuery()
            .eq(UserInfo::userName, userInfo.userName)
            .last("LIMIT 1")
            .one()

        if (user == null) {
            userInfo.createdTime = LocalDateTime.now()
            val isSaved = this.save(userInfo)
            if (isSaved) {
                val extra = UserExtraInfo()
                extra.userId = userInfo.id
                extra.isFirstLogin = true

                mUserExtraInfoMapper.insert(extra)
                user = userInfo
            }
        } else {
            userInfo.id = user.id
            updateById(userInfo)
            user = ktQuery().eq(UserInfo::id, userInfo.id).one()
        }

        return user
    }

}
