package com.zihai.h2Client.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class HttpUtil implements InitializingBean {
    private final static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    OkHttpClient httpclient;
    @Value("${weixin.cert:zihai}")
    private String certPath;
    @Value("${weixin.mchId:1234568}")
    private String mchId;

    public static synchronized String uuidString() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public Document PostXmlWeiXin(String url, String xml) {
        logger.info("postxml=={}", xml);

        RequestBody body = RequestBody.create(MediaType.parse("text/xml"), xml);
        Request request = new Request.Builder()
                .url(url).method("POST", body).build();
        try (Response resonse = httpclient.newCall(request).execute()){
            if (resonse.code() == 200) {
                String receive = resonse.body().string();
                logger.info("receov=={}", receive);
                SAXReader reader = new SAXReader();
                Document document = reader.read(IOUtils.toInputStream(receive));
                Element e_root = document.getRootElement();
                if ("SUCCESS".equals(e_root.elementTextTrim("return_code"))) {
                    return document;
                } else {
                    throw new RuntimeException(e_root.elementTextTrim("return_msg"));
                }
            } else {
                throw new RuntimeException("通讯异常 code=" + resonse.code());
            }
        } catch (IOException e) {
            throw new RuntimeException("通讯异常 msg=" + e.getMessage());
        } catch (DocumentException e) {
            throw new RuntimeException("数据解析异常");
        }
    }

    public Document PostXmlWeiXinWithCert(String url, String xml){
        logger.info("postxml=={}", xml);
        RequestBody body = RequestBody.create(MediaType.parse("application/xml"), xml);
        Request request = new Request.Builder()
                .url(url).method("POST", body).build();
        try (Response resonse = httpclient.newCall(request).execute();){
            if (resonse.code() == 200) {
                String receive = resonse.body().string();
                logger.info("receov=={}", receive);
                SAXReader reader = new SAXReader();
                Document document = reader.read(IOUtils.toInputStream(receive));
                Element e_root = document.getRootElement();
                if ("SUCCESS".equals(e_root.elementTextTrim("return_code"))) {
                    return document;
                } else {
                    throw new RuntimeException(e_root.elementTextTrim("return_msg"));
                }
            } else {
                throw new RuntimeException("通讯异常 code=" + resonse.code());
            }
        } catch (IOException e) {
            throw new RuntimeException("通讯异常 msg=" + e.getMessage());
        } catch (DocumentException e) {
            throw new RuntimeException("数据解析异常");
        }
    }

    public JsonObject getJson(String url, JsonObject param) {
        StringBuffer buf = new StringBuffer(url);
        if (param!=null&&param.size() > 0) {
            int count = 0;
            for (Map.Entry<String, JsonElement> entry : param.entrySet()) {
                if (count == 0) {
                    buf.append("?").append(entry.getKey()).append("=").append(entry.getValue().getAsString());
                } else {
                    buf.append("&").append(entry.getKey()).append("=").append(entry.getValue().getAsString());
                }
                count++;
            }
        }
        logger.info("param=={}", buf.toString());
        Request request = new Request.Builder()
                .url(buf.toString()).build();
        try (Response response = httpclient.newCall(request).execute()){
            String result = response.body().string();
            if (response.code() == 200) {
                return JsonHelp.gson.fromJson(result, JsonObject.class);
            } else {
                throw new RuntimeException("通讯异常 code=" + response.code());
            }
        } catch (IOException e) {
            throw new RuntimeException("通讯异常 error=" + e.getMessage());
        }
    }

    public JsonObject postJson(String url, JsonObject data) {
        logger.info("data=={}", data);
        Request request = new Request.Builder()
                .url(url).post(RequestBody.create(MediaType.parse("application/json"),data.toString())).build();
        try(Response response = httpclient.newCall(request).execute()) {
            String result = response.body().string();
            logger.info("receov=={}", result);
            if (response.code() == 200) {
                return JsonHelp.gson.fromJson(result, JsonObject.class);
            } else {
                throw new RuntimeException("通讯异常 code=" + response.code());
            }
        } catch (IOException e) {
            throw new RuntimeException("通讯异常 error=" + e.getMessage());
        }
    }
    public JsonObject postJson(String url, JsonObject data,String mstoken) {
        logger.info("data=={}", data);
        Request request = new Request.Builder().addHeader("msToken",mstoken)
                .url(url).post(RequestBody.create(MediaType.parse("application/json"),data.toString())).build();
        try(Response response = httpclient.newCall(request).execute()) {
            String result = response.body().string();
            logger.info("receov=={}", result);
            if (response.code() == 200) {
                return JsonHelp.gson.fromJson(result, JsonObject.class);
            } else {
                throw new RuntimeException("通讯异常 code=" + response.code());
            }
        } catch (IOException e) {
            throw new RuntimeException("通讯异常 error=" + e.getMessage());
        }
    }

    public String postForm(String url, JsonObject param) {
        logger.info("send url=={},data=={}",url, param);
        StringBuffer buf = new StringBuffer(url);
        FormBody.Builder builder = new FormBody.Builder();
        if (param!=null&&param.size() > 0) {
            for (Map.Entry<String, JsonElement> entry : param.entrySet()) {
                builder.add(entry.getKey(),entry.getValue().getAsString());
            }
        }
        Request request = new Request.Builder()
                .url(buf.toString()).method("POST",builder.build()).build();
        try (Response response = httpclient.newCall(request).execute()){
            String result = response.body().string();
            logger.info("receov=={}", result);
            if (response.code() == 200) {
                return result;
            } else {
                throw new RuntimeException("通讯异常 code=" + response.code());
            }
        } catch (IOException e) {
            throw new RuntimeException("通讯异常 error=" + e.getMessage());
        }
    }

    @Override
    public void afterPropertiesSet() {
        Security.addProvider(new BouncyCastleProvider());
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS);
        try (InputStream client_input = Thread.currentThread().getContextClassLoader().getResourceAsStream(certPath)){
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(client_input, mchId.toCharArray());
            //KeyManager选择证书证明自己的身份
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, mchId.toCharArray());
            KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, null, new SecureRandom());
            builder.sslSocketFactory(sslContext.getSocketFactory());
            httpclient = builder.build();
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | KeyManagementException e) {
            logger.error(e.getMessage(), e);
            System.exit(0);
        }

    }
}