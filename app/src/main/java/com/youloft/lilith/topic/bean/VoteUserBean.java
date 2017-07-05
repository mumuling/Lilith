package com.youloft.lilith.topic.bean;

/**      参与用户的信息
 *version
 *@author  slj
 *@time    2017/7/4 17:08
 *@class   VoteUserBean
 */

public class VoteUserBean extends Object {
    public int mId;
    public String mHeadImg;

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmHeadImg() {
        return mHeadImg;
    }

    public void setmHeadImg(String mHeadImg) {
        this.mHeadImg = mHeadImg;
    }

    public int getmSex() {
        return mSex;
    }

    public void setmSex(int mSex) {
        this.mSex = mSex;
    }

    public String getmSigns() {
        return mSigns;
    }

    public void setmSigns(String mSigns) {
        this.mSigns = mSigns;
    }

    public int mSex;// 0 未知 1女 2男
    public String mSigns;
    public VoteUserBean() {

    }
    public VoteUserBean(int id,String headImg,int sex,String signs) {
        this.mId = id;
        this.mHeadImg = headImg;
        this.mSex = sex;
        this.mSigns = signs;
    }

}
