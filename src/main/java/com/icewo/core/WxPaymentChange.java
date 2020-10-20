package com.icewo.core;

import com.icewo.properties.WxPayProperties;
import com.icewo.response.WxPayResponse;
import com.icewo.utils.CertUtil;
import com.icewo.utils.WxAmountUtils;
import com.icewo.utils.WxPayUtils;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @ClassName Wx
 * @Deseription 企业付款到零钱
 * @Author zmq
 * @Date 2020/10/20 11:35
 * @Version 1.0
 */
public class WxPaymentChange {

    @Autowired
    private WxPayProperties wxPayProperties;

    private static final String UTF = "UTF-8";
    private static final String SUCCESS = "SUCCESS";
    private static final String RETURNCODE = "return_code";
    private static final String RESULTCODE = "result_code";
    private static final String ERRCODE = "err_code";
    private static final String ERRCODEDES = "err_code_des";



    /**
     * 微信企业付款到零钱
     *
     * @param openId      用户标识openid
     * @param orderNumber 订单号
     * @param realAsset   实际付款金额(单位元)
     */
    public WxPayResponse payMoneyChange(String openId, String orderNumber, BigDecimal realAsset) throws Exception {
        //将金额转换为分
        String amount = WxAmountUtils.changeY2F(realAsset.toString());
        //获取微信oppenid
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        packageParams.put(com.icewo.enums.WxPaymentChange.MCH_APPID.getValue(), wxPayProperties.getWxAppid());
        packageParams.put(com.icewo.enums.WxPaymentChange.MCHID.getValue(), wxPayProperties.getWxMchid());
        packageParams.put(com.icewo.enums.WxPaymentChange.NONCE_STR.getValue(), WxPayUtils.CreateNoncestr());
        packageParams.put(com.icewo.enums.WxPaymentChange.PARTNER_TRADE_NO.getValue(), String.valueOf(orderNumber));
        packageParams.put(com.icewo.enums.WxPaymentChange.OPPENID.getValue(), openId);
        packageParams.put(com.icewo.enums.WxPaymentChange.CHECK_NAME.getValue(), com.icewo.enums.WxPaymentChange.CHECK_NAME.getDefaults());
        packageParams.put(com.icewo.enums.WxPaymentChange.AMOUNT.getValue(), amount);
        packageParams.put(com.icewo.enums.WxPaymentChange.DESC.getValue(), wxPayProperties.getWxDesc());
        //生成签名
        String sign = WxPayUtils.createSign(UTF, packageParams, wxPayProperties.getWxMchkey());
        packageParams.put(com.icewo.enums.WxPaymentChange.SIGN.getValue(), sign);

        //将map集合转换为xml
        String requestXml = WxPayUtils.getRequestXml(packageParams);
        //向微信发送转账请求
        String result = CertUtil.sendRedEnvelope(wxPayProperties.getWxPaymentUrl(), requestXml, wxPayProperties.getWxMchid());
        //将微信返回的xml解析成map集合
        Map<Object, Object> map = new HashMap<>();
        try {
            map = WxPayUtils.doXMLParse(result);
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
            throw new RuntimeException("xml转map失败");
        }
        return checkPayMoney(map);


    }

    /**
     * 判断付款是否成功
     *
     * @param map
     * @return
     */
    public WxPayResponse checkPayMoney(Map<Object, Object> map) {
        String returnCode = (String) map.get(RETURNCODE);
        String resultCode = (String) map.get(RESULTCODE);
        //return_code和result_code都返回 SUCCESS 表示生成预支付信息成功
        if (SUCCESS.equals(resultCode) && SUCCESS.equals(returnCode)) {
            WxPayResponse wxPayResponse = new WxPayResponse();
            wxPayResponse.setStatus(Boolean.TRUE);
            return wxPayResponse;
        } else {
            String errCode = (String) map.get(ERRCODE);
            String errCodeDes = (String) map.get(ERRCODEDES);
            WxPayResponse wxPayResponse = new WxPayResponse();
            wxPayResponse.setStatus(Boolean.FALSE);
            wxPayResponse.setErrCode(errCode);
            wxPayResponse.setErrCodeDes(errCodeDes);
            return wxPayResponse;
        }
    }
}
