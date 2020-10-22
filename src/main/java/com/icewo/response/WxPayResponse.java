package com.icewo.response;

import java.io.Serializable;

/**
 * @ClassName WxPayResponse
 * @Deseription
 * @Author zmq
 * @Date 2020/10/19 18:41
 * @Version 1.0
 */
public class WxPayResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态
     */
    private Boolean status;
    /**
     * 预支付会话交易ID(status为true返回)
     */
    private String prepayId;


    /**
     * 错误码(status为false返回)
     */
    private String errCode;

    /**
     * 错误描述(status为false返回)
     */
    private String errCodeDes;


    /**
     * 附加数据
     */
    private String attach;

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }
}
