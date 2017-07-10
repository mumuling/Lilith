package com.youloft.lilith.login.repo;

import com.youloft.lilith.common.AbstractDataRepo;
import com.youloft.lilith.common.net.Urls;
import com.youloft.lilith.login.bean.UserBean;

import java.util.HashMap;

import io.reactivex.Flowable;

/**
 *
 *
 * Created by GYH on 2017/7/7.
 */

public class LoginUserRepo extends AbstractDataRepo{


    //手机加密码登录
    static HashMap<String,String> params = new HashMap();

    public static Flowable<UserBean> loginWithPassword(String phone, String password) {
        params.put("phone",phone);
        params.put("pwd",password);
        return unionFlow(Urls.LOGIN_URL, null, params, true, UserBean.class, "login_user_info", 0);
    }

    //微信登录
    static HashMap<String,String> paramsWeChatLogin = new HashMap();

    /**
     *
     * @param nickName  昵称
     * @param platform  来源
     * @param headimgurl  头像地址
     * @param openId      openid
     * @param gender    性别
     * @return
     */
    public static Flowable<UserBean> wechatLogin(String nickName, String platform,String headimgurl,String openId,String gender) {
        paramsWeChatLogin.put("NickName",nickName);
        paramsWeChatLogin.put("platform",platform);
        paramsWeChatLogin.put("headimgurl",headimgurl);
        paramsWeChatLogin.put("openId",openId);
        paramsWeChatLogin.put("gender",gender);
        return post(Urls.WE_CHAT_LOGIN, null, paramsWeChatLogin, true, UserBean.class, "login_user_info_wechat", 0);
    }
}
