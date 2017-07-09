package com.youloft.lilith.info.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.youloft.lilith.common.net.AbsResponse;

/**
 * 验证老密码是否正确的bean
 *
 * Created by gyh on 2017/7/7.
 */

public class OldPasswordBean extends AbsResponse<OldPasswordBean.DataBean>{



    public static class DataBean {
        /**
         * result : true
         * message : null
         */
        @JSONField(name = "result")
        public boolean result;
        @JSONField(name = "message")
        public Object message;
    }
}
