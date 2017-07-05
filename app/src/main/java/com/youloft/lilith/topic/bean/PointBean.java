package com.youloft.lilith.topic.bean;

import com.youloft.lilith.common.net.AbsResponse;

import java.util.List;

/**    观点对象
 *version
 *@author  slj
 *@time    2017/7/4 17:32
 *@class   PointBean
 */

public class PointBean extends AbsResponse<List<PointBean.DataBean>> {


    public static class DataBean {
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

        public int id;
        public int topicId;
        public int topicOptionId;
        public int userId;
        public String viewpoint;
        public int zan;
        public int reply;
        public int sex;
        public int signs;
        public String headImg;
        public int isclick;
        public String nickName;
        public List<ReplyListBean> replyList;

        public static class ReplyListBean {
            /**
             * uid : 10000
             * nickName : 小虎儿
             * contents : 绝世你的
             */

            public int uid;
            public String nickName;
            public String contents;
        }
    }
}
