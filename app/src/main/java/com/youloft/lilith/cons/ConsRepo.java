package com.youloft.lilith.cons;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.LLApplication;
import com.youloft.lilith.common.AbstractDataRepo;
import com.youloft.lilith.common.utils.LocationUtil;
import com.youloft.lilith.common.utils.Utils;
import com.youloft.lilith.cons.bean.ConsPredictsBean;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Flowable;

/**
 * 星座数据仓库
 * Created by coder on 2017/6/29.
 */
@Route(path = "/repo/cons", name = "星座数据中心")
public class ConsRepo extends AbstractDataRepo {
    static HashMap<String, String> param = new HashMap();

    /**
     * @param birdt    出生日期格式: yyyy-MM-dd
     * @param birtm    出生时间格式: HH:mm:ss, 缺省: 12:00:00
     * @param birlongi 出生经度	选填, 缺省: 116.07 (北京)
     * @param birlati  出生纬度	选填, 缺省: 34.3 (北京)
     *                 curlongi 当时经度	选填, 缺省: 出生经度
     *                 curlati  当时经度	选填, 缺省: 出生经度
     * @return
     */
    public static Flowable<ConsPredictsBean> getConsPredicts(String birdt, String birtm, String birlongi, String birlati) {
        param.clear();
        String[] location = LocationUtil.getLocation();
        param.put("days", "28");
        param.put("birdt", birdt);
        param.put("birtm", birtm);
        param.put("birlongi", birlongi);
        param.put("birlati", birlati);
        if (location != null) {
            param.put("curlongi", location[0]);
            param.put("curlati", location[1]);
        }
        return unionFlow("http://*****" + "/GetPredicts", null, param, true, ConsPredictsBean.class, "cons_predicts", 10 * 60 * 1000);
    }


}
