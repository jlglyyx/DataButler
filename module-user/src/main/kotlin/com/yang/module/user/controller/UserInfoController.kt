package com.yang.module.user.controller;

import com.yang.lib.common.data.ResultEnum
import com.yang.lib.common.helper.requestFail
import com.yang.lib.common.helper.requestSuccess
import com.yang.module.user.entity.UserExtraInfo
import com.yang.module.user.entity.UserInfo
import com.yang.module.user.service.IUserExtraInfoService
import com.yang.module.user.service.IUserInfoService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author yyx
 * @since 2026-03-22
 */
@RestController
@RequestMapping("/api/user")
class UserInfoController(
    private val mIUserInfoService: IUserInfoService,
    private val mIUserExtraInfoService: IUserExtraInfoService,
) {

    @PostMapping("/login")
    fun login(@RequestBody userInfo: UserInfo,@RequestAttribute("CLIENT_INFO") infoMap: Map<String, Any>): String {

        if (userInfo.userName.isNullOrEmpty() || userInfo.password.isNullOrEmpty()) {
            return requestFail(ResultEnum.PARAM_ERROR_FAIL)
        }


        var loginUser = mIUserInfoService.ktQuery()
            .eq(UserInfo::userName, userInfo.userName)
            .eq(UserInfo::status,true)
            .orderByDesc(UserInfo::id)
            .last("LIMIT 1")
            .one()

        if (null == loginUser) {

            val userExtraInfo = mIUserExtraInfoService.ktQuery()
                .eq(UserExtraInfo::androidId, infoMap["android_id"].toString())
                .orderByDesc(UserExtraInfo::id)
                .last("LIMIT 1")
                .one()


            if (null == userExtraInfo){
                userInfo.recoveryCount = 3
            }else{
                if ((userExtraInfo.deviceBrand+"_"+userExtraInfo.deviceModel) != (infoMap["device_brand"].toString()+"_"+infoMap["device_model"].toString())){
                    userInfo.recoveryCount = 3
                }

            }
            userInfo.createTime = LocalDateTime.now()
            userInfo.updateTime = LocalDateTime.now()

            val isSaved = mIUserInfoService.save(userInfo)

            if (isSaved) {

                val userExtraInfo = UserExtraInfo().apply {
                    userId = userInfo.id
                    isFirstLogin = true
                    appName = infoMap["app_name"] as? String
                    platform = infoMap["platform"] as? String ?: "Android"
                    osVersion = infoMap["os_version"] as? String
                    appVersion = infoMap["app_version"] as? String
                    deviceBrand = infoMap["device_brand"] as? String
                    deviceModel = infoMap["device_model"] as? String
                    androidId = infoMap["android_id"] as? String
                    language = infoMap["language"] as? String
                    timezone = infoMap["timezone"] as? String
                    networkType = infoMap["network_type"] as? String
                    operator = infoMap["operator"] as? String
                    lastLoginTime = LocalDateTime.now()
                }

                mIUserExtraInfoService.save(userExtraInfo)

                loginUser = mIUserInfoService.ktQuery().eq(UserInfo::id, userInfo.id).one()

                loginUser.password = null

                return requestSuccess(loginUser)
            }else{
                return requestFail(ResultEnum.REQUEST_FAIL)
            }
        }else{
            if (loginUser.password != userInfo.password){
                return requestFail(ResultEnum.PASSWORD_ERROR)
            }

            loginUser.email = userInfo.email


            mIUserInfoService.updateById(loginUser)

            loginUser = mIUserInfoService.ktQuery().eq(UserInfo::id, loginUser.id).one()

            loginUser.password = null

            return requestSuccess(loginUser)
        }

    }

    @PostMapping("/getUserInfo")
    fun getUserInfo(@RequestBody userInfo: UserInfo): String {

        val userId = userInfo.id ?: return requestFail(ResultEnum.REQUEST_FAIL)

        val userInfo = mIUserInfoService.getUserInfo(userId)?: return requestFail(ResultEnum.REQUEST_FAIL)

        return requestSuccess(userInfo)
    }


    @PostMapping("/deleteUser")
    fun deleteUser(@RequestBody userInfo: UserInfo): String {

        val userId = userInfo.id ?: return requestFail(ResultEnum.REQUEST_FAIL)

        val userInfo = mIUserInfoService.getUserInfo(userId)?: return requestFail(ResultEnum.REQUEST_FAIL)

        userInfo.status = false

        mIUserInfoService.updateById(userInfo)

        return requestSuccess(true)
    }

}

