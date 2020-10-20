package com.icewo.enums;

/**
 * @ClassName WxPaymentChange
 * @Deseription 微信企业入款到零钱枚举类
 * @Author zmq
 * @Date 2020/10/20 11:48
 * @Version 1.0
 */
public enum WxPaymentChange {
    MCH_APPID("mch_appid", "商户账号appid", null),
    MCHID("mchid", "商户号", null),
    NONCE_STR("nonce_str", "随机字符串", null),
    SIGN("sign", "签名", null),
    PARTNER_TRADE_NO("partner_trade_no", "商户订单号", null),

    OPPENID("openid", "用户openid", null),

    CHECK_NAME("check_name", "校验用户姓名选项(默认值不校验)", "NO_CHECK"),

    AMOUNT("amount", "金额(单位分)", null),
    DESC("desc", "企业付款备注", null);

    private String value;
    private String desc;
    private String defaults;

    WxPaymentChange(String value, String desc, String defaults) {
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
