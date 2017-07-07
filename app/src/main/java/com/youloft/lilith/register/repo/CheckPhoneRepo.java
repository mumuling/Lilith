package com.youloft.lilith.register.repo;

import com.youloft.lilith.common.AbstractDataRepo;
import com.youloft.lilith.common.net.Urls;
import com.youloft.lilith.register.bean.CheckPhoneBean;

import java.util.HashMap;

import io.reactivex.Flowable;

/**
 * 检查手机号码描述类
 *
 * Created by gyh on 2017/7/7.
 */

public class CheckPhoneRepo extends AbstractDataRepo{
    static HashMap<String,String> params = new HashMap();

    public static Flowable<CheckPhoneBean> checkPhone(String phone) {
        params.put("phone",phone);
        return unionFlow(Urls.CHECK_PHONE_NUMBER, null, params, true, CheckPhoneBean.class, "check_phone_number", 1);
    }
}
