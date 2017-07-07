package com.youloft.lilith.register.event;

import com.youloft.lilith.register.bean.RegisterUserBean;

/**
 * 注册成功的事件
 *
 * Created by gyh on 2017/7/7.
 */

public class RegisterEvent {
    public RegisterUserBean registerUserBean;
    public RegisterEvent(RegisterUserBean registerUserBean) {
        this.registerUserBean = registerUserBean;
    }
}
