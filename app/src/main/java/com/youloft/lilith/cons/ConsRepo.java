package com.youloft.lilith.cons;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.common.AbstractDataRepo;

import java.util.HashMap;

import io.reactivex.Flowable;

/**
 * 星座数据仓库
 * Created by coder on 2017/6/29.
 */
@Route(path = "/repo/cons", name = "星座数据中心")
public class ConsRepo extends AbstractDataRepo {
    public Flowable<HashMap> testData() {
        return unionFlow("http://op.juhe.cn/onebox/weather/query", null, null, false, HashMap.class, "xxo.xxo", 5000);
    }

}
