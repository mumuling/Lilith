package com.youloft.lilith.register.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.youloft.lilith.common.net.AbsResponse;

/**
 * 注册时候返回的用户信息
 *
 * Created by gyh on 2017/7/7.
 */

public class RegisterUserBean extends AbsResponse<RegisterUserBean.DataBean>{



    public static class DataBean {
        /**
         * result : 0
         * message :
         * userInfo : {"id":10003,"phone":"15111111119","extInfo":"00000001","password":"E10ADC3949BA59ABBE56E057F20F883E","nickName":"星座达人301499071651","headImg":"","sex":1,"signs":3,"birthDay":"1990-04-01 12:00:00","birthPlace":"","birthLongi":"","birthLati":"","livePlace":"","liveLongi":"","liveLati":"","state":1,"buildDate":"2017-07-03 16:47:31","modifyDate":"2017-07-03 16:47:31"}
         */
        @JSONField(name = "result")
        public int result;
        @JSONField(name = "message")
        public String message;
        @JSONField(name = "userInfo")
        public UserInfoBean userInfo;

        public static class UserInfoBean {
            /**
             * id : 10003
             * phone : 15111111119
             * extInfo : 00000001
             * password : E10ADC3949BA59ABBE56E057F20F883E
             * nickName : 星座达人301499071651
             * headImg :
             * sex : 1
             * signs : 3
             * birthDay : 1990-04-01 12:00:00
             * birthPlace :
             * birthLongi :
             * birthLati :
             * livePlace :
             * liveLongi :
             * liveLati :
             * state : 1
             * buildDate : 2017-07-03 16:47:31
             * modifyDate : 2017-07-03 16:47:31
             */
            @JSONField(name = "id")
            public int id;
            @JSONField(name = "phone")
            public String phone;
            @JSONField(name = "extInfo")
            public String extInfo;
            @JSONField(name = "password")
            public String password;
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
            @JSONField(name = "birthLongi")
            public String birthLongi;
            @JSONField(name = "birthLati")
            public String birthLati;
            @JSONField(name = "livePlace")
            public String livePlace;
            @JSONField(name = "liveLongi")
            public String liveLongi;
            @JSONField(name = "liveLati")
            public String liveLati;
            @JSONField(name = "state")
            public int state;
            @JSONField(name = "buildDate")
            public String buildDate;
            @JSONField(name = "modifyDate")
            public String modifyDate;
        }
    }
}
