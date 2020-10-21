package com.icewo.core;

import com.icewo.enums.WxAppPayFirstSignEnum;
import com.icewo.enums.WxAppPaySecondSignEnum;
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
 * @ClassName WxPayCore
 * @Deseription wx app支付
 * @Author zmq
 * @Date 2020/10/19 15:19
 * @Version 1.0
 */
public class WxAppPayCore {

    @Autowired
    private WxPayProperties wxPayProperties;

    private static final String SUCCESS = "SUCCESS";
    private static final String UTF = "UTF-8";
    private static final String RETURNCODE = "return_code";
    private static final String RESULTCODE = "result_code";
    private static final String PREPAYID = "prepay_id";
    private static final String ERRCODE = "err_code";
    private static final String ERRCODEDES = "err_code_des";
    private static final String POST = "POST";


    /**
     * 生成预支付订单信息
     *
     * @param realAsset   订单金额
     * @param orderNumber 商户订单号
     * @param ipAddress   IP地址
     * @param attach      拓展信息(异步回调attach字段)
     * @return
     */
    public WxPayResponse generatePrepayment(BigDecimal realAsset, String orderNumber, String ipAddress, String attach) {
        //将元转为分,微信要求是分
        String money = WxAmountUtils.changeY2F(realAsset.toString());
        SortedMap<Object, Object> requestMap = new TreeMap<>();
        requestMap.put(WxAppPayFirstSignEnum.APP_ID.getValue(), wxPayProperties.getWxAppid());
        requestMap.put(WxAppPayFirstSignEnum.MCH_ID.getValue(), wxPayProperties.getWxMchid());
        requestMap.put(WxAppPayFirstSignEnum.NONCE_STR.getValue(), WxPayUtils.CreateNoncestr());
        requestMap.put(WxAppPayFirstSignEnum.BODY.getValue(), wxPayProperties.getWxBody());
        requestMap.put(WxAppPayFirstSignEnum.OUT_TRADE_NO.getValue(), orderNumber);
        requestMap.put(WxAppPayFirstSignEnum.FEE_TYPE.getValue(), wxPayProperties.getWxFeeType());
        requestMap.put(WxAppPayFirstSignEnum.TOTAL_FEE.getValue(), money);
        requestMap.put(WxAppPayFirstSignEnum.SPBILL_CREATE_IP.getValue(), ipAddress);
        requestMap.put(WxAppPayFirstSignEnum.NOTIFY_URL.getValue(), wxPayProperties.getWxNotifyUrl());
        requestMap.put(WxAppPayFirstSignEnum.TRADE_TYPE.getValue(), wxPayProperties.getWxTradeType());
        if (attach != null) {
            requestMap.put(WxAppPayFirstSignEnum.ATTACH.getValue(), attach);
        }

        //签名
        String sign = WxPayUtils.createSign(UTF, requestMap, wxPayProperties.getWxMchkey());
        requestMap.put(WxAppPayFirstSignEnum.SIGN.getValue(), sign);

        //map转xml
        String requestXml = null;
        try {
            requestXml = WxPayUtils.getRequestXml(requestMap);
        } catch (Exception e) {
            throw new RuntimeException("map转xml失败");
        }

        //发送请求
        String result = null;
        try {
            result = WxPayUtils.httpsRequest(wxPayProperties.getWxOrderUrl(), POST, requestXml);
        } catch (Exception e) {
            throw new RuntimeException("请求微信统一下单接口报错");
        }

        System.out.println(result);
        //xml转map
        Map responMap = null;
        try {
            responMap = WxPayUtils.doXMLParse(result);
        } catch (IOException | JDOMException e) {
            throw new RuntimeException("微信XML转map失败");
        }

        return checkReturn(responMap);


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
        responMap.put(WxAppPaySecondSignEnum.APP_ID.getValue(), wxPayProperties.getWxAppid());
        responMap.put(WxAppPaySecondSignEnum.NONCESTR.getValue(), nonceStr);
        responMap.put(WxAppPaySecondSignEnum.PACKAGE.getValue(), WxAppPaySecondSignEnum.PACKAGE.getDefaults());
        responMap.put(WxAppPaySecondSignEnum.PARTNER_ID.getValue(), wxPayProperties.getWxMchid());
        responMap.put(WxAppPaySecondSignEnum.PREPAY_ID.getValue(), prepayId);
        responMap.put(WxAppPaySecondSignEnum.TIMESTAMP.getValue(), timestamp);
        //二次签名
        String secondSign = WxPayUtils.createSign(UTF, responMap, wxPayProperties.getWxMchkey());
        return secondSign;

    }


}
