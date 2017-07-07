package com.youloft.lilith.login.event;

import com.youloft.lilith.login.bean.LoginUserInfoBean;

/**
 * 手机+密码直接登录成功发送的事件
 *
 * Created by gyh on 2017/7/7.
 */

public class LoginWithPwdEvent {
    public LoginUserInfoBean loginUserInfoBean;
    public LoginWithPwdEvent(LoginUserInfoBean loginUserInfoBean) {
        this.loginUserInfoBean = loginUserInfoBean;
    }
}
