package com.youloft.lilith.login.event;

/**
 * 登录成功后的EventBus时间
 *
 * Created by GYH on 2017/7/6.
 */

public class LoginEvent {
    public boolean isLogin; //true代表登录,false代表登出
    public LoginEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }
}
