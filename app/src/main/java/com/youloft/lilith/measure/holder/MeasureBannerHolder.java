package com.youloft.lilith.measure.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.utils.SafeUtil;
import com.youloft.lilith.measure.bean.MeasureBean;
import com.youloft.statistics.AppAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 测测的banner  holder
 * <p>
 * Created by GYH on 2017/7/4.
 */

public class MeasureBannerHolder extends BaseMeasureHolder {


    @BindView(R.id.iv_banner)
    ImageView ivBanner;  //banner图
    private MeasureBean.DataBean mMeasureData;//数据源
    private boolean reportTag = true;

    public MeasureBannerHolder(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.item_measure_banner, parent, false));
        ButterKnife.bind(this, itemView);

    }

    @Override
    public void bindData(MeasureBean.DataBean mMeasureData, int position) {
        this.mMeasureData = mMeasureData;
        if (isaBoolean(mMeasureData)) {
            return;
        }
        if (reportTag) {
            AppAnalytics.onEvent("CC.AD", "IM0");
            reportTag = false;
        }
        GlideApp.with(mContext).load(mMeasureData.ads.get(0).image).into(ivBanner);
    }

    private boolean isaBoolean(MeasureBean.DataBean mMeasureData) {
        return mMeasureData == null || mMeasureData.ads == null || mMeasureData.ads.size() == 0;
    }

    @OnClick(R.id.root)
    public void onViewClicked() {
        boolean b = isaBoolean(mMeasureData);
        if (b) return;
        AppAnalytics.onEvent("CC.AD", "C0");
        ARouter.getInstance().build("/ui/web")
                .withString("url", mMeasureData.ads.get(0).url)
                .navigation();
    }
}
