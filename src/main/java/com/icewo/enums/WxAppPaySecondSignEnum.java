package com.icewo.enums;

/**
 * @ClassName WxPaySecondSignEnum
 * @Deseription 微信app支付第二次签名枚举类
 * @Author zmq
 * @Date 2020/10/19 16:54
 * @Version 1.0
 */
public enum WxAppPaySecondSignEnum {
    APP_ID("appid", "应用ID", null),
    PARTNER_ID("partnerid", "商户号", null),
    PREPAY_ID("prepayid", "预支付交易回话ID", null),
    PACKAGE("package", "拓展字段", "Sign=WXPay"),
    NONCESTR("noncestr", "随机字符串", null),
    TIMESTAMP("timestamp", "时间戳", String.valueOf(System.currentTimeMillis()).substring(0, 10));


    private String value;
    private String desc;
    private String defaults;

    WxAppPaySecondSignEnum(String value, String desc, String defaults) {
        this.value = value;
        this.desc = desc;
        this.defaults = defaults;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDefaults() {
        return defaults;
    }

    public void setDefaults(String defaults) {
        this.defaults = defaults;
    }
}

