package com.yang.module.user.controller;

import com.yang.lib.common.helper.requestSuccess
import com.yang.module.user.entity.ProductInfo
import com.yang.module.user.service.IProductInfoService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * <p>
 * 商品信息表 前端控制器
 * </p>
 *
 * @author yyx
 * @since 2026-03-22
 */
@RestController
@RequestMapping("/api/product")
class ProductInfoController(private val mIProductInfoService: IProductInfoService){

    @PostMapping("/queryProductInfo")
    fun queryProductInfo(): String {

        val data = mIProductInfoService.ktQuery()
            .orderByAsc(ProductInfo::sort)
            .list()

        return requestSuccess(data)
    }
}

