package com.icewo.utils;

import javax.net.ssl.TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

public class WxMyX509TrustManager implements TrustManager {
	// 检查客户端证书
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    // 检查服务器端证书
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    // 返回受信任的X509证书数组
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
