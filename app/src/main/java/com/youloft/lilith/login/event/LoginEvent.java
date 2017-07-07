package com.youloft.lilith.login.event;

import com.youloft.lilith.login.bean.UserBean;

/**
 * 登录成功后的EventBus时间
 *
 * Created by GYH on 2017/7/6.
 */

public class LoginEvent {
    public UserBean mUserBean;
    public LoginEvent(UserBean userBean) {
        mUserBean = userBean;
    }
}
