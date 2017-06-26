package com.youloft.lilith.common.net;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Desc: 
 * Change: 
 * 
 * @version 
 * @author zchao created at 2017/6/26 14:43
 * @see 
*/
public class WebUtils {
    /**
     * GET方式獲取
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String getString(String url, Map<String, String> headers, Map<String, String> params) {
        try {
            Response response = request("GET", url, headers, params);
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 請求
     *
     * @param method
     * @param url
     * @param headers
     * @param params
     * @return
     */
    private static Response request(String method, String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        Request.Builder builder = new Request.Builder();
        if (headers != null)
            for (Map.Entry<String, String> header : headers.entrySet()) {
                builder.addHeader(header.getKey(), header.getValue());
            }

        String requestUrl = url;

        RequestBody mBody = null;
        if (params != null && !params.isEmpty()) {
            if (!"GET".equals(method)) {
                builder.url(url);
                FormEncodingBuilder bodyBuilder = new FormEncodingBuilder();
                for (Map.Entry<String, String> param : params.entrySet()) {
                    bodyBuilder.add(param.getKey(), param.getValue());
                }
                mBody = bodyBuilder.build();
            } else {
                StringBuilder sb = new StringBuilder(url);
                if (url.indexOf("?") > 0 & !url.endsWith("&")) {
                    sb.append("&");
                } else if (url.indexOf("?") < 0) {
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
        OkHttpClient client = new OkHttpClient();


        client.setConnectTimeout(15, TimeUnit.SECONDS);
        client.setWriteTimeout(15, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);


        return client.newCall(request).execute();
    }

}
