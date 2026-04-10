package com.yang.module.user.service.impl;

import com.yang.module.user.entity.UserUsageDetail;
import com.yang.module.user.mapper.UserUsageDetailMapper;
import com.yang.module.user.service.IUserUsageDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户功能使用明细及流水表 服务实现类
 * </p>
 *
 * @author yyx
 * @since 2026-03-30
 */
@Service
open class UserUsageDetailServiceImpl : ServiceImpl<UserUsageDetailMapper, UserUsageDetail>(), IUserUsageDetailService {

}
