package com.youloft.lilith.info.repo;

import com.youloft.lilith.common.AbstractDataRepo;
import com.youloft.lilith.common.net.Urls;
import com.youloft.lilith.info.bean.OldPasswordBean;
import com.youloft.lilith.login.bean.ModifyPasswordBean;

import java.util.HashMap;

import io.reactivex.Flowable;

/**
 * Created by Administrator on 2017/7/7.
 */

public class UpdatePasswordRepo extends AbstractDataRepo{
    static HashMap<String,String> paramsOldPassword = new HashMap();

    public static Flowable<OldPasswordBean> checkOldPassword(String uid, String pwd) {
        paramsOldPassword.put("uid",uid);
        paramsOldPassword.put("pwd",pwd);
        return unionFlow(Urls.CHECK_OLD_PASSWORD, null, paramsOldPassword, true, OldPasswordBean.class, "old_password", 0);
    }
    static HashMap<String,String> paramsUpdatePassword = new HashMap();
    public static Flowable<ModifyPasswordBean> updatePassword(String uid, String oldpwd,String newpwd) {
        paramsUpdatePassword.put("uid",uid);
        paramsUpdatePassword.put("oldpwd",oldpwd);
        paramsUpdatePassword.put("newpwd",newpwd);
        return unionFlow(Urls.MODIFY_PASSWORD, null, paramsUpdatePassword, true, ModifyPasswordBean.class, "update_password", 0);
    }
}
