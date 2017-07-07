package com.youloft.lilith.login.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.youloft.lilith.common.net.AbsResponse;

/**
 * 发送短信的bean
 *
 * Created by GYH on 2017/7/6.
 */

public class SendSmsBean extends AbsResponse<SendSmsBean.DataBean>{

    /**
     * data : {"isSend":true,"msg":""}
     * sign : 07b761c2a280007e34a818ba9b0adafb
     */


    public static class DataBean {

        @JSONField(name = "isSend")
        public boolean isSend;
        @JSONField(name = "msg")
        public String msg;
    }
}
