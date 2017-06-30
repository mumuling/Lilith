package com.youloft.lilith.info;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.common.AbstractDataRepo;
import com.youloft.lilith.common.net.ParamsMap;

import java.util.HashMap;

import io.reactivex.Flowable;

/**
 * 描述类作用
 * <p>
 * 作者 coder
 * 创建时间 2017/6/30
 */
@Route(path = "/repo/user", name = "用户数据中心")
public class UserRepo extends AbstractDataRepo {

    /**
     * 电话登录
     *
     * @param phone
     */
    public Flowable<HashMap> loginWithPhone(String phone) {
        return httpFlow(
                "http://login_Url",
                null,
                ParamsMap.create()
                        .withParams("phone", phone)
                        .withParams("ago", null),
                true,
                HashMap.class,
                null,
                0);
    }

}
