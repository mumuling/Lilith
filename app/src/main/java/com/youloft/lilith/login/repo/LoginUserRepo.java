package com.youloft.lilith.login.repo;

import com.youloft.lilith.common.AbstractDataRepo;
import com.youloft.lilith.common.net.Urls;
import com.youloft.lilith.login.bean.LoginUserInfoBean;

import java.util.HashMap;

import io.reactivex.Flowable;

/**
 * 手机+密码登录的描述类
 *
 * Created by GYH on 2017/7/7.
 */

public class LoginUserRepo extends AbstractDataRepo{
    static HashMap<String,String> params = new HashMap();

    public static Flowable<LoginUserInfoBean> loginWithPassword(String phone, String password) {
        params.put("phone",phone);
        params.put("pwd",password);
        return unionFlow(Urls.LOGIN_URL, null, params, true, LoginUserInfoBean.class, "login_user_info", 1);
    }
}
