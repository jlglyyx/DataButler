package com.yang.module.user.config

import ch.qos.logback.classic.pattern.Util
import com.alipay.api.AlipayConfig
import com.alipay.api.AlipayConstants.SIGN_TYPE
import com.alipay.api.DefaultAlipayClient
import com.alipay.api.domain.AlipayTradeAppPayModel
import com.alipay.api.domain.ExtendParams
import com.alipay.api.domain.GoodsDetail
import com.alipay.api.request.AlipayTradeAppPayRequest
import com.alipay.api.response.AlipayTradeAppPayResponse
import com.yang.lib.common.util.dateFormat
import com.yang.module.user.entity.ProductInfo
import org.springframework.stereotype.Component
import java.util.Calendar
import java.util.Date

@Component
class AlipayManager {

    companion object{

        /**
         * 支付宝公钥
         */
        val ALIPAY_PUBLIC_KEY =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiDcpsPuRGZ5gQqRX1yYJ0rh1+cM0MIBp45/eg8BFVwyj/pXM7NwGmQOGn8JT3xqTla8/aPu8O0njjMcbTCQj8GVoCQzzYwhta0Mnow5n2F6DR8BwmtUj//HhnIHEf+Hss7YrDmPuS053R0+JB8C87RnoMTqqhiUDGb8/F3O1UCXGrlie3Kx0UxH1H0+sry1dqlNbzszN+joxDfDApB7iXfXJgMVFPHRSEKBI/eWOGZaCE/8+4f9Yr732Tjdc0I3yrd8m7Vd+w4ta7GPwm0OJwIshGHq+tpVSMJqbvKh8FKlHHGzYTJzGkZ8PibCSOQ1ccCta3uCe/WPZiU8sbPNJIwIDAQAB";

        /* 应用私钥
     */
        val ALIPAY_PRIVATE_KEY =
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDOLozzO5aXyuW6zgIlQjcM1lr91fNDvEotRdaWDPN208BRz/rMaLivgPxBPvir+2C58QzCkFhLz1aKTQe1hYfKnWvmvIphzRjlGZM5ckFcarwhJAuP/3Wh9AnBxjbYyCDkjqQvTRQO4EJ3A5J8kMrF4OoKwmcpedzSby5Ww2bnfbnT8oLFwzVcWLL0xCy6CVnTedmdqtYIHinr5Ij4X6zwHEJxRrtUiNPn63vKeiaZwbFPz+TqDYnge/ITEprFUu6vj+u+sxPDfTab7dZ91cWpLu46z7znYvvmDaPou1iVzqP10EtSYwj5+/YFz3bOXYSSVNYG+7JNXdCgNG+68NQLAgMBAAECggEAQkMHnWv7G9FjP0PRwLOlCYUiPQ2zXoN2u1IEgriqzfeDiUdz7JmDMTQc3cMlG/Bvx5JGbYkCN2rs/wl+fsHpQB6TfNX/Cytk4svzWrTzeIX87BGvfZm17lSFhjbzjXvTxmBpN6Jn3dgIxELrowjNggakcMzFZrj5VM/SbqPhwGc9Ebnwvna3gBiMnGK/mQ2IUE0cyMj18gysLY9tc42C9hTkWGNE88d7MwM3ddTGat0WUXBZ3vkpBV8Jn4b/VYrUtY7YUfko/Q8i/ARjJqjGwEzfxCbb6VcIgzhmOd95OuTbF4m25u62YCEkbST2OSm4ktAMZpKkuEneBH3iZtO3oQKBgQD+OmC98lNo6B679UgrMJeRUilwORz4FxUnj8BgS0aYjgF9ppi/wQTrevg6ltjmHKgMOYkDg/cQSEVG+ux3bZ+Kwsz4M/z3XbnF7yM8BSSJVDB73RxQUOJRInSYpRC87NXZ59302QpKOsdbC6J/R+dVmkZTTkAHDDLrv07KGH7veQKBgQDPnnF7BLGUaVXWwK1gzpchPFTlTEW7lJRxpQArQ+y7wcNSylgOysPH0iso+hyLiRczGElR3Hw7uKQrS0m3Wsk3iBQMWgCIsTe13oLmw+fjzybx7ZqH4ntL6BMn33e3LG9SLl2C+ewwTyVhjjOiotTGEYA22Fi9O2YuMM1y0diqowKBgQC0S5Z8XzUEO0pApm1uI+H577jvm7GNyGD6U5ptMDV6z4sDH3lI1Iy2q9kLRBvC5iPPt6YR1i8LXxWyhxXYhOgWspxGkigfDltHopbsZAFLW2efEGqaEyLNw4/JCZdbRONMzIG+uYXwqsrjKNcvSUEolma1gdMqrF4Yju5DqSeskQKBgDwGz0CGBuGl4s+IPZhzonZVfyYcezzUpGaHEhlwNg3C47oYDoWYUip/tULPrVkadb6sEua8HJ8Xrz9lMRb2BJ8tlL5Nd0IOGtFWV/poQa83wUbJL5iB/qvfmUo57d7j1fuotBieMY92EpS2DG/FLEdn3+37dwyfE9fe/EdqLH6JAoGBAK4eNE2pEn7v50H4m/AlwXMgO+cc3XmPhJjMAw/gs9s3x+3BTqZYgDohsLPFHfAsBwD/4EiJkTST0COz2i4vdh33ll0lCr3IjhN8IORr+aA2Hrr50KZ52JQwKgsQ1IiTf+HMixQIIz4TeJG6FV/okmmV+YkJRFUF5NUrXtq6hcVS";

        /**
         * 支付宝APPID
         */
        val ALIPAY_APPID = "9021000141685310";

        /**
         * 支付宝网关
         */
        val ALIPAY_WEB_WAY = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

        /**
         * 支付宝异步通知回调地址
         */
        //    public static String ALIPAY_NOTIFY_URL = "https://0286-122-233-157-44.ngrok-free.app/api/order/alpayNotice";
        //    public static String ALIPAY_NOTIFY_URL = "https://hzlzddw.cn/api/order/alpayNotice";
        var ALIPAY_NOTIFY_URL: String = "https://5576-115-220-169-37.ngrok-free.app/api/order/alpayNotice"

        /**
         * 支付宝编码
         */
        var ALIPAY_CHARSET: String = "UTF-8"

        /**
         * 支付宝编码
         */
        var ALIPAY_CONTENT_TYPE: String = "json"

        var SIGN_TYPE: String = "RSA2"
    }


