package com.youloft.lilith.topic.bean;


import com.alibaba.fastjson.annotation.JSONField;
import com.youloft.lilith.common.net.AbsResponse;

/**
 *
 */

public class ClickLikeBean extends AbsResponse<Boolean> {

    @JSONField(name = "sign")
    public String sign;
}
