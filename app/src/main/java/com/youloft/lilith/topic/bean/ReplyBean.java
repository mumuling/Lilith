package com.youloft.lilith.topic.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.youloft.lilith.common.net.AbsResponse;

import java.io.Serializable;
import java.util.List;

/**    观点回复的对象
 *version
 *@author  slj
 *@time    2017/7/4 17:53
 *@class   ReplyBean
 */

public class ReplyBean extends AbsResponse<List<ReplyBean.DataBean>> {

    public static class DataBean implements Serializable{
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
        @JSONField(name = "id")
        public int id;
        @JSONField(name = "uid")
        public int uid;
        @JSONField(name = "headImg")
        public String headImg;
        @JSONField(name = "nickName")
        public String nickName;
        @JSONField(name = "contents")
        public String contents;
        @JSONField(name = "pid")
        public int pid;
        @JSONField(name = "pName")
        public String pName;
        @JSONField(name = "pContents")
        public String pContents;
        @JSONField(name = "sex")
        public int sex;
        @JSONField(name = "signs")
        public int signs;
        @JSONField(name = "date")
        public String date;
        @JSONField(name = "zan")
        public int zan;
        @JSONField(name = "isclick")
        public int isclick;
    }



}
