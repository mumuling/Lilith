package com.youloft.lilith.topic.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.youloft.lilith.common.net.AbsResponse;

/**
 *
 */

public class PointAnswerBean  extends AbsResponse{

    /**
     * data : 26
     * status : 200
     * sign : b326b5062b2f0e69046810717534cb09
     */
    @JSONField(name = "data")
    public int data;
}
