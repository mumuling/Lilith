package com.youloft.lilith.login.repo;

import com.youloft.lilith.common.AbstractDataRepo;
import com.youloft.lilith.common.net.Urls;
import com.youloft.lilith.login.bean.ModifyPasswordBean;

import java.util.HashMap;

import io.reactivex.Flowable;

/**
 * 短信修改密码请求描述类
 *
 * Created by gyh on 2017/7/7.
 */

public class ModifyPasswordRepo extends AbstractDataRepo{
    static HashMap<String,String> params = new HashMap();

    public static Flowable<ModifyPasswordBean> modifyPassword(String phone, String code,String password) {
        params.put("phone",phone);
        params.put("code",code);
        params.put("newpwd",password);
        return unionFlow(Urls.MODIFY_PASSWORD, null, params, true, ModifyPasswordBean.class, "modify_password", 0);
    }
}
