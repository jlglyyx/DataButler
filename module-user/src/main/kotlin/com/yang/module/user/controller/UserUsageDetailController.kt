package com.yang.module.user.controller;

import com.yang.lib.common.data.ResultEnum
import com.yang.lib.common.helper.requestFail
import com.yang.lib.common.helper.requestSuccess
import com.yang.module.user.entity.UserUsageDetail
import com.yang.module.user.service.IOrderInfoService
import com.yang.module.user.service.IProductInfoService
import com.yang.module.user.service.IUserInfoService
import com.yang.module.user.service.IUserUsageDetailService
import org.springframework.beans.BeanUtils
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime

/**
 * <p>
 * 用户功能使用明细及流水表 前端控制器
 * </p>
 *
 * @author yyx
 * @since 2026-03-30
 */
@RestController
@RequestMapping("/api/use")
class UserUsageDetailController(
    private val mIOrderInfoService: IOrderInfoService,
    private val mIProductInfoService: IProductInfoService,
    private val mIUserInfoService: IUserInfoService,
    private val mIUserUsageDetailService: IUserUsageDetailService
) {


    @PostMapping("/useProduct")
    @Transactional
    fun useProduct(@RequestBody mUserUsageDetail: UserUsageDetail): String {

        val userId = mUserUsageDetail.userId ?: return requestFail()

        val userInfo = mIUserInfoService.getUserInfo(userId) ?: return requestFail()

        val isVip = userInfo.isVip

        mUserUsageDetail.beforeCount = userInfo.recoveryCount

        mUserUsageDetail.createTime = LocalDateTime.now()

        mUserUsageDetail.updateTime = LocalDateTime.now()
        /**
         * 功能具体类型: 1-照片恢复, 2-视频恢复, 3-画质增强
         */

        val title = when(mUserUsageDetail.usageType){
            "IMAGE" ->{"照片恢复"}
            "VIDEO"->{"视频恢复"}
            "IMAGE_TO_PDF"->{"图片转PDF"}
            "PDF_TO_IMAGE"->{"PDF转图片"}
            else -> ""
        }

        mUserUsageDetail.title = "使用${title}-${mUserUsageDetail.consumeCount}次"

        mUserUsageDetail.operation = "-"

        if (isVip) {


        }else{

            if (mUserUsageDetail.consumeCount > userInfo.recoveryCount) {

                return requestFail(ResultEnum.COUNT_NOT_ENOUGH_ERROR)

            }else{

                userInfo.recoveryCount -= mUserUsageDetail.consumeCount

                mIUserInfoService.updateById(userInfo)

            }

        }

        mUserUsageDetail.afterCount = userInfo.recoveryCount

        mUserUsageDetail.isVip = isVip

        mIUserUsageDetailService.save(mUserUsageDetail)

        return requestSuccess(userInfo)

    }
}


