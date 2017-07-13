package com.youloft.lilith.login.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.youloft.lilith.common.net.AbsResponse;

/**
 * 用户信息数据模型
 * <p>
 * Created by GYH on 2017/7/6.
 */

public class UserBean extends AbsResponse<UserBean.DataBean> {


    public static class DataBean {

        @JSONField(name = "result")
        public int result;
        @JSONField(name = "message")
        public String message;
        @JSONField(name = "userInfo")
        public UserInfoBean userInfo;

        public static class UserInfoBean {
            @JSONField(name = "id")
            public int id;   //id
            @JSONField(name = "phone")
            public String phone;   //电话
            @JSONField(name = "nickName")
            public String nickName;   //昵称
            @JSONField(name = "headImg")
            public String headImg;  //头像链接
            @JSONField(name = "sex")
            public int sex;   //性别
            @JSONField(name = "signs")
            public int signs;    //星座
            @JSONField(name = "birthDay")
            public String birthDay;  //出生日期  yyyy-MM-dd HH:mm:ss
            @JSONField(name = "birthPlace")
            public String birthPlace;  //出生地
            @JSONField(name = "livePlace")
            public String livePlace;  //现居地
            @JSONField(name = "state")
            public int state;
            @JSONField(name = "accessToken")
            public String accessToken;

            @JSONField(name = "sunSigns")
            public int sunSigns;    //太阳星座
            @JSONField(name = "moonSigns")
            public int moonSigns;  //月亮星座
            @JSONField(name = "asceSigns")
            public int asceSigns; //上升星座
            @JSONField(name = "birthLongi")
            public String birthLongi;  //出生地经度  这里还有个额外的作用,判断是不是新用户
            @JSONField(name = "birthLati")
            public String birthLati;   //出生地纬度
            @JSONField(name = "liveLongi")
            public String liveLongi;  //现居地经度
            @JSONField(name = "liveLati")
            public String liveLati; //现居地纬度
        }


    }
}
