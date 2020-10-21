package com.icewo.core;

import com.icewo.enums.WxSmallFirstSignEnum;
import com.icewo.enums.WxSmallSecondSignEnum;
import com.icewo.properties.WxPayProperties;
import com.icewo.response.WxPayResponse;
import com.icewo.utils.WxAmountUtils;
import com.icewo.utils.WxPayUtils;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @ClassName WxSmallPayCore
 * @Deseription wx 小程序支付
 * @Author zmq
 * @Date 2020/10/19 17:56
 * @Version 1.0
 */
public class WxSmallPayCore {

    private static final String SUCCESS = "SUCCESS";
    private static final String UTF = "UTF-8";
    private static final String RETURNCODE = "return_code";
    private static final String RESULTCODE = "result_code";
    private static final String PREPAYID = "prepay_id";
    private static final String ERRCODE = "err_code";
    private static final String ERRCODEDES = "err_code_des";
    private static final String POST = "POST";

    @Autowired
    private WxPayProperties wxPayProperties;


    public WxPayResponse generatePrepayment(String openId, BigDecimal realAsset, String orderNumber, String ipAddress, String attach) {
        //将元转为分,微信金额单位是分
        String money = WxAmountUtils.changeY2F(realAsset.toString());
        SortedMap<Object, Object> responMap = new TreeMap<>();
        responMap.put(WxSmallFirstSignEnum.APP_ID.getValue(), wxPayProperties.getWxAppid());
        responMap.put(WxSmallFirstSignEnum.MCH_ID.getValue(), wxPayProperties.getWxMchid());
        responMap.put(WxSmallFirstSignEnum.NONCE_STR.getValue(), WxPayUtils.CreateNoncestr());
        responMap.put(WxSmallFirstSignEnum.BODY.getValue(), wxPayProperties.getWxBody());
        responMap.put(WxSmallFirstSignEnum.OUT_TRADE_NO.getValue(), orderNumber);
        responMap.put(WxSmallFirstSignEnum.FEE_TYPE.getValue(), wxPayProperties.getWxFeeType());
        responMap.put(WxSmallFirstSignEnum.TOTAL_FEE.getValue(), money);
        responMap.put(WxSmallFirstSignEnum.SPBILL_CREATE_IP.getValue(), ipAddress);
        responMap.put(WxSmallFirstSignEnum.NOTIFY_URL.getValue(), wxPayProperties.getWxNotifyUrl());
        responMap.put(WxSmallFirstSignEnum.TRADE_TYPE.getValue(), wxPayProperties.getWxTradeType());
        responMap.put(WxSmallFirstSignEnum.OPPEN_ID.getValue(), openId);
        if (attach != null) {
            responMap.put(WxSmallFirstSignEnum.ATTACH.getValue(), attach);
        }
        //签名
        String sign = WxPayUtils.createSign(UTF, responMap, wxPayProperties.getWxMchkey());
        responMap.put(WxSmallFirstSignEnum.SIGN.getValue(), sign);

        //map转xml
        String requestXml = WxPayUtils.getRequestXml(responMap);
        //调用微信统一下单接口返回xml
        String result = null;
        try {
            result = WxPayUtils.httpsRequest(wxPayProperties.getWxOrderUrl(), POST, requestXml);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("map转xml失败");
        }
        //解析xml
        Map resMap = null;
        try {
            resMap = WxPayUtils.doXMLParse(result);
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
            throw new RuntimeException("xml转map失败");
        }
        return checkReturn(resMap);
    }


    /**
     * 判断生成是否成功生成预支付信息
     *
     * @param result
     * @return status:boolean  errCode:"err_code字段"   errCodeDes:"errCodeDes字段"
     */
    private WxPayResponse checkReturn(Map<String, Object> result) {
        String returnCode = (String) result.get(RETURNCODE);
        String resultCode = (String) result.get(RESULTCODE);

        //return_code和result_code都返回 SUCCESS 表示生成预支付信息成功
        if (SUCCESS.equals(resultCode) && SUCCESS.equals(returnCode)) {
            String prepayId = (String) result.get(PREPAYID);
            WxPayResponse wxPayResponse = new WxPayResponse();
            wxPayResponse.setStatus(Boolean.TRUE);
            wxPayResponse.setPrepayId(prepayId);
            return wxPayResponse;
        } else {
            String errCode = (String) result.get(ERRCODE);
            String errCodeDes = (String) result.get(ERRCODEDES);
            WxPayResponse wxPayResponse = new WxPayResponse();
            wxPayResponse.setStatus(Boolean.FALSE);
            wxPayResponse.setErrCode(errCode);
            wxPayResponse.setErrCodeDes(errCodeDes);
            return wxPayResponse;
        }
    }


    /**
     * 微信app支付二次签名
     *
     * @param prepayId 预支付交易ID
     * @return
     */
    public String secondSignature(Long timestamp,String prepayId) {
        SortedMap<Object, Object> responMap = new TreeMap<>();
        String nonceStr = WxPayUtils.CreateNoncestr(); //生成随机字符串
        responMap.put(WxSmallSecondSignEnum.APP_ID.getValue(), wxPayProperties.getWxAppid());
        responMap.put(WxSmallSecondSignEnum.NONCE_STR.getValue(), nonceStr);
        responMap.put(WxSmallSecondSignEnum.PACKAGE.getValue(), WxSmallSecondSignEnum.PACKAGE.getDefaults() + prepayId);
        responMap.put(WxSmallSecondSignEnum.SIGN_TYPE.getValue(), WxSmallSecondSignEnum.SIGN_TYPE.getDefaults());
        responMap.put(WxSmallSecondSignEnum.TIMESTAMP.getValue(), timestamp);
        //二次签名
        String secondSign = WxPayUtils.createSign(UTF, responMap, wxPayProperties.getWxMchkey());
        return secondSign;

    }


}

