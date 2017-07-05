package com.youloft.lilith.topic.bean;

import java.util.ArrayList;
import java.util.List;

/**    观点对象
 *version
 *@author  slj
 *@time    2017/7/4 17:32
 *@class   PointBean
 */

public class PointBean extends Object {
    public int mId;
    public int mTopicId;
    public int mUserId;
    public String mViewPoint;
    public int mZanCount;
    public int mReply;
    public int mSex;
    public int mSigns;
    public String mName;
    public String mHeadImage;
    public String mVote;
    public List<ReplyItem> mReplyList = new ArrayList<>();
    public PointBean(){}

    public static class ReplyItem {
        public ReplyItem(){}
        public int uId;
        public String uNickName;
        public String uContents;
    }

    public static List<PointBean> getPointListDefault() {
        List<PointBean> pointBeanList = new ArrayList<>();
        for (int i = 0; i < 10; i ++) {
            PointBean pointBean= new PointBean();
            ReplyItem replyItem = new ReplyItem();
            pointBean.mName = "星座达人" + i;
            pointBean.mHeadImage = "http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg";
            pointBean.mId = i;
            pointBean.mTopicId = i;
            pointBean.mUserId = i;
            pointBean.mZanCount = 888;
            pointBean.mSex = 1;
            pointBean.mSigns = 6;
            pointBean.mReply = 456;
            pointBean.mViewPoint = "相似更好";
            replyItem.uId = i;
            replyItem.uNickName = "用户" + i;
            replyItem.uContents = "用户" + i + "的评论";
            pointBean.mReplyList.add(replyItem);
            pointBeanList.add(pointBean);
        }
        return pointBeanList;

    }


}
