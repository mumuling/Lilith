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
            public int id;
            @JSONField(name = "phone")
            public String phone;
            @JSONField(name = "nickName")
            public String nickName;
            @JSONField(name = "headImg")
            public String headImg;
            @JSONField(name = "sex")
            public int sex;
            @JSONField(name = "signs")
            public int signs;
            @JSONField(name = "birthDay")
            public String birthDay;
            @JSONField(name = "birthPlace")
            public String birthPlace;
            @JSONField(name = "livePlace")
            public String livePlace;
            @JSONField(name = "state")
            public int state;
            @JSONField(name = "accessToken")
            public String accessToken;

            @JSONField(name = "sunSigns")
            public int sunSigns;
            @JSONField(name = "moonSigns")
            public int moonSigns;
            @JSONField(name = "asceSigns")
            public int asceSigns;
            @JSONField(name = "birthLongi")
            public String birthLongi;
            @JSONField(name = "birthLati")
            public String birthLati;
            @JSONField(name = "liveLongi")
            public String liveLongi;
            @JSONField(name = "liveLati")
            public String liveLati;
        }


    }
}
