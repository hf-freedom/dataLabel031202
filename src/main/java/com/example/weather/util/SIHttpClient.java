package com.example.weather.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HTTP请求工具类
 */
@Component
public class SIHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(SIHttpClient.class);

    /**
     * 发送GET请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应结果
     */
    public String doGet(String url, Map<String, String> params) {
        StringBuilder urlBuilder = new StringBuilder(url);
        if (params != null && !params.isEmpty()) {
            urlBuilder.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                try {
                    urlBuilder.append(entry.getKey())
                            .append("=")
                            .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.name()))
                            .append("&");
                } catch (Exception e) {
                    logger.error("URL编码失败", e);
                }
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(urlBuilder.toString());
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            logger.error("HTTP请求失败: {}", url, e);
        }
        return null;
    }
}
