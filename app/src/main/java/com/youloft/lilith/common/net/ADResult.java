package com.youloft.lilith.common.net;

import com.alibaba.fastjson.annotation.JSONField;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Desc: 万年历数据封装公共类
 * Change:
 *
 * @author zchao created at 2017/2/27 11:38
 * @see
 */
public class ADResult<T> implements IJsonObject {

    @JSONField(name = "sign")
    public String sign;

    @JSONField(name = "status")
    public int status;

    @JSONField(name = "msg")
    public String msg;

    @JSONField(name = "dateTime")
    public String dateTime;

    @JSONField(name = "apiVersion")
    public String apiVersion;

    @JSONField(name = "server")
    public String server;

    @JSONField(name = "t")
    public long t;

    @JSONField(name = "data")
    public T data;


    public boolean isSuccess() {
        return status == 200;
    }


    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }

    public boolean isSuccessOrThrow() {
        if (!isSuccess()) {
            throw new RuntimeException(msg);
        }
        return isSuccess();
    }
}
