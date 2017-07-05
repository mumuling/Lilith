package com.youloft.lilith.measure.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.measure.bean.MeasureBean;

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
    ImageView ivBanner;

    public MeasureBannerHolder(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.item_measure_banner, parent, false));
        ButterKnife.bind(this, itemView);

    }

    @Override
    public void bindData(MeasureBean.DataBean mMeasureData, int position) {
        GlideApp.with(mContext).load(mMeasureData.ads.get(0).image).into(ivBanner);
    }

    @OnClick(R.id.root)
    public void onViewClicked() {

    }
}
