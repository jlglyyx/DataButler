package com.yang.lib.common.data

enum class ResultEnum(val code:Int, val message:String,val success:Boolean) {

    // === 成功系列 (后缀 _SUCCESS) ===
    REQUEST_SUCCESS(200, "请求成功", true),
    LOGIN_SUCCESS(200, "登录成功", true),
    REGISTER_SUCCESS(200, "注册成功", true),
    PAY_SUCCESS(200, "支付成功", true),
    RECOVERY_SUCCESS(200, "文件恢复成功", true),

    // === 客户端错误 (后缀 _FAIL) ===
    REQUEST_FAIL(-1, "请求失败", false),
    PARAM_ERROR_FAIL(400, "请求参数错误", false),
    UNAUTHORIZED_FAIL(401, "未登录或授权已过期", false),
    FORBIDDEN_FAIL(403, "权限不足", false),
    NOT_FOUND_FAIL(404, "资源不存在", false),

    // === 业务逻辑错误 (后缀 _ERROR) ===
    USER_NOT_EXIST_ERROR(1001, "用户不存在", false),
    PASSWORD_ERROR(1002, "用户名或密码错误", false),
    USER_BANNED_ERROR(1003, "账户已被封禁", false),
    PHONE_USED_ERROR(1004, "手机号已被占用", false),

    // 商品与订单相关
    PRODUCT_OFFLINE_ERROR(2001, "商品已下架", false),
    ORDER_NOT_FOUND_ERROR(2002, "找不到对应订单", false),
    COUNT_NOT_ENOUGH_ERROR(2003, "权益次数不足", false),
    VIP_EXPIRED_ERROR(2004, "会员已到期", false),
    REFUND_AMOUNT_EXCEED_ERROR(2005, "退款金额大于订单金额", false),
    OPERATOR_PERMISSION_DENIED(2006, "操作人权限不足", false),
    REFUND_FAILED_CONTACT_SERVICE(2007, "退款失败，请将订单号和支付宝截图发送客服处理", false),
    REFUND_AMOUNT_FORMATE_ERROR(2008, "退款金额格式错误", false),

    // === 系统级错误 (后缀 _EXCEPTION) ===
    SYSTEM_INNER_EXCEPTION(500, "系统内部异常", false),
    DATABASE_EXCEPTION(501, "数据库操作异常", false),
    NETWORK_TIMEOUT_EXCEPTION(502, "网络请求超时", false);

    companion object {
        fun getByCode(code: Int): ResultEnum? {
            return ResultEnum.entries.find { it.code == code }
        }
    }
}