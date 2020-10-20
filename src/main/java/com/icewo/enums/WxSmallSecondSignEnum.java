package com.icewo.enums;

/**
 * @ClassName WxSmallFirstSignEnum
 * @Deseription wx 小程序第二次签名枚举类
 * @Author zmq
 * @Date 2020/10/19 18:12
 * @Version 1.0
 */
public enum WxSmallSecondSignEnum {

    APP_ID("appId", "应用ID", null),
    TIMESTAMP("timeStamp", "时间戳", String.valueOf(System.currentTimeMillis()).substring(0, 10)),
    PACKAGE("package", "数据包", "prepay_id="),
    NONCE_STR("nonceStr", "随机字符串", null),
    SIGN_TYPE("signType", "加密方式", "MD5");

    private String value;
    private String desc;
    private String defaults;


    WxSmallSecondSignEnum(String value, String desc, String defaults) {
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
