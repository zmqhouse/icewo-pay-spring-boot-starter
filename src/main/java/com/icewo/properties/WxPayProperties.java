package com.icewo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName PayProperties
 * @Deseription
 * @Author zmq
 * @Date 2020/10/19 14:45
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "icewo.wxpay")
public class WxPayProperties {


    /**
     * 微信appid
     */
    private String wxAppid;
    /**
     * 微信商户号
     */
    private String wxMchid;
    /**
     * 微信交易货币类型
     */
    private String wxFeeType;
    /**
     * 微信异步回调地址
     */
    private String wxNotifyUrl;

    /**
     * 微信交易类型
     */
    private String wxTradeType;
    /**
     * 微信商户秘钥
     */
    private String wxMchkey;
    /**
     * 企业付款到零钱URL
     */
    private String wxPaymentUrl;

    /**
     * 微信统一下单URL
     */
    private String wxOrderUrl;


    /**
     * 支付商品描述
     */
    private String wxBody;


    /**
     * 微信企业付款到零钱备注
     */
    private String wxDesc;


    /**
     * 小程序密钥
     */
    private String wxAppSecret;


    /**
     * 授权类型
     */
    private String wxGrantType;


    /**
     * 授权获取用户信息URL
     */
    private String wxLoginUrl;

    public String getWxLoginUrl() {
        return wxLoginUrl;
    }

    public void setWxLoginUrl(String wxLoginUrl) {
        this.wxLoginUrl = wxLoginUrl;
    }

    public String getWxGrantType() {
        return wxGrantType;
    }

    public void setWxGrantType(String wxGrantType) {
        this.wxGrantType = wxGrantType;
    }

    public String getWxAppid() {
        return wxAppid;
    }

    public void setWxAppid(String wxAppid) {
        this.wxAppid = wxAppid;
    }

    public String getWxMchid() {
        return wxMchid;
    }

    public void setWxMchid(String wxMchid) {
        this.wxMchid = wxMchid;
    }

    public String getWxFeeType() {
        return wxFeeType;
    }

    public void setWxFeeType(String wxFeeType) {
        this.wxFeeType = wxFeeType;
    }

    public String getWxNotifyUrl() {
        return wxNotifyUrl;
    }

    public void setWxNotifyUrl(String wxNotifyUrl) {
        this.wxNotifyUrl = wxNotifyUrl;
    }

    public String getWxTradeType() {
        return wxTradeType;
    }

    public void setWxTradeType(String wxTradeType) {
        this.wxTradeType = wxTradeType;
    }

    public String getWxMchkey() {
        return wxMchkey;
    }

    public void setWxMchkey(String wxMchkey) {
        this.wxMchkey = wxMchkey;
    }

    public String getWxPaymentUrl() {
        return wxPaymentUrl;
    }

    public void setWxPaymentUrl(String wxPaymentUrl) {
        this.wxPaymentUrl = wxPaymentUrl;
    }

    public String getWxOrderUrl() {
        return wxOrderUrl;
    }

    public void setWxOrderUrl(String wxOrderUrl) {
        this.wxOrderUrl = wxOrderUrl;
    }

    public String getWxBody() {
        return wxBody;
    }

    public void setWxBody(String wxBody) {
        this.wxBody = wxBody;
    }

    public String getWxDesc() {
        return wxDesc;
    }

    public void setWxDesc(String wxDesc) {
        this.wxDesc = wxDesc;
    }

    public String getWxAppSecret() {
        return wxAppSecret;
    }

    public void setWxAppSecret(String wxAppSecret) {
        this.wxAppSecret = wxAppSecret;
    }
}
