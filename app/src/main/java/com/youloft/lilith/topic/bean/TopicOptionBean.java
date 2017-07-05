package com.youloft.lilith.topic.bean;

/**      话题对应的选项
 *version
 *@author  slj
 *@time    2017/7/4 17:26
 *@class   TopicOptionBean
 */

public class TopicOptionBean extends Object {

    public int mId;
    public String mShortTitle;//短标题
    public String mTitle;//标题
    public int mVote;//票数
    public String mTime;

    public TopicOptionBean() {}
    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmShortTitle() {
        return mShortTitle;
    }

    public void setmShortTitle(String mShortTitle) {
        this.mShortTitle = mShortTitle;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getmVote() {
        return mVote;
    }

    public void setmVote(int mVote) {
        this.mVote = mVote;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }


}
