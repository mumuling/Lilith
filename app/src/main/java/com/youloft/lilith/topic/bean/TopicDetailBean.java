package com.youloft.lilith.topic.bean;

import java.util.ArrayList;
import java.util.List;

/**         话题详情对象
 *version
 *@author  slj
 *@time    2017/7/4 17:22
 *@class   TopicDetailBean
 */

public class TopicDetailBean extends Object {

    public int mId;
    public String mTitle;//标题
    public String mBackImg;// 话题背景图
    public int mCollection;// 收藏数
    public int mTotalVote;// 总投票数
    public int mIsClose;//是否关闭 0 否 1是
    public List<TopicOptionBean> mOptions = new ArrayList<>();//话题对应的选项，正方和反方



    public TopicDetailBean() {}

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

    public int getmColletion() {
        return mCollection;
    }

    public void setmColletion(int mColletion) {
        this.mCollection = mColletion;
    }

    public int getmTotalVote() {
        return mTotalVote;
    }

    public void setmTotalVote(int mTotalVote) {
        this.mTotalVote = mTotalVote;
    }

    public int getmIsClose() {
        return mIsClose;
    }

    public void setmIsClose(int mIsClose) {
        this.mIsClose = mIsClose;
    }

    public List<TopicOptionBean> getmOptions() {
        return mOptions;
    }

    public void setmOptions(List<TopicOptionBean> mOptions) {
        this.mOptions = mOptions;
    }




}
