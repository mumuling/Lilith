package com.youloft.lilith.topic.bean;

import com.youloft.lilith.common.net.AbsResponse;

import java.util.List;

/**    话题对象
 *version
 *@author  slj
 *@time    2017/7/4 17:01
 *@class   TopicBean
 */

public class TopicBean extends AbsResponse<List<TopicBean.DataBean>> {

    /**
     * data : [{"id":10000,"title":"爱情，究竟应该是互补还是相似的两个人在一起呢？","backImg":"http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg","totalVote":3,"voteUser":[{"id":10001,"sex":1,"signs":1,"headImg":"http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg"},{"id":10000,"sex":2,"signs":1,"headImg":"http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg"}]},{"id":10001,"title":"基情，究竟应该是互补还是相似的两个人在一起呢？","backImg":"http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg","totalVote":3,"voteUser":[{"id":10001,"sex":1,"signs":1,"headImg":"http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg"}]}]
     * sign : 4f1fca460ee3286a60b4b2803958888e
     */

    public static class DataBean {
        /**
         * id : 10000
         * title : 爱情，究竟应该是互补还是相似的两个人在一起呢？
         * backImg : http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg
         * totalVote : 3
         * voteUser : [{"id":10001,"sex":1,"signs":1,"headImg":"http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg"},{"id":10000,"sex":2,"signs":1,"headImg":"http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg"}]
         */

        public int id;
        public String title;
        public String backImg;
        public int totalVote;
        public List<VoteUserBean> voteUser;

        public static class VoteUserBean {
            /**
             * id : 10001
             * sex : 1
             * signs : 1
             * headImg : http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg
             */

            public int id;
            public int sex;
            public int signs;
            public String headImg;
        }
    }
}

