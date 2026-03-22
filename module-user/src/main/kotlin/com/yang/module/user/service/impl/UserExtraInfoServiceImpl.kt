package com.yang.module.user.service.impl;

import com.yang.module.user.entity.UserExtraInfo;
import com.yang.module.user.mapper.UserExtraInfoMapper;
import com.yang.module.user.service.IUserExtraInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户扩展信息表 服务实现类
 * </p>
 *
 * @author yyx
 * @since 2026-03-22
 */
@Service
open class UserExtraInfoServiceImpl : ServiceImpl<UserExtraInfoMapper, UserExtraInfo>(), IUserExtraInfoService {

}
