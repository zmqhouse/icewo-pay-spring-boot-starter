package com.icewo.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CertUtil
 * @Deseription TODO
 * @Author zmq
 * @Date 2020/6/19 18:48
 * @Version 1.0
 */

public class CertUtil {
    private static final String UTF = "UTF-8";
    private static final String PKCS12 = "PKCS12";

    /**
     * 带证书httpPost请求
     *
     * @param url   接口地址
     * @param param 参数
     * @return
     * @throws Exception
     */
    public static String sendRedEnvelope(String url, String param, String wechatMchId) throws Exception {

        //证书地址
        // String fileRoute = "";
        ClassPathResource cl = new ClassPathResource("apiclient_cert.p12");
        //指定读取证书格式为PKCS12
        KeyStore keyStore = KeyStore.getInstance(PKCS12);
//        //读取本机存放的PKCS12证书文件
//        FileInputStream instream = new FileInputStream(new File(fileRoute));
        InputStream inputStream = cl.getInputStream();
        try {
            //指定PKCS12的密码
            keyStore.load(inputStream, wechatMchId.toCharArray());
        } finally {
            inputStream.close();
        }
        //指定TLS版本
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, wechatMchId.toCharArray())
                .build();
        //设置httpclient的SSLSocketFactory
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        StringBuffer stringBuffer = new StringBuffer();
        try {
            HttpPost httpPost = new HttpPost(url);
            InputStream is = new ByteArrayInputStream(param.getBytes(UTF));
            //InputStreamEntity严格是对内容和长度相匹配的。用法和BasicHttpEntity类似
            InputStreamEntity inputStreamEntity = new InputStreamEntity(is, is.available());
            httpPost.setEntity(inputStreamEntity);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        entity.getContent(), UTF));
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    stringBuffer.append(inputLine);
                }
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return stringBuffer.toString();
    }


    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
