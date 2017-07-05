package com.youloft.lilith.common.net;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 抽象响应类
 * Created by coder on 2017/6/26.
 */
public abstract class AbsResponse<T> {

    @JSONField(name = "status")
    public int status;

    @JSONField(name = "data")
    public T data;

    /**
     * 是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        return status == 200;
    }

}
