package com.youloft.lilith.topic.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.youloft.lilith.common.net.AbsResponse;

/**
 *
 */

public class VoteBean extends AbsResponse<Integer> {

    @JSONField(name = "data")
    public int data;
    public String sign;
}
