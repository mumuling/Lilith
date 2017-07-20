package com.youloft.lilith.login.repo;

import com.youloft.lilith.common.AbstractDataRepo;
import com.youloft.lilith.common.net.Urls;
import com.youloft.lilith.login.bean.SendSmsBean;

import java.util.HashMap;

import io.reactivex.Flowable;

/**
 * 请求发送短信
 *
 * Created by GYH on 2017/7/6.
 */

public class SendSmsRepo extends AbstractDataRepo{
    static HashMap<String,String> params = new HashMap();

    public static Flowable<SendSmsBean> sendSms(String phone,String smsType) {
        params.put("Phone",phone);
        params.put("SmsType",smsType);
        return httpFlow(Urls.SEND_SMS_URL, null, params, true, SendSmsBean.class, null, 0);
    }
}
