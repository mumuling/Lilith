package com.youloft.lilith.register.repo;

import com.youloft.lilith.common.AbstractDataRepo;
import com.youloft.lilith.common.net.Urls;
import com.youloft.lilith.login.bean.UserBean;

import java.util.HashMap;

import io.reactivex.Flowable;

/**
 * 注册描述类
 *
 * Created by gyh on 2017/7/7.
 */

public class RegisterUserRepo extends AbstractDataRepo{
    static HashMap<String,String> params = new HashMap();

    public static Flowable<UserBean> registerUser(String phone, String code, String password) {
        params.put("phone",phone);
        params.put("code",code);
        params.put("pwd",password);
        return unionFlow(Urls.REGISTER_URL, null, params, true, UserBean.class, "register_user_info", 1);
    }
}
