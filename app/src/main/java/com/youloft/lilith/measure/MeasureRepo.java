package com.youloft.lilith.measure;

import com.youloft.lilith.common.AbstractDataRepo;
import com.youloft.lilith.common.net.Urls;
import com.youloft.lilith.measure.bean.MeasureBean;

import io.reactivex.Flowable;

/**
 * 测测请求辅助类
 *
 * Created by GYH on 2017/7/5.
 */
public class MeasureRepo extends AbstractDataRepo{

    public static Flowable<MeasureBean> getMeasureData() {

        return unionFlow(Urls.MEASURE_URL, null, null, true, MeasureBean.class, "measure_data", 1000*60*2);
    }

}
