package com.youloft.lilith.info.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.youloft.lilith.common.net.AbsResponse;

/**
 * 意见反馈的bean
 *
 * Created by gyh on 2017/7/9.
 */

public class FeedBackBean extends AbsResponse<FeedBackBean.DataBean>{


    public static class DataBean {

        @JSONField(name = "result")
        public boolean result;
        @JSONField(name = "msg")
        public String msg;
    }
}
