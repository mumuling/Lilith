package com.youloft.lilith.register.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.youloft.lilith.common.net.AbsResponse;

/**
 * 检查电话号码是否存在的数据模型
 *
 * Created by GYH on 2017/7/7.
 */

public class CheckPhoneBean extends AbsResponse<CheckPhoneBean.DataBean>{



    public static class DataBean {
        /**
         * result : 0
         * message :
         * uid : 0
         */
        @JSONField(name = "result")
        public int result;
        @JSONField(name = "message")
        public String message;
        @JSONField(name = "uid")
        public int uid;
    }
}
