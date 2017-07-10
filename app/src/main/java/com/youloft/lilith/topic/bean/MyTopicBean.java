package com.youloft.lilith.topic.bean;

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

        public int id;
        public int topicId;
        public String topicIdTitle;
        public int topicOptionId;
        public String optionTitle;
        public String Viewpoint;
        public int zan;
        public int reply;
        public String date;
        public int isclick;
        public String sdate;
    }
}
