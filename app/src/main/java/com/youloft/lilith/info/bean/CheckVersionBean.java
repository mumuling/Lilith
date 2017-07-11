package com.youloft.lilith.info.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.youloft.lilith.common.net.AbsResponse;

/**
 * 检查更新的bean
 * Created by gyh on 2017/7/11.
 */

public class CheckVersionBean extends AbsResponse<CheckVersionBean.DataBean>{




    public static class DataBean {

        @JSONField(name = "version")
        public String version;
        @JSONField(name = "downPath")
        public String downPath;
        @JSONField(name = "contents")
        public String contents;
    }
}
