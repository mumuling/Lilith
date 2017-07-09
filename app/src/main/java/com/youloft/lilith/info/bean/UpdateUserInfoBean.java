package com.youloft.lilith.info.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.youloft.lilith.common.net.AbsResponse;

/**
 * Created by gyh on 2017/7/9.
 */

public class UpdateUserInfoBean extends AbsResponse<UpdateUserInfoBean.DataBean>{



    public static class DataBean {
        @JSONField(name = "result")
        public int result;
        @JSONField(name = "message")
        public Object message;
    }
}

