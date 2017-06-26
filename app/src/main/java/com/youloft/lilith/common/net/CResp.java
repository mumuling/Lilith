package com.youloft.lilith.common.net;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Desc: C.51wnl.com返回的公共消息主体
 * Change:
 *
 * @version
 * @author zchao created at 2017/6/26 14:27
 * @see
*/
public class CResp<T> implements IJsonObject {

    @JSONField(name = "r")
    public String r;

    @JSONField(name = "status")
    public int status;

    @JSONField(name = "msg")
    public T data;
}
