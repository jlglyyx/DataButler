package com.yang.module.user.config

import com.alipay.api.AlipayApiException
import com.alipay.api.AlipayClient
import com.alipay.api.AlipayConfig
import com.alipay.api.DefaultAlipayClient
import com.alipay.api.diagnosis.DiagnosisUtils
import com.alipay.api.domain.AlipayTradeAppPayModel
import com.alipay.api.domain.AlipayTradeRefundModel
import com.alipay.api.domain.ExtendParams
import com.alipay.api.domain.GoodsDetail
import com.alipay.api.request.AlipayTradeAppPayRequest
import com.alipay.api.request.AlipayTradeRefundRequest
import com.yang.lib.common.util.dateFormat
import com.yang.lib.common.util.toJson
import com.yang.module.user.entity.OrderInfo
import com.yang.module.user.entity.ProductInfo
import org.springframework.stereotype.Component
import java.util.Calendar
import java.util.Date

@Component
class AlipayManager(private val alipayClient: AlipayClient) {

    companion object {

        /**
         * 支付宝公钥
         */
        val ALIPAY_PUBLIC_KEY =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApJT8DPQlymFiwog2RqK6D1gSMDXcoMIsxuKZeMtQ8Xr9GDLY+TF/nDRuLAJDGAp0rfjkQ1QxH6ml4oR5o1QPcLIPUQ878DuOR0tFTL1A6LW5JulkF9ui4O2rfsjTLU3o5L8DQ8Z7zaY3gIHHhqW6ujOSe808RJ3RSG5sdQm51RADF991I2FCtqlbafcUF5rjpbgnEj7Vx7VC8/mXHkcVQiaG2aPAcJiISbjJ16VP3v/Yz3+a6rNvL3urzkZw0ag1u0Cm/uYe7mBhjY4p//CnoeBdKCcCO7hLEZGEYTkARskvEgQzpUSXVeRP6h+ZotMV/GTOzFyoplOVOmqza5jf/wIDAQAB"

        /* 应用私钥
     */
        val ALIPAY_PRIVATE_KEY =
            "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDgHal1DJ1ITJBt3GIgA77MlXNwq+obPo5eKn113PgK96XuORyqS91ue+QL1j0meMlT7LT2iu9EjWKXX5vPXJoG9enjk/i2V2elNtSwSgATtBe7m257UcWShr6hkPugPHbKH5/pO0AV0foXv+gQs58QrpTHYh5KMN42oz8b/7eNVHLs8SlStr6Fkt9X3WZAeUed3+hawdtygu2Z9BBMcgs0QreF/nmTunjk+QebocAc08ksC4H63iPILNXkZ/2pTefmCufS2iNK36RtLAvLhkjhevHvvR42EksDOwXcImAF5uow53KifvMJP0u4XbeLAwA8/HOKFoJVWCw+YtNpmS4nAgMBAAECggEAMz2U6PYhX85hlzy0Eq3Iie4BntkvhVsgIdeFJ9WFwLwua/ES5LzDu+cHlM0+3fTv0whjt5q8aegJiqKJdTeu37iJ6UaDruz8/YEzz7qbmeWrJ3G6KOfhTEcSfs4vOcuZPU1Nun2ZgWOo0pVF8cufqqi/y3Zbhor1w3g/jZ9B0EX5E9pGaNvvXssms80D15k9kZTo13sinstKrad4UvGND7mqgYdli6Hd4Lo3L0iFBDsd9RByNMiNFu4iZJZzuBBMwhpB68t0SwB0QjaDlGLvTkUNlGjam2G3Cx612vOCHqhK7J4saRKs5avhB4RLto738q7bpQAflnTZ6rsTzvihwQKBgQD0K2lVYRorxlmCyJ7Jv5gzN/eTBOWnvtTWo2CJ6B4S5UTg4PaSp5fEmILucvK8oug4/0LwhkJzSUDsn3VcmG54oLBgtRr8pJCnidmKPzKzmFtATngNyuSdXz6uLblX/C1OjM7Xxu+RZh/XJJpVsu9YSiFHTEMKlOSg9qFX74sGswKBgQDq+YMBZ4aQYHZc/Mw3ji/a8dMbTvVQpRdOtkzlerYbANU05g7Adrz9PbLJChKURmPNrW0BcWrc7OSlBRqrwJLP3Kyaa9XWlQGA39uiBzn2CKxoWE9U9Y9/xTyzo1DjpPOIbAn2cg82B9eVmHS8jNuBpnkQ1vqdxde5Nizv5HPUvQKBgDTL6kPe8kTC5/oCnWssdaGlmW/94BbMicM7opVPTqlxT9p8anS9rnCBNdaPnnMxmpswfu35agb5sQM5W6xA/aoxvNG8d3BnuSuxAAwHrFPvPtQB462/0H1C6g/JrUkcK0PP6K35wzWiaoIf1Qtw3JYvuVBsahawqVsw2SRllJ9HAoGAZqKJ2Ikxu8qGqOM7CGExAf9y6JC6knkqDxWNzQammHDqr+p1MxgqQKs8dpfRubZtTZ+c52508P8AeBwcpDy/Y8/ODL1hcBMxzur48nMZnB/5vrIkJMYF4hdzBjwlzulz/njCeUUk48LVJeCrzNfx4VWnt/VPDq8mJR5GzpoJBjkCgYA5pAMiioZPzsMUiOlllp9q241e4A+zoZHfWWmz68LDzCdKtM5eY77vl9JXqKtV48n5lIzbiAbkrn6A+y0Oj+HIsE9ORbKXAM8pE+wwtF6S0Cey94G9ymU1D9K/Op7vQB4051mKo9Aotu2kfHpzd1YCbJnJzNpVuFLwTIgMvvy7zg=="

        /**
         * 支付宝APPID
         */
        val ALIPAY_APPID = "2021006146603479"

        /**
         * 支付宝网关
         */
        val ALIPAY_WEB_WAY = "https://openapi.alipay.com/gateway.do"

        /**
         * 支付宝异步通知回调地址
         */

        var ALIPAY_NOTIFY_URL: String = "https://databox.hzlzddw.cn/api/order/alpayNotice"
//        /**
//         * 支付宝公钥
//         */
//        val ALIPAY_PUBLIC_KEY =
//            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiDcpsPuRGZ5gQqRX1yYJ0rh1+cM0MIBp45/eg8BFVwyj/pXM7NwGmQOGn8JT3xqTla8/aPu8O0njjMcbTCQj8GVoCQzzYwhta0Mnow5n2F6DR8BwmtUj//HhnIHEf+Hss7YrDmPuS053R0+JB8C87RnoMTqqhiUDGb8/F3O1UCXGrlie3Kx0UxH1H0+sry1dqlNbzszN+joxDfDApB7iXfXJgMVFPHRSEKBI/eWOGZaCE/8+4f9Yr732Tjdc0I3yrd8m7Vd+w4ta7GPwm0OJwIshGHq+tpVSMJqbvKh8FKlHHGzYTJzGkZ8PibCSOQ1ccCta3uCe/WPZiU8sbPNJIwIDAQAB";
//
//        /* 应用私钥
//     */
//        val ALIPAY_PRIVATE_KEY =
//            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDOLozzO5aXyuW6zgIlQjcM1lr91fNDvEotRdaWDPN208BRz/rMaLivgPxBPvir+2C58QzCkFhLz1aKTQe1hYfKnWvmvIphzRjlGZM5ckFcarwhJAuP/3Wh9AnBxjbYyCDkjqQvTRQO4EJ3A5J8kMrF4OoKwmcpedzSby5Ww2bnfbnT8oLFwzVcWLL0xCy6CVnTedmdqtYIHinr5Ij4X6zwHEJxRrtUiNPn63vKeiaZwbFPz+TqDYnge/ITEprFUu6vj+u+sxPDfTab7dZ91cWpLu46z7znYvvmDaPou1iVzqP10EtSYwj5+/YFz3bOXYSSVNYG+7JNXdCgNG+68NQLAgMBAAECggEAQkMHnWv7G9FjP0PRwLOlCYUiPQ2zXoN2u1IEgriqzfeDiUdz7JmDMTQc3cMlG/Bvx5JGbYkCN2rs/wl+fsHpQB6TfNX/Cytk4svzWrTzeIX87BGvfZm17lSFhjbzjXvTxmBpN6Jn3dgIxELrowjNggakcMzFZrj5VM/SbqPhwGc9Ebnwvna3gBiMnGK/mQ2IUE0cyMj18gysLY9tc42C9hTkWGNE88d7MwM3ddTGat0WUXBZ3vkpBV8Jn4b/VYrUtY7YUfko/Q8i/ARjJqjGwEzfxCbb6VcIgzhmOd95OuTbF4m25u62YCEkbST2OSm4ktAMZpKkuEneBH3iZtO3oQKBgQD+OmC98lNo6B679UgrMJeRUilwORz4FxUnj8BgS0aYjgF9ppi/wQTrevg6ltjmHKgMOYkDg/cQSEVG+ux3bZ+Kwsz4M/z3XbnF7yM8BSSJVDB73RxQUOJRInSYpRC87NXZ59302QpKOsdbC6J/R+dVmkZTTkAHDDLrv07KGH7veQKBgQDPnnF7BLGUaVXWwK1gzpchPFTlTEW7lJRxpQArQ+y7wcNSylgOysPH0iso+hyLiRczGElR3Hw7uKQrS0m3Wsk3iBQMWgCIsTe13oLmw+fjzybx7ZqH4ntL6BMn33e3LG9SLl2C+ewwTyVhjjOiotTGEYA22Fi9O2YuMM1y0diqowKBgQC0S5Z8XzUEO0pApm1uI+H577jvm7GNyGD6U5ptMDV6z4sDH3lI1Iy2q9kLRBvC5iPPt6YR1i8LXxWyhxXYhOgWspxGkigfDltHopbsZAFLW2efEGqaEyLNw4/JCZdbRONMzIG+uYXwqsrjKNcvSUEolma1gdMqrF4Yju5DqSeskQKBgDwGz0CGBuGl4s+IPZhzonZVfyYcezzUpGaHEhlwNg3C47oYDoWYUip/tULPrVkadb6sEua8HJ8Xrz9lMRb2BJ8tlL5Nd0IOGtFWV/poQa83wUbJL5iB/qvfmUo57d7j1fuotBieMY92EpS2DG/FLEdn3+37dwyfE9fe/EdqLH6JAoGBAK4eNE2pEn7v50H4m/AlwXMgO+cc3XmPhJjMAw/gs9s3x+3BTqZYgDohsLPFHfAsBwD/4EiJkTST0COz2i4vdh33ll0lCr3IjhN8IORr+aA2Hrr50KZ52JQwKgsQ1IiTf+HMixQIIz4TeJG6FV/okmmV+YkJRFUF5NUrXtq6hcVS";
//
//        /**
//         * 支付宝APPID
//         */
//        val ALIPAY_APPID = "9021000141685310";
//
//        /**
//         * 支付宝网关
//         */
//        val ALIPAY_WEB_WAY = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
//
//        /**
//         * 支付宝异步通知回调地址
//         */
//
//        var ALIPAY_NOTIFY_URL: String = "https://abee-115-206-193-224.ngrok-free.app/api/order/alpayNotice"

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


    fun reBackAlipay(mOrderInfo: OrderInfo, refundReason: String = "系统退款"): Boolean {
        try {


            // 初始化SDK

            // 构造请求参数以调用接口
            val request = AlipayTradeRefundRequest()

            val model = AlipayTradeRefundModel()


            // 设置商户订单号
            model.outTradeNo = mOrderInfo.orderNo

            // 设置支付宝交易号
            model.tradeNo = mOrderInfo.alipayTradeNo

            // 设置退款金额
            model.refundAmount = mOrderInfo.rebackAmount.toString()

            // 设置退款原因说明
            model.refundReason = refundReason

// out_request_no: 标识一次退款请求，同一笔交易多次退款需要保证唯一。
            //                由于你要求“一个订单只支持一次退款”，可以直接用商户订单号作为请求号。
            model.outRequestNo = mOrderInfo.orderNo

//            // 设置退款请求号
//            val refundGoodsDetail: MutableList<RefundGoodsDetail?> = ArrayList()
//
//            val refundGoodsDetail0 = RefundGoodsDetail()
//            refundGoodsDetail0.outItemId = mUserOrders.productId.toString()
//            refundGoodsDetail0.refundAmount = mUserOrders.totalAmount.toString()
//            refundGoodsDetail.add(refundGoodsDetail0)
//            model.refundGoodsDetail = refundGoodsDetail


            // 设置查询选项
            val queryOptions: MutableList<String?> = ArrayList()

            queryOptions.add("refund_detail_item_list")

            queryOptions.add("deposit_back_info")

            queryOptions.add("refund_voucher_detail_list")

            model.queryOptions = queryOptions

            request.bizModel = model

            println("================开始退款===================")

            val response = alipayClient.execute(request)


            if (response.isSuccess) {
                println("================调用成功=================== \n" + response.toJson())

                return true
            } else {
                val diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response)

                println("================调用失败=================== " + diagnosisUrl + "\n" + response.toJson())

                return false
            }
        } catch (e: AlipayApiException) {
            println("================支付宝退款失败===================：" + e.message)

            return false
        }
    }
}