package com.yang.module.user.config

import com.alibaba.fastjson.JSON.toJSONString
import com.alipay.api.AlipayApiException
import com.alipay.api.AlipayClient
import com.alipay.api.AlipayConfig
import com.alipay.api.DefaultAlipayClient
import com.alipay.api.diagnosis.DiagnosisUtils
import com.alipay.api.domain.AlipayTradeAppPayModel
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel
import com.alipay.api.domain.AlipayTradeQueryModel
import com.alipay.api.domain.AlipayTradeRefundModel
import com.alipay.api.domain.ExtendParams
import com.alipay.api.domain.GoodsDetail
import com.alipay.api.domain.RefundGoodsDetail
import com.alipay.api.request.AlipayTradeAppPayRequest
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest
import com.alipay.api.request.AlipayTradeQueryRequest
import com.alipay.api.request.AlipayTradeRefundRequest
import com.alipay.api.response.AlipayTradeAppPayResponse
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse
import com.alipay.api.response.AlipayTradeQueryResponse
import com.alipay.api.response.AlipayTradeRefundResponse
import jakarta.annotation.PostConstruct
import jakarta.annotation.Resource
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.lang.Double
import java.lang.Long
import java.math.BigDecimal
import java.util.*
import kotlin.Boolean
import kotlin.Exception
import kotlin.String
import kotlin.Throws
import kotlin.toString

