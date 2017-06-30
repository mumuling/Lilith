package com.youloft.lilith.api;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * LilithApi用于请求相应网络接口
 * <p>
 * Created by coder on 2017/6/30.
 */
public interface LilithApi {

    @GET("/")
    Observable<String> getBaiduContent();
}
