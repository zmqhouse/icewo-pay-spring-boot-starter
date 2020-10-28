package com.icewo.enums;

/**
 * 微信app支付第一次签名枚举类
 *
 * @author 花臂
 */
public enum WxAppPayFirstSignEnum {

    APP_ID("appid", "应用ID"),
    MCH_ID("mch_id", "商户号"),
    NONCE_STR("nonce_str", "随机字符串"),
    SIGN("sign", "签名"),
    BODY("body", "支付描述"),
    ATTACH("attach", "附加数据"),
    OUT_TRADE_NO("out_trade_no", "商户订单号"),
    FEE_TYPE("fee_type", "货币类型"),
    TOTAL_FEE("total_fee", "总金额(单位分)"),
    SPBILL_CREATE_IP("spbill_create_ip", "终端IP"),
    NOTIFY_URL("notify_url", "微信异步回调地址"),
    TRADE_TYPE("APP", "交易类型(APP...)");


    private String value;
    private String desc;

    WxAppPayFirstSignEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
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
}
