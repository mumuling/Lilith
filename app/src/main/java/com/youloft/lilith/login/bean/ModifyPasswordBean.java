package com.youloft.lilith.login.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.youloft.lilith.common.net.AbsResponse;

/**
 * 短信修改密码之后返回的数据
 *
 * Created by gyh on 2017/7/7.
 */

public class ModifyPasswordBean extends AbsResponse<ModifyPasswordBean.DataBean>{


    public static class DataBean {
        /**
         * result : 0
         * message :
         */
        @JSONField(name = "result")
        public int result;
        @JSONField(name = "message")
        public String message;
    }
}
