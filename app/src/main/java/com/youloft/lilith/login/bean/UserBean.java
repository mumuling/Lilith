package com.youloft.lilith.login.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.youloft.lilith.common.net.AbsResponse;

/**
 * 快捷登录返回的user信息
 *
 * Created by GYH on 2017/7/6.
 */

public class UserBean extends AbsResponse<UserBean.DataBean>{


    public static class DataBean {
        /**
         * result : 0
         * message :
         * userInfo : {"id":10002,"phone":"15111928029","nickName":"小鬼头","headImg":"http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg","sex":1,"signs":6,"birthDay":"1987-07-05 13:15:00","birthPlace":"110105","livePlace":"120105","state":1,"accessToken":"6def023733252044b045d48eab00dab0"}
         */
        @JSONField(name = "result")
        public int result;
        @JSONField(name = "message")
        public String message;
        @JSONField(name = "userInfo")
        public UserInfoBean userInfo;

        public static class UserInfoBean {
            /**
             * id : 10002
             * phone : 15111928029
             * nickName : 小鬼头
             * headImg : http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg
             * sex : 1
             * signs : 6
             * birthDay : 1987-07-05 13:15:00
             * birthPlace : 110105
             * livePlace : 120105
             * state : 1
             * accessToken : 6def023733252044b045d48eab00dab0
             */
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
        }
    }
}
