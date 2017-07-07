package com.youloft.lilith.login.bean;

import com.youloft.lilith.common.net.AbsResponse;

/**
 * 手机+密码登录,返回的user信息
 *
 * Created by gyh on 2017/7/7.
 */

public class LoginUserInfoBean extends AbsResponse<LoginUserInfoBean.DataBean>{

    public static class DataBean {
        /**
         * result : 0
         * message :
         * userInfo : {"id":10002,"phone":"15111928029","nickName":"小鬼头","headImg":"http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg","sex":1,"signs":6,"birthDay":"1987-07-05 13:15:00","birthPlace":"110105","livePlace":"120105","state":1,"accessToken":"82991f47144a1d14f8f6e5679f67c405","sunSigns":0,"moonSigns":0,"asceSigns":0}
         */

        public int result;
        public String message;
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
             * accessToken : 82991f47144a1d14f8f6e5679f67c405
             * sunSigns : 0
             * moonSigns : 0
             * asceSigns : 0
             */

            public int id;
            public String phone;
            public String nickName;
            public String headImg;
            public int sex;
            public int signs;
            public String birthDay;
            public String birthPlace;
            public String livePlace;
            public int state;
            public String accessToken;
            public int sunSigns;
            public int moonSigns;
            public int asceSigns;
        }
    }
}
