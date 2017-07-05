package com.youloft.lilith.topic.bean;

import com.youloft.lilith.common.net.AbsResponse;

import java.util.List;

/**    观点回复的对象
 *version
 *@author  slj
 *@time    2017/7/4 17:53
 *@class   ReplyBean
 */

public class ReplyBean extends AbsResponse<List<ReplyBean.DataBean>> {

    public static class DataBean {
        /**
         * id : 5
         * uid : 10001
         * headImg : http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg
         * nickName : 什么鬼2
         * contents : 就是干
         * pid : 4
         * pName : 什么鬼2
         * pContents : 什么
         * sex : 1
         * signs : 7
         * date : 2017-06-28 17:05:22
         * zan : 1
         * isclick : 1
         */

        public int id;
        public int uid;
        public String headImg;
        public String nickName;
        public String contents;
        public int pid;
        public String pName;
        public String pContents;
        public int sex;
        public int signs;
        public String date;
        public int zan;
        public int isclick;
    }



}
