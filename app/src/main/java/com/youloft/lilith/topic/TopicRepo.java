package com.youloft.lilith.topic;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.LLApplication;
import com.youloft.lilith.common.AbstractDataRepo;
import com.youloft.lilith.common.net.AbsResponse;
import com.youloft.lilith.common.net.Urls;
import com.youloft.lilith.topic.bean.ClickLikeBean;
import com.youloft.lilith.topic.bean.MyTopicBean;
import com.youloft.lilith.topic.bean.PointAnswerBean;
import com.youloft.lilith.topic.bean.PointBean;
import com.youloft.lilith.topic.bean.ReplyBean;
import com.youloft.lilith.topic.bean.TopicBean;
import com.youloft.lilith.topic.bean.TopicDetailBean;
import com.youloft.lilith.topic.bean.VoteBean;

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


    /**
     *     请求观点的评论信息
     * @param vid   观点id
     * @param uid   用户id
     * @param limit  请求条数
     * @return
     */
    public static Flowable<ReplyBean> getPointReply(String vid,String uid,String limit,String skip,boolean needCache) {
        String cacheKey = "point_reply" + vid;
        long cacheDuration = 1000 * 60;
        HashMap<String, String> param = new HashMap();
        param.put("vid",vid);
        if (uid != null)  param.put("uid",uid);
        if (limit!=null)param.put("limit",limit);
        if (skip!= null)param.put("skip",skip);
        if (needCache) {
            if (!LLApplication.getApiCache().isExpired(cacheKey,cacheDuration)) {
                return LLApplication.getApiCache().readCache(cacheKey,ReplyBean.class);
            } else {
                return httpFlow(Urls.REPLY_LIST, null, param, true, ReplyBean.class, cacheKey, cacheDuration);
            }
        } else {
            return httpFlow(Urls.REPLY_LIST, null, param, true, ReplyBean.class, null, 0);
        }
    }

    /**
     *        请求话题列表
     * @param sortby  0 默认(参与人数 ) 1 首页 2 底部推荐 3时间倒序
     * @param limit   获取条数 默认10条
     * @return
     */
    public static  Flowable<TopicBean> getTopicList(String sortby,String skip ,String limit,Boolean needCache) {
        String cacheKey = "topic_list" + sortby;
        long cacheDuration = 1000 * 60;
        HashMap<String, String> param = new HashMap();
        param.put("sortby",sortby);
        if (limit != null)param.put("limit",limit);
        if (skip != null)param.put("skip",skip);

        if (needCache) {
            if (!LLApplication.getApiCache().isExpired(cacheKey,cacheDuration)) {
                return LLApplication.getApiCache().readCache(cacheKey,TopicBean.class);
            } else {
                return httpFlow(Urls.TOPIC_LIST, null, param, true, TopicBean.class, cacheKey, cacheDuration);
            }
        } else {
            return httpFlow(Urls.TOPIC_LIST, null, param, true, TopicBean.class, null, 0);
        }
    }

    /**  底部其他话题推荐请求
     *
     * @param limit   请求条数
     * @param needCache  是否需要缓存
     * @return
     */
    public static Flowable<TopicBean> getTopicListBottom(String limit,String skip,boolean needCache) {
        String cacheKey = "topic_list_bottom";
        long cacheDuration = 2 * 1000;
        HashMap<String, String> param = new HashMap();
        if (limit != null)param.put("limit",limit);
        if (skip != null)param.put("skip",skip);
        param.put("sortby","2");
        if (needCache) {
            if (!LLApplication.getApiCache().isExpired(cacheKey,cacheDuration)) {
                return LLApplication.getApiCache().readCache(cacheKey,TopicBean.class);
            } else {
                return httpFlow(Urls.TOPIC_LIST, null, param, true, TopicBean.class, cacheKey, cacheDuration);
            }
        } else {
            return httpFlow(Urls.TOPIC_LIST, null, param, true, TopicBean.class, null, 0);
        }
    }

    /**   请求话题详情
     *
     * @param tid  话题编号
     * @return
     */
    public static Flowable<TopicDetailBean> getTopicDetail(String tid,String vid,boolean needCache) {
        String cacheKey = "topic_datail" + tid;
        long cacheDuration = 2 * 1000 * 60;
        HashMap<String, String> param = new HashMap();
        param.put("tid",tid);

        if (vid != null) param.put("uid",vid);
        if (needCache) {
            if (!LLApplication.getApiCache().isExpired(cacheKey,cacheDuration)) {
                return LLApplication.getApiCache().readCache(cacheKey,TopicDetailBean.class);
            } else {
                return httpFlow(Urls.TOPIC_INFO, null, param, true, TopicDetailBean.class, null, 0);
            }
        } else {
            return httpFlow(Urls.TOPIC_INFO, null, param, true, TopicDetailBean.class, null, 0);
        }

    }

    /**
     *     请求观点列表
     * @param tid   话题ID
     * @param uid  用户ID
     * @param limit  请求条数
     * @param skip   跳过条数
     * @param needCache   是否缓存
     * @return
     */
    public static Flowable<PointBean> getPointList(String tid,String uid,String limit,String skip,boolean needCache) {
        HashMap<String, String> param = new HashMap();
        param.put("tid",tid);
        if (uid != null)param.put("uid",uid);
        if (limit!=null)param.put("limit",limit);
        if (skip!=null)param.put("skip",skip);
        String cacheKey = "point_list" + tid;
        long duration = 60 * 1000;
        if (needCache) {
            if (!LLApplication.getApiCache().isExpired(cacheKey,duration)) {
                return LLApplication.getApiCache().readCache(cacheKey,PointBean.class);
            } else {
                return httpFlow(Urls.VOTE_LIST, null, param, true, PointBean.class, null, 0);
            }
        } else {
            return httpFlow(Urls.VOTE_LIST, null, param, true, PointBean.class, null, 0);
        }
    }

    /**
     *          投票
     * @param tid 话题id
     * @param oid  选择的观点ID
     * @param uid  用户ID
     * @param viewpoint   观点
     * @return
     */

    public static Flowable<VoteBean> postVote(String tid,String oid,String uid, String viewpoint) {
        HashMap<String, String> param = new HashMap();
        param.put("tid",tid);
        param.put("uid",uid);
        param.put("oid",oid);
        param.put("Viewpoint",viewpoint);
        return post(Urls.POST_VOTE,null,param,true,VoteBean.class,"awoiegewg",0);
    }

    /**     观点点赞
     *
     * @param vid  观点编号
     * @param uid   点赞人
     * @return
     */
    public static Flowable<ClickLikeBean> likePoint(String vid, String uid) {
        HashMap<String, String> param = new HashMap();
        param.put("vid",vid);
        param.put("uid",uid);
        return httpFlow(Urls.LIKE_POINT,null,param,true,ClickLikeBean.class,null,0);
    }

    /**
     *    回复点赞
     * @param rid  回复的ID
     * @param uid  用户的ID
     * @return
     */
    public static Flowable<ClickLikeBean> likeReply(String rid,String uid) {
        HashMap<String, String> param = new HashMap();
        param.put("rid",rid);
        param.put("uid",uid);
        return httpFlow(Urls.LIKE_REPLY,null,param,true,ClickLikeBean.class,null,0);
    }

    /**
     *      观点回复
     * @param vid   观点编号
     * @param uid   回复人
     * @param nickName   回复人昵称
     * @param msg     回复内容
     * @param pid     引用的回复编号
     * @return
     */
    public static Flowable<PointAnswerBean> reply(String vid, String uid, String nickName, String msg, String pid) {
        HashMap<String, String> param = new HashMap();
        param.put("vid",vid);
        param.put("uid",uid);
        param.put("NickName",nickName);
        param.put("txt",msg);
        param.put("pid",pid);
        return post(Urls.POINT_REPLY,null,param,true,PointAnswerBean.class,null,0);
    }

    /**
     *
     * @param uid   用户ID
     * @param skip  跳过条数
     * @param limit 请求条数
     * @return
     */
    public static Flowable<MyTopicBean> getMyTopic(String uid, String skip,String limit,boolean needCache) {
        HashMap<String, String> param = new HashMap();
        param.put("uid",uid);
        if (skip != null) param.put("skip",skip);
        if (limit != null) param.put("limit",limit);
        String cacheKey = "my_topic" + uid;
        long duration = 5 * 60 * 1000;
        if (needCache) {

//            if (!LLApplication.getApiCache().isExpired(cacheKey,duration)) {
//                return LLApplication.getApiCache().readCache(cacheKey,MyTopicBean.class);
//            } else {
                return httpFlow(Urls.MY_VOTE, null, param, true, MyTopicBean.class, null, 0);

        } else {
            return httpFlow(Urls.MY_VOTE, null, param, true, MyTopicBean.class, null, 0);
        }
    }

    public static Flowable<VoteBean> getCacheTime() {
        return unionFlow(Urls.Cache_Time,null,null,true,VoteBean.class,"ruquest_cache_time",1000 * 60 * 60 * 2);
    }
}
