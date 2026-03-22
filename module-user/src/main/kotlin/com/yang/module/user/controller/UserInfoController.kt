package com.yang.module.user.controller;

import com.yang.lib.common.data.ResultEnum
import com.yang.lib.common.helper.requestFail
import com.yang.lib.common.helper.requestSuccess
import com.yang.module.user.entity.UserInfo
import com.yang.module.user.service.IUserInfoService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
    private val mIUserInfoService: IUserInfoService
){

    @PostMapping("/login")
    fun login(@RequestBody userInfo: UserInfo): String{

        if (userInfo.userName.isNullOrEmpty() || userInfo.password.isNullOrEmpty()){ return requestFail(ResultEnum.PARAM_ERROR_FAIL)}

            // 2. 执行登录或注册混合逻辑
            val loginUser = mIUserInfoService.login(userInfo)

            return when {
                loginUser == null -> {

                    requestFail(ResultEnum.REQUEST_FAIL)

                }
                // 如果是已存在的用户，校验密码
                // 注意：新注册的用户直接通过，老用户需要校验密码
                // 这里的逻辑可以根据业务调整，比如：新注册直接登录，老用户密码错则报错
                loginUser.id != null && loginUser.password != userInfo.password -> {
                    requestFail(ResultEnum.PASSWORD_ERROR)
                }
                else -> {
                    // 登录/注册成功
                    loginUser.password = null // 屏蔽敏感信息
                    requestSuccess(loginUser)
                }
            }

    }

    @PostMapping("/getUserInfo")
    fun getUserInfo(@RequestBody userId:Int): String {

        val userInfo = mIUserInfoService.ktQuery().eq(UserInfo::id, userId).one()

        if (null == userInfo) {

            return requestFail(ResultEnum.REQUEST_FAIL)
        }

        return requestSuccess(userInfo)
    }

}

