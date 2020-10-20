package com.icewo.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;

/**
 * @ClassName JmUtils
 * @Deseription
 * @Author zmq
 * @Date 2020/9/1 12:02
 * @Version 1.0
 */
public class JmUtils {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String dd="HjREuIqH6J p/0sZZc1WyfpnNT7JS8pwIf/r if5dJ2keP7ojVv WqF7tb8NLogtcbYPVmng6bm0rvHlcpIBs8GnPyBl93s4E4kKH/D9yP6G5mAXOLJehqM1/8U6LH8mxczn5P630av5P27r7pesBH5PjjSYXE4WGVUqHZUS1b HGXrzvl8JGcnPgO6gpx EWkdF0J5tVb2Kz37E40NUnYcSHMAHiKr8x0Ym8/ wU1zcXimAZbuF6Fd2bpb3a9A0usb7QTXkyV4255rPgBRTAeA jK0WJwlSnmzSQrGPuNdbKO3M0hoEudhZltwlGzj3HgjXfQm3g9a6PNjzlW wCVAzLdCqa36SKv0kZX6azzRsSdChDAbgP614uu169jNbKwNV0hc0FWoLY/5ocR07hYL9oCIW4m kdJcawalq1traOPS OKnO/LmpPWUhgGyjMZx8/Rt/FQVj1ostN4CxCe T1yQURW3EqJIdTbPlqbtaln5fXp4krJmu0faQ//R/";

        String iv="WQ GaZjj4JS7h41DXxap4g==";
        String replace = URLEncoder.encode(iv, "UTF-8").replace("%3D", "=").replace("%2F", "/");
        String replace1 = URLEncoder.encode(dd, "UTF-8").replace("%3D", "=").replace("%2F", "/");
        JSONObject userInfo = getUserInfo(replace1, "RYiymigy2KIx8gANhgYaHw==", replace);
        JSONObject jsonObject = JSONUtil.parseObj(userInfo);
        System.out.println(jsonObject.get("nickName").toString());
        System.out.println(jsonObject.get("avatarUrl").toString());
        System.out.println(jsonObject.get("country").toString());
        System.out.println(jsonObject.get("province").toString());
        System.out.println(jsonObject.get("city").toString());
        System.out.println(Integer.valueOf(jsonObject.get("gender").toString()));
        System.out.println(jsonObject.get("unionId").toString());
        System.out.println();
    }


    /**
     * 解密encryptedData获取用户信息
     *
     * @param encryptedData
     * @param sessionKey
     * @param iv
     * @return
     */
    public static JSONObject getUserInfo(String encryptedData, String sessionKey, String iv) {
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);

        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSONUtil.parseObj(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
