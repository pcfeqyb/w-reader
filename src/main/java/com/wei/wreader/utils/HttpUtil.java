package com.wei.wreader.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http请求工具类
 *
 * @author weizhanjie
 */
public class HttpUtil {

    public static final String GET = "GET";

    public static final String POST = "POST";

    /**
     * 发送GET请求
     * @param urlStr
     * @return
     * @throws Exception
     */
    public static String sendGet(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    /**
     * 发送POST请求
     * @param urlStr
     * @param urlParameters
     * @return
     * @throws Exception
     */
    public static String sendPost(String urlStr, String urlParameters) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.writeBytes(urlParameters);
            wr.flush();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    /**
     * 通用请求
     * @param config 请求配置 or 请求url
     * @return
     */
    public static @NotNull HttpRequestBase commonRequest(String config)  {
        HttpRequestConfigParser parser = new HttpRequestConfigParser(config);
        String requestUrl = parser.getUrl();
        String requestMethod = parser.getMethod();
        Map<String, String> queryParam = parser.getQueryParams();
        Map<String, String> bodyParam = parser.getBodyParams();

        if (POST.equalsIgnoreCase(requestMethod)) {
            // 处理POST请求
            // 处理query请求参数
            if (queryParam != null && !queryParam.isEmpty()) {
                StringBuilder queryString = new StringBuilder();
                for (Map.Entry<String, String> entry : queryParam.entrySet()) {
                    queryString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
                requestUrl += "?" + queryString.substring(0, queryString.length() - 1);
            }
            HttpPost httpPost = new HttpPost(requestUrl);
            // 处理body请求参数
            if (bodyParam != null && !bodyParam.isEmpty()) {
                List<NameValuePair> formParams = new ArrayList<>();
                for (Map.Entry<String, String> entry : bodyParam.entrySet()) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }

                try {
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");
                    httpPost.setEntity(entity);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            return httpPost;
        } else if (GET.equalsIgnoreCase(requestMethod)) {
            // 处理GET请求
            if (queryParam != null && !queryParam.isEmpty()) {
                StringBuilder queryString = new StringBuilder();
                for (Map.Entry<String, String> entry : queryParam.entrySet()) {
                    queryString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
                requestUrl += "?" + queryString.substring(0, queryString.length() - 1);
            }
            return new HttpGet(requestUrl);
        }

        return new HttpGet(requestUrl);
    }

    public static void main(String[] args) {
        try {
            // 测试GET请求
            String getResponse = sendGet("http://example.com");
            System.out.println("GET Response: " + getResponse);

            // 测试POST请求
            String postParams = "param1=value1&param2=value2";
            String postResponse = sendPost("http://example.com", postParams);
            System.out.println("POST Response: " + postResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



