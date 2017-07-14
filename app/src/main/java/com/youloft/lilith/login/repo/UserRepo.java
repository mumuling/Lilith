package com.youloft.lilith.login.repo;

import com.youloft.lilith.common.AbstractDataRepo;
import com.youloft.lilith.common.net.Urls;
import com.youloft.lilith.login.bean.UserBean;

import java.util.HashMap;

import io.reactivex.Flowable;

/**
 * User信息请求描述类
 *
 * Created by GYH on 2017/7/6.
 */

public class UserRepo extends AbstractDataRepo{
    //快捷登录
    static HashMap<String,String> params = new HashMap();

    public static Flowable<UserBean> loginForUserInfo(String phone, String code) {
        params.put("phone",phone);
        params.put("code",code);
        return httpFlow(Urls.QUICKLY_LOGIN_URL, null, params, true, UserBean.class, "user_info", 0);
    }
}
