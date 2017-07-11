package com.youloft.lilith.topic.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.youloft.lilith.common.net.AbsResponse;

import java.util.ArrayList;
import java.util.List;

/**         话题详情对象
 *version
 *@author  slj
 *@time    2017/7/4 17:22
 *@class   TopicDetailBean
 */

public class TopicDetailBean extends AbsResponse<TopicDetailBean.DataBean> {


    public static class DataBean {
        /**
         * id : 10000
         * title : 爱情，究竟应该是互补还是相似的两个人在一起呢？
         * backImg : http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg
         * collection : 0
         * totalVote : 2
         * isClose : 0
         * option : [{"id":1,"shortTitle":"相识","title":"相似更好，因为可以更好的理解对方","vote":2,"buildDate":"2017-06-27 20:48:43"},{"id":3,"shortTitle":"互补","title":"互补更好，不说了","vote":0,"buildDate":"2017-06-27 20:49:07"}]
         */
        @JSONField(name = "id")
        public int id;
        @JSONField(name = "title")
        public String title;
        @JSONField(name = "backImg")
        public String backImg;
        @JSONField(name = "collection")
        public int collection;
        @JSONField(name = "totalVote")
        public int totalVote;
        @JSONField(name = "isClose")
        public int isClose;
        @JSONField(name = "isVote")
        public int isVote;
        @JSONField(name = "option")
        public ArrayList<OptionBean> option;

        public static class OptionBean {
            /**
             * id : 1
             * shortTitle : 相识
             * title : 相似更好，因为可以更好的理解对方
             * vote : 2
             * buildDate : 2017-06-27 20:48:43
             */
            @JSONField(name = "id")
            public int id;
            @JSONField(name = "shortTitle")
            public String shortTitle;
            @JSONField(name = "title")
            public String title;
            @JSONField(name = "vote")
            public int vote;
            @JSONField(name = "buildDate")
            public String buildDate;
        }
    }
}