//@Component
//class AliConstant {
//
//
//    @Value("\${spring.profiles.active:dev}")
//    private val env: String? = null
//
//
//    @PostConstruct
//    fun init() {
//        if ("prod".equals(env, ignoreCase = true)) {
//            ALIPAY_PUBLIC_KEY =
//                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkboibY5IyCQlYJLKd9LsT5F+C4+D+f/meXuYdaoyxJoXm4JW11ZxyKnwZU+eO6ucfsm0CHvAlj6G8mw7JScXhD2VVWVsfkx5reUmqTn9HNN5oeDag9DiR2CoveuLNGLjZTjKhtvy8jGaPKMyzDuc47DRQyU3zt3r+lTQ0iBNBTA/RbrJ5h4YGz9DsJ0eyqGxfwSsw2sf8rBXiUfMz69DAlAHwB7Z2ncM0woGVd5P8FtKMMnOVcInbOkZuuYyJNLAUBv2bwbp3T3NJg2HwsMvxZ1j095T31FUgEikctZOvxtensgYE0ZTKNyxh4y4SLg1i3bN5mn4OXs5wnBF1YMVuQIDAQAB"
//            ALIPAY_PRIVATE_KEY =
//                "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCyqAmmv0+okgiorXJT7365hV56KezbCEpB9g7PlddJ4fM2CZQed6k76/ih0eLSOc9JWDTW34vdCaqezqNo+uGb6/yPHwWgevRyURNQJLzE0hB/2Xhs9EIKafo+lSaY9QS6tJXYq84YGt/whh6RVW0f8uu7k3n5Q4GKL+T8rb1nlAx7IAxPwpTBzyg2xX8goJhUzvK0KlzKwbHVnKOoOo7IT6Dj3UeGxsqZ5oFIJ71xQ2CXL8RNp2X15n9Wf1Xz27QAp36wnv4WJ+WkOn4i9QmE33gaiOtld/ff9KxhWtRu45wKYR0udKfI3aamUwA7knpZZyHsGaHGGIcsw2lipg43AgMBAAECggEBAIVqQVLZuTYMYBT++cq+pecLNuOJ3YwwI6wggSz6G/zblFF9U7Rw7duGu3S0lWsiqq5+BW+VHxBhAJ55d8SU3dgfocqLgxs11shrzT1jQejFanbfQzQyMAMw4yTLIzjEeNSouOJGYrDTmPo4XilwHv7l3K6GUmcXx2wXmiwr3q6yt3L0y2ewtf9QPd7fx9RU9pRHaLh96e619l2uQbTPl3cYj+vEhTGQNNZIjjRAE7tHLUaEllWpZKnHAyIw1pTvXMSkkPknKDAjY2QtaZgrVf8EQ8nUcKkVBlYaIEfcrSM8NVYOf/pnMu4II4WzEpOs1zTPAg06yFZ0Y2pLZfAm+PkCgYEA/i4Ik8KrZsVr4/i/dkcoG115Ji+yScHI7+mzabOl4ry6/eyMCFFy4WrCzOEqPvyqHuAnfK13QTAC0YLvhBrUmG1Nk5slIty54f6EmLu4H1EKv5EGCRY8/jVVcdacq5Kn/FFUm7EsO+FaS8kFgO+C+w3I/YoZea8z5Cb5g5z68g0CgYEAs++Nr1CglBCJFuVeZ7GM5NfV3UGpz8uUGIeQyZT/O3Ls1oo4xFuKtN5TtICxXXvkDy6Sf7gq292KGiuwtPWVOtMExgZ1LaDtpzYKGj8zrSmdJQ35e/4Xj4XOJu6KjtG7n6CXLdbRPyiy5mArL+PMXFcBCT/dh/KWb+y5RIUO5FMCgYEAvWwA6F0C7yvhI5uU08hjk0Etr8+LxnusC17WXceLsFWF5QYLtYc9Xc/6LEUA/r+86BWUV83OOb5V210AsipslMugLIhKPJjRziGnH0uGA5CZufmRzePMwIVuj8+oIhTBK8BQlVaL6HQgnqnIeDYGHqI/TvND2PNAcJ67A+JRAWECgYEArAXmv+fmTQft+Cy674moOJQ8SzZeoWFwqW7Ztk6zIGPEF2u3z8nwtvAiuFuctdIe+4C74Ezy9JY9VL3WR3rN+5oIY0WDO2cyLX0VdXhc8DzrlqRgkvEeF0GGEGRXpSjQJlECl3lbPPWs62CfiT5QfytSu0QKh9Edj82SZfaLgS8CgYBWFej81VX25Bn+LAJNUFMAikPbg1ENEK3E+AO1Bdk+Sz3bpNm1NUc/v9lwDJkVPgiKKloCoiyIJrHIH+bniq1LTnKEHIobWk5xX7bvs5QONauy2NSxPtedlC4mM/KR5xPxtwcZ9c8Ts1eW9jx6pVdMWEHDqoER255y23pXO2Ipdg=="
//            ALIPAY_APPID = "2021005148609055"
//            ALIPAY_WEB_WAY = "https://openapi.alipay.com/gateway.do"
//            ALIPAY_NOTIFY_URL = "https://hzlzddw.cn/api/order/alpayNotice"
//        } else {
//            ALIPAY_PUBLIC_KEY =
//                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiDcpsPuRGZ5gQqRX1yYJ0rh1+cM0MIBp45/eg8BFVwyj/pXM7NwGmQOGn8JT3xqTla8/aPu8O0njjMcbTCQj8GVoCQzzYwhta0Mnow5n2F6DR8BwmtUj//HhnIHEf+Hss7YrDmPuS053R0+JB8C87RnoMTqqhiUDGb8/F3O1UCXGrlie3Kx0UxH1H0+sry1dqlNbzszN+joxDfDApB7iXfXJgMVFPHRSEKBI/eWOGZaCE/8+4f9Yr732Tjdc0I3yrd8m7Vd+w4ta7GPwm0OJwIshGHq+tpVSMJqbvKh8FKlHHGzYTJzGkZ8PibCSOQ1ccCta3uCe/WPZiU8sbPNJIwIDAQAB"
//            ALIPAY_PRIVATE_KEY =
//                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDOLozzO5aXyuW6zgIlQjcM1lr91fNDvEotRdaWDPN208BRz/rMaLivgPxBPvir+2C58QzCkFhLz1aKTQe1hYfKnWvmvIphzRjlGZM5ckFcarwhJAuP/3Wh9AnBxjbYyCDkjqQvTRQO4EJ3A5J8kMrF4OoKwmcpedzSby5Ww2bnfbnT8oLFwzVcWLL0xCy6CVnTedmdqtYIHinr5Ij4X6zwHEJxRrtUiNPn63vKeiaZwbFPz+TqDYnge/ITEprFUu6vj+u+sxPDfTab7dZ91cWpLu46z7znYvvmDaPou1iVzqP10EtSYwj5+/YFz3bOXYSSVNYG+7JNXdCgNG+68NQLAgMBAAECggEAQkMHnWv7G9FjP0PRwLOlCYUiPQ2zXoN2u1IEgriqzfeDiUdz7JmDMTQc3cMlG/Bvx5JGbYkCN2rs/wl+fsHpQB6TfNX/Cytk4svzWrTzeIX87BGvfZm17lSFhjbzjXvTxmBpN6Jn3dgIxELrowjNggakcMzFZrj5VM/SbqPhwGc9Ebnwvna3gBiMnGK/mQ2IUE0cyMj18gysLY9tc42C9hTkWGNE88d7MwM3ddTGat0WUXBZ3vkpBV8Jn4b/VYrUtY7YUfko/Q8i/ARjJqjGwEzfxCbb6VcIgzhmOd95OuTbF4m25u62YCEkbST2OSm4ktAMZpKkuEneBH3iZtO3oQKBgQD+OmC98lNo6B679UgrMJeRUilwORz4FxUnj8BgS0aYjgF9ppi/wQTrevg6ltjmHKgMOYkDg/cQSEVG+ux3bZ+Kwsz4M/z3XbnF7yM8BSSJVDB73RxQUOJRInSYpRC87NXZ59302QpKOsdbC6J/R+dVmkZTTkAHDDLrv07KGH7veQKBgQDPnnF7BLGUaVXWwK1gzpchPFTlTEW7lJRxpQArQ+y7wcNSylgOysPH0iso+hyLiRczGElR3Hw7uKQrS0m3Wsk3iBQMWgCIsTe13oLmw+fjzybx7ZqH4ntL6BMn33e3LG9SLl2C+ewwTyVhjjOiotTGEYA22Fi9O2YuMM1y0diqowKBgQC0S5Z8XzUEO0pApm1uI+H577jvm7GNyGD6U5ptMDV6z4sDH3lI1Iy2q9kLRBvC5iPPt6YR1i8LXxWyhxXYhOgWspxGkigfDltHopbsZAFLW2efEGqaEyLNw4/JCZdbRONMzIG+uYXwqsrjKNcvSUEolma1gdMqrF4Yju5DqSeskQKBgDwGz0CGBuGl4s+IPZhzonZVfyYcezzUpGaHEhlwNg3C47oYDoWYUip/tULPrVkadb6sEua8HJ8Xrz9lMRb2BJ8tlL5Nd0IOGtFWV/poQa83wUbJL5iB/qvfmUo57d7j1fuotBieMY92EpS2DG/FLEdn3+37dwyfE9fe/EdqLH6JAoGBAK4eNE2pEn7v50H4m/AlwXMgO+cc3XmPhJjMAw/gs9s3x+3BTqZYgDohsLPFHfAsBwD/4EiJkTST0COz2i4vdh33ll0lCr3IjhN8IORr+aA2Hrr50KZ52JQwKgsQ1IiTf+HMixQIIz4TeJG6FV/okmmV+YkJRFUF5NUrXtq6hcVS"
//            ALIPAY_APPID = "9021000141685310"
//            ALIPAY_WEB_WAY = "https://openapi-sandbox.dl.alipaydev.com/gateway.do"
//            ALIPAY_NOTIFY_URL = "https://0286-122-233-157-44.ngrok-free.app/api/order/alpayNotice"
//        }
//    }
//
//
//    companion object {
//        /**
//         * 支付宝公钥
//         */
//        //    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiDcpsPuRGZ5gQqRX1yYJ0rh1+cM0MIBp45/eg8BFVwyj/pXM7NwGmQOGn8JT3xqTla8/aPu8O0njjMcbTCQj8GVoCQzzYwhta0Mnow5n2F6DR8BwmtUj//HhnIHEf+Hss7YrDmPuS053R0+JB8C87RnoMTqqhiUDGb8/F3O1UCXGrlie3Kx0UxH1H0+sry1dqlNbzszN+joxDfDApB7iXfXJgMVFPHRSEKBI/eWOGZaCE/8+4f9Yr732Tjdc0I3yrd8m7Vd+w4ta7GPwm0OJwIshGHq+tpVSMJqbvKh8FKlHHGzYTJzGkZ8PibCSOQ1ccCta3uCe/WPZiU8sbPNJIwIDAQAB";
//        var ALIPAY_PUBLIC_KEY: String = ""
//
//        //        public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkboibY5IyCQlYJLKd9LsT5F+C4+D+f/meXuYdaoyxJoXm4JW11ZxyKnwZU+eO6ucfsm0CHvAlj6G8mw7JScXhD2VVWVsfkx5reUmqTn9HNN5oeDag9DiR2CoveuLNGLjZTjKhtvy8jGaPKMyzDuc47DRQyU3zt3r+lTQ0iBNBTA/RbrJ5h4YGz9DsJ0eyqGxfwSsw2sf8rBXiUfMz69DAlAHwB7Z2ncM0woGVd5P8FtKMMnOVcInbOkZuuYyJNLAUBv2bwbp3T3NJg2HwsMvxZ1j095T31FUgEikctZOvxtensgYE0ZTKNyxh4y4SLg1i3bN5mn4OXs5wnBF1YMVuQIDAQAB";
//        /* 应用私钥
//     */
//        //    public static String ALIPAY_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDOLozzO5aXyuW6zgIlQjcM1lr91fNDvEotRdaWDPN208BRz/rMaLivgPxBPvir+2C58QzCkFhLz1aKTQe1hYfKnWvmvIphzRjlGZM5ckFcarwhJAuP/3Wh9AnBxjbYyCDkjqQvTRQO4EJ3A5J8kMrF4OoKwmcpedzSby5Ww2bnfbnT8oLFwzVcWLL0xCy6CVnTedmdqtYIHinr5Ij4X6zwHEJxRrtUiNPn63vKeiaZwbFPz+TqDYnge/ITEprFUu6vj+u+sxPDfTab7dZ91cWpLu46z7znYvvmDaPou1iVzqP10EtSYwj5+/YFz3bOXYSSVNYG+7JNXdCgNG+68NQLAgMBAAECggEAQkMHnWv7G9FjP0PRwLOlCYUiPQ2zXoN2u1IEgriqzfeDiUdz7JmDMTQc3cMlG/Bvx5JGbYkCN2rs/wl+fsHpQB6TfNX/Cytk4svzWrTzeIX87BGvfZm17lSFhjbzjXvTxmBpN6Jn3dgIxELrowjNggakcMzFZrj5VM/SbqPhwGc9Ebnwvna3gBiMnGK/mQ2IUE0cyMj18gysLY9tc42C9hTkWGNE88d7MwM3ddTGat0WUXBZ3vkpBV8Jn4b/VYrUtY7YUfko/Q8i/ARjJqjGwEzfxCbb6VcIgzhmOd95OuTbF4m25u62YCEkbST2OSm4ktAMZpKkuEneBH3iZtO3oQKBgQD+OmC98lNo6B679UgrMJeRUilwORz4FxUnj8BgS0aYjgF9ppi/wQTrevg6ltjmHKgMOYkDg/cQSEVG+ux3bZ+Kwsz4M/z3XbnF7yM8BSSJVDB73RxQUOJRInSYpRC87NXZ59302QpKOsdbC6J/R+dVmkZTTkAHDDLrv07KGH7veQKBgQDPnnF7BLGUaVXWwK1gzpchPFTlTEW7lJRxpQArQ+y7wcNSylgOysPH0iso+hyLiRczGElR3Hw7uKQrS0m3Wsk3iBQMWgCIsTe13oLmw+fjzybx7ZqH4ntL6BMn33e3LG9SLl2C+ewwTyVhjjOiotTGEYA22Fi9O2YuMM1y0diqowKBgQC0S5Z8XzUEO0pApm1uI+H577jvm7GNyGD6U5ptMDV6z4sDH3lI1Iy2q9kLRBvC5iPPt6YR1i8LXxWyhxXYhOgWspxGkigfDltHopbsZAFLW2efEGqaEyLNw4/JCZdbRONMzIG+uYXwqsrjKNcvSUEolma1gdMqrF4Yju5DqSeskQKBgDwGz0CGBuGl4s+IPZhzonZVfyYcezzUpGaHEhlwNg3C47oYDoWYUip/tULPrVkadb6sEua8HJ8Xrz9lMRb2BJ8tlL5Nd0IOGtFWV/poQa83wUbJL5iB/qvfmUo57d7j1fuotBieMY92EpS2DG/FLEdn3+37dwyfE9fe/EdqLH6JAoGBAK4eNE2pEn7v50H4m/AlwXMgO+cc3XmPhJjMAw/gs9s3x+3BTqZYgDohsLPFHfAsBwD/4EiJkTST0COz2i4vdh33ll0lCr3IjhN8IORr+aA2Hrr50KZ52JQwKgsQ1IiTf+HMixQIIz4TeJG6FV/okmmV+YkJRFUF5NUrXtq6hcVS";
//        var ALIPAY_PRIVATE_KEY: String = ""
//        //    public static String ALIPAY_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCyqAmmv0+okgiorXJT7365hV56KezbCEpB9g7PlddJ4fM2CZQed6k76/ih0eLSOc9JWDTW34vdCaqezqNo+uGb6/yPHwWgevRyURNQJLzE0hB/2Xhs9EIKafo+lSaY9QS6tJXYq84YGt/whh6RVW0f8uu7k3n5Q4GKL+T8rb1nlAx7IAxPwpTBzyg2xX8goJhUzvK0KlzKwbHVnKOoOo7IT6Dj3UeGxsqZ5oFIJ71xQ2CXL8RNp2X15n9Wf1Xz27QAp36wnv4WJ+WkOn4i9QmE33gaiOtld/ff9KxhWtRu45wKYR0udKfI3aamUwA7knpZZyHsGaHGGIcsw2lipg43AgMBAAECggEBAIVqQVLZuTYMYBT++cq+pecLNuOJ3YwwI6wggSz6G/zblFF9U7Rw7duGu3S0lWsiqq5+BW+VHxBhAJ55d8SU3dgfocqLgxs11shrzT1jQejFanbfQzQyMAMw4yTLIzjEeNSouOJGYrDTmPo4XilwHv7l3K6GUmcXx2wXmiwr3q6yt3L0y2ewtf9QPd7fx9RU9pRHaLh96e619l2uQbTPl3cYj+vEhTGQNNZIjjRAE7tHLUaEllWpZKnHAyIw1pTvXMSkkPknKDAjY2QtaZgrVf8EQ8nUcKkVBlYaIEfcrSM8NVYOf/pnMu4II4WzEpOs1zTPAg06yFZ0Y2pLZfAm+PkCgYEA/i4Ik8KrZsVr4/i/dkcoG115Ji+yScHI7+mzabOl4ry6/eyMCFFy4WrCzOEqPvyqHuAnfK13QTAC0YLvhBrUmG1Nk5slIty54f6EmLu4H1EKv5EGCRY8/jVVcdacq5Kn/FFUm7EsO+FaS8kFgO+C+w3I/YoZea8z5Cb5g5z68g0CgYEAs++Nr1CglBCJFuVeZ7GM5NfV3UGpz8uUGIeQyZT/O3Ls1oo4xFuKtN5TtICxXXvkDy6Sf7gq292KGiuwtPWVOtMExgZ1LaDtpzYKGj8zrSmdJQ35e/4Xj4XOJu6KjtG7n6CXLdbRPyiy5mArL+PMXFcBCT/dh/KWb+y5RIUO5FMCgYEAvWwA6F0C7yvhI5uU08hjk0Etr8+LxnusC17WXceLsFWF5QYLtYc9Xc/6LEUA/r+86BWUV83OOb5V210AsipslMugLIhKPJjRziGnH0uGA5CZufmRzePMwIVuj8+oIhTBK8BQlVaL6HQgnqnIeDYGHqI/TvND2PNAcJ67A+JRAWECgYEArAXmv+fmTQft+Cy674moOJQ8SzZeoWFwqW7Ztk6zIGPEF2u3z8nwtvAiuFuctdIe+4C74Ezy9JY9VL3WR3rN+5oIY0WDO2cyLX0VdXhc8DzrlqRgkvEeF0GGEGRXpSjQJlECl3lbPPWs62CfiT5QfytSu0QKh9Edj82SZfaLgS8CgYBWFej81VX25Bn+LAJNUFMAikPbg1ENEK3E+AO1Bdk+Sz3bpNm1NUc/v9lwDJkVPgiKKloCoiyIJrHIH+bniq1LTnKEHIobWk5xX7bvs5QONauy2NSxPtedlC4mM/KR5xPxtwcZ9c8Ts1eW9jx6pVdMWEHDqoER255y23pXO2Ipdg==";
//        /**
//         * 支付宝APPID
//         */
//        //    public static String ALIPAY_APPID = "9021000141685310";
//        var ALIPAY_APPID: String = ""
//        //    public static String ALIPAY_APPID = "2021005148609055";
//        /**
//         * 支付宝网关
//         */
//        //    public static String ALIPAY_WEB_WAY = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
//        var ALIPAY_WEB_WAY: String = ""
//
//        //    public static String ALIPAY_WEB_WAY = "https://openapi.alipay.com/gateway.do";
//        /**
//         * 支付宝异步通知回调地址
//         */
//        //    public static String ALIPAY_NOTIFY_URL = "https://0286-122-233-157-44.ngrok-free.app/api/order/alpayNotice";
//        //    public static String ALIPAY_NOTIFY_URL = "https://hzlzddw.cn/api/order/alpayNotice";
//        var ALIPAY_NOTIFY_URL: String = ""
//
//
//        /**
//         * 支付宝编码
//         */
//        var ALIPAY_CHARSET: String = "UTF-8"
//
//        /**
//         * 支付宝编码
//         */
//        var ALIPAY_CONTENT_TYPE: String = "json"
//
//        var SIGN_TYPE: String = "RSA2"
//
//
//        val alipayConfig: AlipayConfig
//            get() {
//                val alipayConfig = AlipayConfig()
//                alipayConfig.setServerUrl(ALIPAY_WEB_WAY) //("https://openapi.alipay.com/gateway.do");
//                alipayConfig.setAppId(ALIPAY_APPID)
//                alipayConfig.setPrivateKey(ALIPAY_PRIVATE_KEY)
//                alipayConfig.setFormat(ALIPAY_CONTENT_TYPE)
//                alipayConfig.setAlipayPublicKey(ALIPAY_PUBLIC_KEY)
//                alipayConfig.setCharset(ALIPAY_CHARSET)
//                alipayConfig.setSignType(SIGN_TYPE)
//                return alipayConfig
//            }
//
//
//        @Throws(AlipayApiException::class)
//        fun createAliPayOrderId(userOrders: MutableList<UserOrder>): String? {
//            val goodsListDetail: MutableList<GoodsDetail?> = ArrayList<GoodsDetail?>()
//
//            val titles: MutableList<String?> = ArrayList<String?>()
//
//            for (mUserOrder in userOrders) {
//                titles.add(mUserOrder.getProductName())
//                val goodsDetail = GoodsDetail()
//                goodsDetail.setGoodsName(mUserOrder.getProductName())
//                goodsDetail.setQuantity(Long.valueOf(mUserOrder.getQuantity()))
//                goodsDetail.setPrice(Util.formatPrice(Double.valueOf(mUserOrder.getItemTotal())))
//                goodsDetail.setGoodsId(mUserOrder.getProductId())
//                goodsDetail.setShowUrl(mUserOrder.getProductImg())
//                goodsListDetail.add(goodsDetail)
//            }
//
//
//            val totalPrice: String? = BigDecimal(userOrders.get(0).getOrderTotal()).toString()
//
//            val orderId: String? = userOrders.get(0).getOrderId()
//
//            val currentDate = Date()
//
//            // 初始化SDK
//            val alipayClient: AlipayClient = DefaultAlipayClient(alipayConfig)
//
//
//            // 构造请求参数以调用接口
//            val request = AlipayTradeAppPayRequest()
//
//
//            val model = AlipayTradeAppPayModel()
//
//            // 设置商户订单号
//            model.setOutTradeNo(orderId)
//
//            // 设置订单总金额
//            model.setTotalAmount(totalPrice)
//            // 设置订单标题
//            model.setSubject("LPG:" + Util.formatWithSymbol(titles, ","))
//
//
//            // 设置订单包含的商品列表信息
//            model.setGoodsDetail(goodsListDetail)
//
//            val calendar = Calendar.getInstance()
//
//            calendar.setTime(currentDate)
//
//            calendar.add(Calendar.MINUTE, 30)
//            // 设置订单绝对超时时间
//            model.setTimeExpire(Util.dateFormat(calendar.getTime(), "yyyy-MM-dd HH:mm:ss"))
//
//            // 设置业务扩展参数
//            val extendParams = ExtendParams()
//            extendParams.setSpecifiedSellerName("梁品购")
//            model.setExtendParams(extendParams)
//
//            // 设置商户的原始订单号
//            model.setMerchantOrderNo(orderId)
//
//            // 设置通知参数选项
//            val queryOptions: MutableList<String?> = ArrayList<String?>()
//            queryOptions.add("hyb_amount")
//            queryOptions.add("enterprise_pay_info")
//            model.setQueryOptions(queryOptions)
//
//            request.setNotifyUrl(ALIPAY_NOTIFY_URL)
//
//            request.setBizModel(model)
//
//            val response = alipayClient.sdkExecute<AlipayTradeAppPayResponse?>(request)
//
//            if (response.isSuccess()) {
//                println("调用成功" + response)
//
//                return response.getBody()
//            } else {
//                println("调用失败 " + response)
//            }
//
//            return ""
//        }
//
//
//        fun reBackAlipay(mUserOrders: MutableList<UserOrder>, refundReason: String?, mOutRequestNo: String?): Boolean {
//            try {
//                var countPrice = BigDecimal.valueOf(0.0)
//
//                for (mUserOrder in mUserOrders) {
//                    if (mUserOrder.getCouponDiscountAmount() != null) {
//                        countPrice = countPrice.add(
//                            BigDecimal(mUserOrder.getCouponDiscountAmount()).setScale(
//                                2,
//                                BigDecimal.ROUND_HALF_UP
//                            )
//                        )
//                    } else {
//                        countPrice =
//                            countPrice.add(BigDecimal(mUserOrder.getItemTotal()).setScale(2, BigDecimal.ROUND_HALF_UP))
//                    }
//                }
//
//                val refundAmount: String? = countPrice.toString()
//
//                // 初始化SDK
//                var alipayClient: AlipayClient? = null
//
//                alipayClient = DefaultAlipayClient(alipayConfig)
//
//
//                // 构造请求参数以调用接口
//                val request = AlipayTradeRefundRequest()
//
//                val model = AlipayTradeRefundModel()
//
//
//                val currentUserOrder: UserOrder = mUserOrders.get(0)
//
//                // 设置商户订单号
//                model.setOutTradeNo(currentUserOrder.getOrderId())
//
//                // 设置支付宝交易号
//                model.setTradeNo(currentUserOrder.getAlipayId())
//
//                // 设置退款金额
//                model.setRefundAmount(refundAmount)
//
//                // 设置退款原因说明
//                model.setRefundReason(Objects.requireNonNullElse<T?>(currentUserOrder.getReBackReason(), refundReason))
//
//
//                model.setOutRequestNo(mOutRequestNo)
//
//                // 设置退款请求号
//                val refundGoodsDetail: MutableList<RefundGoodsDetail?> = ArrayList<RefundGoodsDetail?>()
//
//                for (mUserOrder in mUserOrders) {
//                    // 设置退款包含的商品列表信息
//
//                    val refundGoodsDetail0 = RefundGoodsDetail()
//                    refundGoodsDetail0.setOutSkuId(mUserOrder.getProductSku())
//                    refundGoodsDetail0.setOutItemId(mUserOrder.getId())
//                    //            refundGoodsDetail0.setGoodsId(mUserOrder.getProductId());
//                    refundGoodsDetail0.setRefundAmount(mUserOrder.getPrice())
//                    refundGoodsDetail.add(refundGoodsDetail0)
//                    model.setRefundGoodsDetail(refundGoodsDetail)
//                }
//
//
//                // 设置查询选项
//                val queryOptions: MutableList<String?> = ArrayList<String?>()
//
//                queryOptions.add("refund_detail_item_list")
//
//                queryOptions.add("deposit_back_info")
//
//                queryOptions.add("refund_voucher_detail_list")
//
//                model.setQueryOptions(queryOptions)
//
//                request.setBizModel(model)
//
//                println("================开始退款===================")
//
//                val response = alipayClient.execute<AlipayTradeRefundResponse?>(request)
//
//
//                if (response.isSuccess()) {
//                    println("================调用成功=================== \n" + Util.toJson(response))
//
//                    return true
//                } else {
//                    val diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response)
//
//                    println("================调用失败=================== " + diagnosisUrl + "\n" + Util.toJson(response))
//
//                    return false
//                }
//            } catch (e: AlipayApiException) {
//                println("================支付宝退款失败===================：" + e.message)
//
//                return false
//            }
//        }
//
//
//        fun queryAlipayOrder(orderId: String?, aliPayId: String?): AlipayTradeQueryResponse? {
//            try {
//                // 初始化SDK
//
//                val alipayClient: AlipayClient = DefaultAlipayClient(alipayConfig)
//
//                // 构造请求参数以调用接口
//                val request = AlipayTradeQueryRequest()
//
//                val model = AlipayTradeQueryModel()
//
//                // 设置订单支付时传入的商户订单号
//                model.setOutTradeNo(orderId)
//
//                // 设置支付宝交易号
//                model.setTradeNo(aliPayId)
//
//                // 设置查询选项
//                val queryOptions: MutableList<String?> = ArrayList<String?>()
//                queryOptions.add("trade_settle_info")
//                model.setQueryOptions(queryOptions)
//
//                request.setBizModel(model)
//
//                // 第三方代调用模式下请设置app_auth_token
//                // request.putOtherTextParam("app_auth_token", "<-- 请填写应用授权令牌 -->");
//                val response = alipayClient.execute<AlipayTradeQueryResponse?>(request)
//
//                println(response.getBody())
//
//
//
//                if (response.isSuccess()) {
//                    println("================支付宝订单查询调用成功=================== \n" + Util.toJson(response))
//
//
//                    return response
//                } else {
//                    val diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response)
//
//                    println(
//                        "================支付宝订单查询调用失败=================== " + diagnosisUrl + "\n" + Util.toJson(
//                            response
//                        )
//                    )
//
//                    return null
//                }
//            } catch (e: AlipayApiException) {
//                println("================支付宝订单查询失败===================：" + e.message)
//
//                return null
//            }
//        }
//
//
//        fun queryAlipayReBackOrder(
//            orderId: String?,
//            aliPayId: String?,
//            outRequestNo: String?
//        ): AlipayTradeFastpayRefundQueryResponse? {
//            try {
//                // 初始化SDK
//
//                val alipayClient: AlipayClient = DefaultAlipayClient(alipayConfig)
//
//                // 构造请求参数以调用接口
//                val request = AlipayTradeFastpayRefundQueryRequest()
//                val model = AlipayTradeFastpayRefundQueryModel()
//
//                // 设置支付宝交易号
//                model.setTradeNo(aliPayId)
//
//                // 设置商户订单号
//                model.setOutTradeNo(orderId)
//
//                // 设置退款请求号
//                model.setOutRequestNo(outRequestNo)
//
//                // 设置查询选项
//                val queryOptions: MutableList<String?> = ArrayList<String?>()
//                queryOptions.add("refund_detail_item_list")
//                model.setQueryOptions(queryOptions)
//
//                request.setBizModel(model)
//
//                // 第三方代调用模式下请设置app_auth_token
//                // request.putOtherTextParam("app_auth_token", "<-- 请填写应用授权令牌 -->");
//                val response = alipayClient.execute<AlipayTradeFastpayRefundQueryResponse?>(request)
//
//                println(response.getBody())
//
//
//                if (response.isSuccess()) {
//                    println("================支付宝退款订单查询调用成功=================== \n" + Util.toJson(response))
//
//
//                    return response
//                } else {
//                    val diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response)
//
//                    println(
//                        "================支付宝退款订单查询调用失败=================== " + diagnosisUrl + "\n" + Util.toJson(
//                            response
//                        )
//                    )
//
//                    return null
//                }
//            } catch (e: AlipayApiException) {
//                println("================支付宝退款订单查询失败===================：" + e.message)
//
//                return null
//            }
//        }
//
//
//        @Throws(Exception::class)
//        fun createClient(): Client {
//            val config: Config = Config()
//                .setAccessKeyId(System.getenv("MESSAGE_ACCESS_KEY_ID")) //                // 配置 AccessKey Secret，请确保代码运行环境设置了环境变量。
//                .setAccessKeySecret(System.getenv("MESSAGE_ACCESS_KEY_SECRET"))
//
//            // System.getenv()方法表示获取系统环境变量，请配置环境变量后，在此填入环境变量名称，不要直接填入AccessKey信息。
//
//            // 配置 Endpoint
//            config.endpoint = "dysmsapi.aliyuncs.com"
//
//            return Client(config)
//        }
//
//        fun sendSms(phoneNumber: String?, templateParam: String?): String? {
//            try {
//                // 初始化请求客户端
//                val client: Client = createClient()
//
//                // 构造请求对象，请填入请求参数值
//                val sendSmsRequest: SendSmsRequest? = SendSmsRequest()
//                    .setPhoneNumbers(phoneNumber)
//                    .setSignName("杭州梁子等等我服饰")
//                    .setTemplateCode("SMS_318650686")
//                    .setTemplateParam("{\"code\":\"" + templateParam + "\"}")
//
//
//                // 获取响应对象
//                val sendSmsResponse: SendSmsResponse? = client.sendSms(sendSmsRequest)
//
//                // 响应包含服务端响应的 body 和 headers
//                println("发送短信成功：" + toJSONString(sendSmsResponse))
//
//                return toJSONString(sendSmsResponse)
//            } catch (ignored: Exception) {
//                println("发送短信失败：" + ignored.message)
//            }
//
//            return null
//        }
//    }
//}