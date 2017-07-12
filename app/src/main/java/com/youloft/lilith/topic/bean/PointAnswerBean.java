package com.youloft.lilith.topic.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 *
 */

public class PointAnswerBean  {

    /**
     * data : 26
     * status : 200
     * sign : b326b5062b2f0e69046810717534cb09
     */
    @JSONField(name = "data")
    public int data;
    @JSONField(name = "status")
    public int status;
    @JSONField(name = "sign")
    public String sign;
}
