package com.youloft.lilith.common.net;


import android.os.Handler;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.http.PUT;


/**
 * Desc: 
 * Change: 
 * 
 * @version 
 * @author zchao created at 2017/6/26 14:43
 * @see 
*/
public class WebUtils {

    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    /**
     * GET方式获取
     *
     * @param url 地址
     * @param params 所有参数，
     * @return
     */
    public static String getString(String url,  Map<String, String> params) {
        try {
            Response response = request("GET", url, null, params);
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 同步請求
     *
     * @param method
     * @param baseUrl
     * @param headers
     * @param params
     * @return
     */
    private static Response request(String method, String baseUrl, Map<String, String> headers, Map<String, String> params) throws IOException {
        Request.Builder builder = new Request.Builder();
        if (headers != null)
            for (Map.Entry<String, String> header : headers.entrySet()) {
                builder.addHeader(header.getKey(), header.getValue());
            }

        String requestUrl = baseUrl;

        RequestBody mBody = null;
        if (params != null && !params.isEmpty()) {
            if (!"GET".equals(method)) {
                builder.url(baseUrl);
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                for (Map.Entry<String, String> param : params.entrySet()) {
                    bodyBuilder.add(param.getKey(), param.getValue());
                }
                mBody = bodyBuilder.build();
            } else {
                StringBuilder sb = new StringBuilder(baseUrl);
                if (baseUrl.indexOf("?") > 0 & !baseUrl.endsWith("&")) {
                    sb.append("&");
                } else if (baseUrl.indexOf("?") < 0) {
                    sb.append("?");
                }
                for (Map.Entry<String, String> param : params.entrySet()) {
                    try {
                        sb.append(URLEncoder.encode(param.getKey(), "UTF-8"))
                                .append('=')
                                .append(URLEncoder.encode(param.getValue(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        throw new AssertionError(e);
                    }
                }
                requestUrl = sb.toString();
            }
        }

        builder.url(requestUrl);
        builder.method(method, mBody);
        Request request = builder.build();

        return client.newCall(request).execute();
    }




}
