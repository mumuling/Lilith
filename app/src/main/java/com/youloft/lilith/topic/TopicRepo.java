package com.youloft.lilith.topic;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.common.AbstractDataRepo;
import com.youloft.lilith.common.net.Urls;
import com.youloft.lilith.topic.bean.PointBean;
import com.youloft.lilith.topic.bean.ReplyBean;
import com.youloft.lilith.topic.bean.TopicBean;
import com.youloft.lilith.topic.bean.TopicDetailBean;

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

    /**
     *     请求观点的评论信息
     * @param vid   观点id
     * @param uid   用户id
     * @param limit  请求条数
     * @return
     */
    public static Flowable<ReplyBean> getPointReply(String vid,String uid,String limit) {
        param.clear();
        param.put("vid",vid);
        if (uid != null)  param.put("uid",uid);
        param.put("limit",limit);
        return unionFlow(Urls.REPLY_LIST, null, param, true, ReplyBean.class, "point_reply", 2*60*1000);
    }

    /**
     *        请求话题列表
     * @param sortby  0 默认(参与人数 ) 1 首页 2 底部推荐 3时间倒序
     * @param limit   获取条数 默认10条
     * @return
     */
    public static  Flowable<TopicBean> getTopicList(String sortby,String limit) {
        param.clear();
        param.put("sortby",sortby);
        param.put("limit",limit);
        return unionFlow(Urls.TOPIC_LIST,null,param,true,TopicBean.class,"topic_list",5 * 60 * 1000);
    }

    /**   请求话题详情
     *
     * @param tid  话题编号
     * @return
     */
    public static Flowable<TopicDetailBean> getTopicDetail(String tid) {
        param.clear();
        param.put("tid",tid);
        return unionFlow(Urls.TOPIC_INFO,null,param,true,TopicDetailBean.class,"topic_detail" + tid,5 * 60 * 1000);
    }

    public static Flowable<PointBean> getPointList(String tid,String uid) {
        param.clear();
        param.put("tid",tid);
        if (uid != null)param.put("uid",uid);
        return unionFlow(Urls.VOTE_LIST,null,param,true,PointBean.class,"point_list" + tid,5 * 60 * 1000);
    }

}
