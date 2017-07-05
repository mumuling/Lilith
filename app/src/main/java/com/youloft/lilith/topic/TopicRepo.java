package com.youloft.lilith.topic;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.common.AbstractDataRepo;
import com.youloft.lilith.topic.bean.ReplyBean;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Flowable;

/**
 *version   话题数据中心
 *@author  slj
 *@time    2017/7/5 16:46
 *@class   TopicRepo
 */
@Route(path = "/repo/topic", name = "话题数据中心")
public class TopicRepo extends AbstractDataRepo {
    static HashMap<String, String> param = new HashMap();


    public static Flowable<ReplyBean> testReply(String vid,String uid,String limit) {
        param.clear();
        param.put("vid",vid);
        if (uid != null)  param.put("uid",uid);
        param.put("limit",limit);
        return unionFlow("http://lilith.51wnl.com/GetReplyList", null, param, true, ReplyBean.class, "test", 10);
    }

}
