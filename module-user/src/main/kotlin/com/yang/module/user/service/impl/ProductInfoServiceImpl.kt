package com.yang.module.user.service.impl;

import com.yang.module.user.entity.ProductInfo;
import com.yang.module.user.mapper.ProductInfoMapper;
import com.yang.module.user.service.IProductInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品信息表 服务实现类
 * </p>
 *
 * @author yyx
 * @since 2026-03-22
 */
@Service
open class ProductInfoServiceImpl : ServiceImpl<ProductInfoMapper, ProductInfo>(), IProductInfoService {

}
