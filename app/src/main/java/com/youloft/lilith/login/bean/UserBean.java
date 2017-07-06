package com.youloft.lilith.login.bean;

import com.youloft.lilith.common.net.AbsResponse;

/**
 * 登录成功返回的数据模型
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
             * accessToken : 6def023733252044b045d48eab00dab0
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
        }
    }
}
