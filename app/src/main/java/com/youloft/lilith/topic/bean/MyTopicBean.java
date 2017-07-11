package com.youloft.lilith.topic.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.youloft.lilith.common.net.AbsResponse;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class MyTopicBean extends AbsResponse<ArrayList<MyTopicBean.DataBean>> {



    public static class DataBean {
        /**
         * id : 3
         * topicId : 10000
         * topicIdTitle : 爱情，究竟应该是互补还是相似的两个人在一起呢？
         * topicOptionId : 3
         * optionTitle : 互补更好，不说了
         * Viewpoint : 究竟应该是互补
         * zan : 0
         * reply : 0
         * date : 2017-06-28 13:54:08
         */
        @JSONField(name = "id")
        public int id;
        @JSONField(name = "topicId")
        public int topicId;
        @JSONField(name = "topicIdTitle")
        public String topicIdTitle;
        @JSONField(name = "topicOptionId")
        public int topicOptionId;
        @JSONField(name = "optionTitle")
        public String optionTitle;
        @JSONField(name = "Viewpoint")
        public String Viewpoint;
        @JSONField(name = "zan")
        public int zan;
        @JSONField(name = "reply")
        public int reply;
        @JSONField(name = "date")
        public String date;
        @JSONField(name = "isclick")
        public int isclick;
        @JSONField(name = "sdate")
        public String sdate;
    }
}