    fun createPayInfo(orderNo: String, mProductInfo: ProductInfo): String {
        val alipayConfig = AlipayConfig()
        alipayConfig.serverUrl = ALIPAY_WEB_WAY //("https://openapi.alipay.com/gateway.do");
        alipayConfig.appId = ALIPAY_APPID
        alipayConfig.privateKey = ALIPAY_PRIVATE_KEY
        alipayConfig.format = ALIPAY_CONTENT_TYPE
        alipayConfig.alipayPublicKey = ALIPAY_PUBLIC_KEY
        alipayConfig.charset = ALIPAY_CHARSET
        alipayConfig.signType = SIGN_TYPE

        val alipayClient = DefaultAlipayClient(alipayConfig)

        val request = AlipayTradeAppPayRequest()

        val model = AlipayTradeAppPayModel()


        val goodsListDetail: MutableList<GoodsDetail?> = ArrayList()


        val goodsDetail = GoodsDetail()
        goodsDetail.goodsName = mProductInfo.name
        goodsDetail.quantity = 1
        goodsDetail.price = mProductInfo.price.toString()
        goodsDetail.goodsId = mProductInfo.id.toString()
        goodsListDetail.add(goodsDetail)

        val currentDate = Date()

//         设置商户订单号
        model.outTradeNo = orderNo

        // 设置订单总金额
        model.totalAmount = mProductInfo.price.toString()
        // 设置订单标题
        model.subject = mProductInfo.name


        // 设置订单包含的商品列表信息
        model.goodsDetail = goodsListDetail

        val calendar = Calendar.getInstance()

        calendar.setTime(currentDate)

        calendar.add(Calendar.MINUTE, 30)
        // 设置订单绝对超时时间
        model.timeExpire = calendar.getTime().dateFormat("yyyy-MM-dd HH:mm:ss")

        // 设置业务扩展参数
        val extendParams = ExtendParams()
        extendParams.specifiedSellerName = "梁品购"
        model.extendParams = extendParams

        // 设置商户的原始订单号
        model.merchantOrderNo = orderNo

        // 设置通知参数选项
        val queryOptions: MutableList<String?> = ArrayList()
        queryOptions.add("hyb_amount")
        queryOptions.add("enterprise_pay_info")
        model.queryOptions = queryOptions


        request.notifyUrl = ALIPAY_NOTIFY_URL
        request.bizModel = model

        val response = alipayClient.sdkExecute(request)

        if (response.isSuccess) {
            println("调用成功$response")

            return response.getBody()
        } else {
            println("调用失败 $response")
        }

        return ""
    }
}