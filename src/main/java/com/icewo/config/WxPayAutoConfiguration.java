package com.icewo.config;

import com.icewo.core.WxAppPayCore;
import com.icewo.core.WxPaymentChange;
import com.icewo.core.WxSmallPayCore;
import com.icewo.core.WxSmallUserInfoCore;
import com.icewo.properties.WxPayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @ClassName PayAutoConfig
 * @Deseription
 * @Author zmq
 * @Date 2020/10/19 14:44
 * @Version 1.0
 */
@Configuration
@EnableConfigurationProperties(WxPayProperties.class)
public class WxPayAutoConfiguration implements CommandLineRunner {

    @Autowired

    private WxPayProperties wxPayProperties;


    @Bean
    @Lazy
    public WxAppPayCore wxPayCore() throws Exception {

        if (wxPayProperties.getWxBody() == null || "".equals(wxPayProperties.getWxBody())) {
            throw new Exception("icewo.wxpay.wx-body为空");
        }
        return new WxAppPayCore();
    }

    @Bean
    @Lazy
    public WxSmallPayCore wxSmallPayCore() throws Exception {
        if (wxPayProperties.getWxBody() == null || "".equals(wxPayProperties.getWxBody())) {
            throw new Exception("icewo.wxpay.wx-body为空");
        }
        return new WxSmallPayCore();
    }

    @Bean
    @Lazy
    public WxPaymentChange wxPaymentChange() throws Exception {
        if (wxPayProperties.getWxPaymentUrl() == null || "".equals(wxPayProperties.getWxPaymentUrl())) {
            throw new Exception("icewo.wxpay.wx-payment-url为空");
        }
        if (wxPayProperties.getWxDesc() == null || "".equals(wxPayProperties.getWxDesc())) {
            throw new Exception("icewo.wxpay.wx-desc为空");
        }
        return new WxPaymentChange();
    }



    @Bean
    @Lazy
    public WxSmallUserInfoCore wxSmallUserInfoCore() throws Exception {
        if (wxPayProperties.getWxAppSecret() == null || "".equals(wxPayProperties.getWxAppSecret())) {
            throw new Exception("icewo.wxpay.wx-app-secret为空");
        }
        return new WxSmallUserInfoCore();
    }

    @Override
    public void run(String... args) throws Exception {
        if (wxPayProperties.getWxAppid() == null || "".equals(wxPayProperties.getWxAppid())) {
            throw new Exception("icewo.wxpay.wx-appid为空");
        }
        if (wxPayProperties.getWxMchid() == null || "".equals(wxPayProperties.getWxMchid())) {
            throw new Exception("icewo.wxpay.wx-mchid为空");
        }
        if (wxPayProperties.getWxMchkey() == null || "".equals(wxPayProperties.getWxMchkey())) {
            throw new Exception("icewo.wxpay.wx-mchkey为空");
        }
        if (wxPayProperties.getWxOrderUrl() == null || "".equals(wxPayProperties.getWxOrderUrl())) {
            throw new Exception("icewo.wxpay.wx-order-url为空");
        }
        if (wxPayProperties.getWxTradeType() == null || "".equals(wxPayProperties.getWxTradeType())) {
            throw new Exception("icewo.wxpay.wx-trade-type为空");
        }
        if (wxPayProperties.getWxFeeType() == null || "".equals(wxPayProperties.getWxFeeType())) {
            throw new Exception("icewo.wxpay.wx-fee-type为空");
        }
        if (wxPayProperties.getWxNotifyUrl() == null || "".equals(wxPayProperties.getWxNotifyUrl())) {
            throw new Exception("icewo.wxpay.wx-notify-url为空");
        }

    }
}
