package com.youloft.lilith.topic.bean;

import com.alibaba.android.arouter.facade.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**    话题对象
 *version
 *@author  slj
 *@time    2017/7/4 17:01
 *@class   TopicBean
 */

public class TopicBean extends Object {

    public int mId;
    public String mTitle;//
    public String mBackImg;//背景图地址
    public int mTotalVote;//总的参与数
    public List<VoteUserBean> mVoteUserList = new ArrayList<>();//参与用户信息，主要取里面的用户头像

    public TopicBean(){}

    public static List<TopicBean> getTopicListDefault() {
        List<TopicBean> topicBeanList = new ArrayList<>();
        for (int i = 0 ;i < 10; i ++) {
            TopicBean topicBean = new TopicBean();
            VoteUserBean voteUserBean = new VoteUserBean();
            topicBean.mId = i;
            topicBean.mTitle = "话题" + i;
            topicBean.mBackImg = "http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg";
            topicBean.mTotalVote = 999;
            voteUserBean.mHeadImg = "http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg";
            topicBean.mVoteUserList.add(voteUserBean);
            topicBeanList.add(topicBean);
        }
        return topicBeanList;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmBackImg() {
        return mBackImg;
    }

    public void setmBackImg(String mBackImg) {
        this.mBackImg = mBackImg;
    }

    public int getmTotalVote() {
        return mTotalVote;
    }

    public void setmTotalVote(int mTotalVote) {
        this.mTotalVote = mTotalVote;
    }

    public List<VoteUserBean> getmVoteUserList() {
        return mVoteUserList;
    }

    public void setmVoteUserList(List<VoteUserBean> mVoteUserList) {
        this.mVoteUserList = mVoteUserList;
    }



}

