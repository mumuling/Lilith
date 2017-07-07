package com.youloft.lilith.login.repo;

import com.youloft.lilith.common.AbstractDataRepo;
import com.youloft.lilith.common.net.Urls;
import com.youloft.lilith.login.bean.SmsCodeBean;

import java.util.HashMap;

import io.reactivex.Flowable;

/**
 * 获取验证码辅助类
 *
 * Created by GYH on 2017/7/6.
 */

public class SmsCodeRepo extends AbstractDataRepo{
    static HashMap<String,String> params = new HashMap();

    public static Flowable<SmsCodeBean> getSmsCode(String phone,String smsType,String smsCode) {
        params.put("Phone",phone);
        params.put("SmsType",smsType);
        params.put("code",smsCode);
        return unionFlow(Urls.VERIFICATIONCODE_URL, null, params, true, SmsCodeBean.class, "sms_code", 0);
    }
}
