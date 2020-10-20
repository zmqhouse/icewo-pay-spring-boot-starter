package com.icewo.core;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.icewo.properties.WxPayProperties;
import com.icewo.response.WxUserInfoVo;
import com.icewo.utils.CertUtil;
import com.icewo.utils.JmUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @ClassName WxUserInfoCore
 * @Deseription 获取用户信息
 * @Author zmq
 * @Date 2020/10/20 15:42
 * @Version 1.0
 */
public class WxSmallUserInfoCore {

    @Autowired
    private WxPayProperties wxPayProperties;


    /**
     * 解密用户信息
     *
     * @param code          code凭证
     * @param encryptedData 加密数据
     * @param iv            加密算法的初始向量
     * @return
     */
    public WxUserInfoVo getWxUserInfo(String code, String encryptedData, String iv) throws UnsupportedEncodingException {
        //拼接请求URL
        String param = "appid=" + wxPayProperties.getWxAppid() + "&secret=" + wxPayProperties.getWxAppSecret() + "&js_code=" + code + "&grant_type=" + wxPayProperties.getWxGrantType();
        String res = CertUtil.sendGet(wxPayProperties.getWxLoginUrl(), param);
        JSONObject jsonObject = JSONUtil.parseObj(res);
        String sessionKey = jsonObject.get("session_key").toString();
        String openid = jsonObject.get("openid").toString();
        //对encryptedData加密数据进行AES解密
        String encryptedData1 = URLEncoder.encode(encryptedData, "UTF-8").replace("%3D", "=").replace("%2F", "/");
        String replace = URLEncoder.encode(iv, "UTF-8").replace("%3D", "=").replace("%2F", "/");
        JSONObject userInfo = JmUtils.getUserInfo(encryptedData1, sessionKey, iv);
        String decrypt = JSONUtil.toJsonStr(userInfo);
        if (decrypt != null) {
            JSONObject jsonObject1 = JSONUtil.parseObj(decrypt);
            WxUserInfoVo wxUserInfoVo = new WxUserInfoVo();
            wxUserInfoVo.setAvatarUrl((String) jsonObject1.get("avatarUrl"));
            wxUserInfoVo.setNickName((String) jsonObject1.get("nickName"));
            wxUserInfoVo.setCountry((String) jsonObject1.get("country"));
            wxUserInfoVo.setCity((String) jsonObject1.get("city"));
            wxUserInfoVo.setGender(Integer.parseInt((String) jsonObject1.get("gender")));
            wxUserInfoVo.setUnionId((String) jsonObject1.get("unionId"));
            wxUserInfoVo.setOpenId(openid);
            return wxUserInfoVo;
        }
        return null;
    }

    /**
     * 获取用户手机号
     *
     * @param code          code凭证
     * @param encryptedData 加密数据
     * @param iv            加密算法的初始向量
     * @return
     */
    public WxUserInfoVo getWxPhone(String code, String encryptedData, String iv) throws UnsupportedEncodingException {
        //拼接请求URL
        String param = "appid=" + wxPayProperties.getWxAppid() + "&secret=" + wxPayProperties.getWxAppSecret() + "&js_code=" + code + "&grant_type=" + wxPayProperties.getWxGrantType();
        String res = CertUtil.sendGet(wxPayProperties.getWxLoginUrl(), param);
        JSONObject jsonObject = JSONUtil.parseObj(res);
        String sessionKey = jsonObject.get("session_key").toString();
        String openid = jsonObject.get("openid").toString();
        //对encryptedData加密数据进行AES解密
        String encryptedData1 = URLEncoder.encode(encryptedData, "UTF-8").replace("%3D", "=").replace("%2F", "/");
        String replace = URLEncoder.encode(iv, "UTF-8").replace("%3D", "=").replace("%2F", "/");
        JSONObject userInfo = JmUtils.getUserInfo(encryptedData1, sessionKey, replace);
        String phoneNumber = userInfo.get("phoneNumber").toString();
        WxUserInfoVo wxUserInfoVo = new WxUserInfoVo();
        wxUserInfoVo.setPhone(phoneNumber);
        return wxUserInfoVo;
    }
}




