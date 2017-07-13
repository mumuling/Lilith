package com.youloft.lilith.topic.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.youloft.lilith.common.net.AbsResponse;

import java.io.Serializable;
import java.util.List;

/**    观点对象
 *version
 *@author  slj
 *@time    2017/7/4 17:32
 *@class   PointBean
 */

public class PointBean extends AbsResponse<List<PointBean.DataBean>> implements Serializable{


    public static class DataBean implements Serializable{
        /**
         * id : 2
         * topicId : 10000
         * topicOptionId : 1
         * userId : 10000
         * viewpoint : 支持
         * zan : 2
         * reply : 1
         * sex : 0
         * signs : 0
         * headImg :
         * isclick : 1
         * nickName : 小虎儿
         * replyList : [{"uid":10000,"nickName":"小虎儿","contents":"绝世你的"}]
         */
        @JSONField(name = "id")
        public int id;
        @JSONField(name = "topicId")
        public int topicId;
        @JSONField(name = "topicOptionId")
        public int topicOptionId;
        @JSONField(name = "userId")
        public int userId;
        @JSONField(name = "viewpoint")
        public String viewpoint;
        @JSONField(name = "zan")
        public int zan;
        @JSONField(name = "reply")
        public int reply;
        @JSONField(name = "sex")
        public int sex;
        @JSONField(name = "signs")
        public int signs;
        @JSONField(name = "headImg")
        public String headImg;
        @JSONField(name = "buildDate")
        public String buildDate;
        @JSONField(name = "isclick")
        public int isclick;
        @JSONField(name = "nickName")
        public String nickName;
        @JSONField(name = "replyList")
        public List<ReplyListBean> replyList;

        public static class ReplyListBean implements Serializable{
            /**
             * uid : 10000
             * nickName : 小虎儿
             * contents : 绝世你的
             */
            @JSONField(name = "uid")
            public int uid;
            @JSONField(name = "nickName")
            public String nickName;
            @JSONField(name = "contents")
            public String contents;
        }

//        public PointBean.DataBean setData(PointBean.DataBean data) {
//            PointBean.DataBean point = new PointBean.DataBean();
//            point.isclick
//        }
    }

}
