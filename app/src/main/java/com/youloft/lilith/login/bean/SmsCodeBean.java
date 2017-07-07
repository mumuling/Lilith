package com.youloft.lilith.login.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.youloft.lilith.common.net.AbsResponse;

/**
 * 短信验证码的数据模型
 *
 * Created by GYH on 2017/7/6.
 */

public class SmsCodeBean extends AbsResponse<SmsCodeBean.DataBean>{



    public static class DataBean {
        /**
         * result : true
         * msg : null
         */
        @JSONField(name = "result")
        public boolean result;
        @JSONField(name = "msg")
        public Object msg;
    }
}
