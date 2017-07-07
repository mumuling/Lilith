package com.youloft.lilith.common.net;


import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.youloft.lilith.AppConfig;
import com.youloft.lilith.common.utils.DeviceUtil;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Desc: 网路请求, 同步请求{@link #requestExecute(String, Map, Map)}, {@link #requestExecute(String, Map, Map, boolean)},
 * 异步请求{@link #requestEnqueue(String, Map, Map, Class, IRequestResult) {@link #requestEnqueue(String, Map, Map, boolean, Class, IRequestResult)}}
 * Change:
 *
 * @author zchao created at 2017/6/26 14:43
 * @see
 */
public class OkHttpUtils {


    private static HashMap<String, String> sPublicParams = new HashMap<>();

    static {
        //不变的参数
        sPublicParams.put("cid", AppConfig.CID);
        sPublicParams.put("cc", AppConfig.CC);
        sPublicParams.put("av", AppConfig.VERSION_NAME);
        sPublicParams.put("chn", AppConfig.CHANNEL);
        sPublicParams.put("lang", AppConfig.LANG);
        sPublicParams.put("bd", AppConfig.Bundle);
        sPublicParams.put("tkn", AppConfig.TKN);
    }


    private static OkHttpClient client = null;

    private static OkHttpUtils instance = null;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public static OkHttpUtils getInstance() {
        if (instance == null) {
            synchronized (OkHttpUtils.class) {
                if (instance == null) {
                    instance = new OkHttpUtils();
                }
            }
        }

        return instance;
    }

    public OkHttpUtils() {
        client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(publicInterceptor)
                .build();
    }

    public OkHttpClient getClient() {
        return client;
    }

    /**
     * 拦截器，用于处理公共参数
     */
    Interceptor publicInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            String method = request.method();
            //不需要公共参数
            if (request.tag() != null && (request.tag() instanceof Boolean) && !(boolean) request.tag()) {
                return chain.proceed(request);
            }

            //针对post请求
            if (method.equalsIgnoreCase("post")) {
                RequestBody body = request.body();
                if (body instanceof MultipartBody) {
                    MultipartBody requestBody = createdNewMultipartBody((MultipartBody) body);
                    request = new Request.Builder().url(request.url()).post(requestBody).build();
                } else if (body instanceof FormBody) {
                    FormBody formBody = createdFormBody((FormBody) body);
                    request = new Request.Builder().url(request.url()).post(formBody).build();
                }
            } else if (method.equalsIgnoreCase("get")) {       //针对get请求

                request = addPublicParam(request);

            }

            Response response = null;
            response = chain.proceed(request);
            return response;
        }
    };

    private MultipartBody createdNewMultipartBody(MultipartBody body) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        HashMap<String, String> params = obtainPublicParams();
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                builder.addFormDataPart(param.getKey(), TextUtils.isEmpty(param.getValue())? "" : param.getValue());
            }
        }
        builder.addPart(body);
        return builder.build();
    }

    private FormBody createdFormBody(FormBody body) {
        FormBody.Builder builder = new FormBody.Builder();
        HashMap<String, String> params = obtainPublicParams();
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                builder.add(param.getKey(), TextUtils.isEmpty(param.getValue())? "" : param.getValue());
            }
        }
        for (int i = 0; i < body.size(); i++) {
            builder.addEncoded(body.encodedName(i), body.encodedValue(i));
        }
        return builder.build();
    }

    /**
     * 添加公共参数,返回一个新的请求Request
     *
     * @param oldRequest
     * @return
     */
    private Request addPublicParam(Request oldRequest) {
        HttpUrl.Builder builder = oldRequest.url()
                .newBuilder();
        HashMap<String, String> params = obtainPublicParams();
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                builder.setEncodedQueryParameter(param.getKey(), param.getValue());
            }
        }

        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(builder.build())
                .build();

        return newRequest;
    }

    /**
     * 获取公共参数
     *
     * @return
     */
    private HashMap<String, String> obtainPublicParams() {
        //可变参数
        sPublicParams.put("mac", DeviceUtil.getWifiMacAddress());
        sPublicParams.put("did", AppConfig.getDeviceId());
        sPublicParams.put("t", String.valueOf(System.currentTimeMillis() / 1000));
        return sPublicParams;
    }

    /**
     * GET方式获取
     *
     * @param url    地址
     * @param params 所有参数，
     * @return
     */
    public String getString(String url, Map<String, String> params) {
        return getString(url, params, true);
    }

    /**
     * GET方式获取
     * 说明：此方法只用于某些不需要公共参数的，如果请求需要公共参数直接使用{@link #getString(String, Map)}
     *
     * @param url             地址
     * @param params          业务参数，
     * @param usePublicParams 如果为false忽略公共参数，反正会在拦截器中自动添加上公共参数
     * @return
     */
    public String getString(String url, Map<String, String> params, boolean usePublicParams) {
        try {

            Response response = requestExecute(url, null, params, usePublicParams);
            if (response.isSuccessful()) {
                return response.body() == null ? "" : response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 同步GET請求
     *
     * @param baseUrl
     * @param headers
     * @param params
     * @return
     */
    public Response requestExecute(String baseUrl, Map<String, String> headers, Map<String, String> params) throws IOException {
        Request request = getRequest("GET", baseUrl, headers, params, true);

        return client.newCall(request).execute();
    }

    /**
     * 同步GET請求
     *
     * @param baseUrl
     * @param headers
     * @param params
     * @param usePublicParam 如果为false忽略公共参数，反正会在拦截器中自动添加上公共参数
     * @return
     */
    public Response requestExecute(String baseUrl, Map<String, String> headers, Map<String, String> params, boolean usePublicParam) throws IOException {
        Request request = getRequest("GET", baseUrl, headers, params, usePublicParam);

        return client.newCall(request).execute();
    }

    /**
     * 异步GET請求
     *
     * @param baseUrl
     * @param headers
     * @param params
     * @return
     */
    public <T> void requestEnqueue(String baseUrl, Map<String, String> headers, Map<String, String> params, Class<T> cls, IRequestResult<T> callBack) {
        Request request = getRequest("GET", baseUrl, headers, params, true);
        try {
            client.newCall(request).enqueue(new RequestCallBack<>(callBack, cls));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步GET請求
     *
     * @param baseUrl
     * @param headers
     * @param params
     * @param usePublicParam 如果为false忽略公共参数，反正会在拦截器中自动添加上公共参数
     * @return
     */
    public <T> void requestEnqueue(String baseUrl, Map<String, String> headers, Map<String, String> params,
                                   boolean usePublicParam, Class<T> cls, IRequestResult<T> callBack) {
        Request request = getRequest("GET", baseUrl, headers, params, usePublicParam);
        try {
            client.newCall(request).enqueue(new RequestCallBack<>(callBack, cls));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 对url进行组装
     *
     * @param baseUrl 基础url
     * @param params  业务参数
     * @return
     */
    public String constructUrl(String baseUrl, Map<String, String> params) {
        if (TextUtils.isEmpty(baseUrl)) {
            return "";
        }
        StringBuilder sb = new StringBuilder(baseUrl);
        if (baseUrl.indexOf("?") > 0 & !baseUrl.endsWith("&")) {
            sb.append("&");
        } else if (baseUrl.indexOf("?") < 0) {
            sb.append("?");
        }
        for (Map.Entry<String, String> param : params.entrySet()) {
            try {
                if (TextUtils.isEmpty(param.getValue())) continue;
                sb.append(URLEncoder.encode(param.getKey(), "UTF-8"))
                        .append('=')
                        .append(URLEncoder.encode(param.getValue(), "UTF-8"))
                        .append("&");
            } catch (UnsupportedEncodingException e) {
                throw new AssertionError(e);
            }
        }

        return sb.substring(0, sb.length() - 1);
    }

    /**
     * post方法请求
     *
     * @param baseUrl
     * @param params         表单参数
     * @param usePublicParam 是否需要添加公共参数
     * @param files          需要上传的文件
     * @return
     * @throws IOException
     */
    public Response post(String baseUrl, Map<String, String> headers, Map<String, String> params, boolean usePublicParam, File... files) throws IOException {

        RequestBody mBody = null;
        if (files.length > 0) {
            MultipartBody.Builder bodyBulider = new MultipartBody.Builder().setType(MultipartBody.FORM);

            for (String key : params.keySet()) {
                bodyBulider.addFormDataPart(key, params.get(key));
            }
            for (File file : files) {
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                bodyBulider.addFormDataPart("file", file.getName(), fileBody);
            }
            mBody = bodyBulider.build();
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
            mBody = builder.build();
        }

        Request.Builder builder = new Request.Builder().url(baseUrl).post(mBody);

        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                builder.addHeader(header.getKey(), header.getValue());
            }
        }
        builder.tag(usePublicParam);
        Request request = builder.build();
        return client.newCall(request).execute();
    }


    /**
     * 构建request
     *
     * @param method         方法名，如果是get请求则传入 "GET"
     * @param baseUrl        url
     * @param headers        header参数
     * @param params         业务参数
     * @param usePublicParam 是否自动添加公共参数；如果为false则在拦截器中处理时不添加公共参数
     * @return
     */
    private Request getRequest(String method, String baseUrl, Map<String, String> headers, Map<String, String> params, boolean usePublicParam) {
        Request.Builder builder = new Request.Builder();
        builder.tag(usePublicParam);
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
                requestUrl = constructUrl(baseUrl, params);
            }
        }

        builder.url(requestUrl);
        builder.method(method, mBody);
        return builder.build();
    }


    public static String SERVER_ERROR = "请求失败";

    public static String NETWORK_ERROR = "请检查网络连接";

    /**
     * Desc: okhttp回调接口，继承自okhttp的callback
     * Change:
     *
     * @author zchao created at 2017/6/28 11:33
     * @see
     */
    public class RequestCallBack<T> implements Callback {

        private IRequestResult<T> mIRequestResult;

        private Class<T> clazz;

        private String notifyMsg = "";


        public RequestCallBack(IRequestResult<T> mIRequestResult, Class<T> clazz) {
            this.mIRequestResult = mIRequestResult;
            this.clazz = clazz;
        }


        @Override
        public void onFailure(Call call, IOException e) {
            //请求失败发送失败消息到主线程
            notifyMsg = NETWORK_ERROR;
            postErrorMsg();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                //请求成功
                String result = response.body().string();
                final T res = JSON.parseObject(result, clazz);
                if (res != null) {
                    postSuccessMsg(res);
                } else {
                    notifyMsg = SERVER_ERROR;
                    postErrorMsg();
                }
            } else {
                notifyMsg = NETWORK_ERROR;
                postErrorMsg();
            }
        }


        /**
         * 主线程发送错误消息
         */
        private void postErrorMsg() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mIRequestResult.onCompleted();
                    mIRequestResult.onFailure(notifyMsg);
                }
            });
        }

        /**
         * 主线程发送正确消息
         */
        private void postSuccessMsg(final T result) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mIRequestResult.onCompleted();
                    mIRequestResult.onSuccessful(result);
                }
            });
        }

    }
}
