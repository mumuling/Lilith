package com.youloft.lilith.measure.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.measure.bean.MeasureBean;
import com.youloft.statistics.AppAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 测测  立即测算holder
 * <p>
 * Created by GYH on 2017/7/4.
 */

public class ImmediatelyMeasureHolder extends BaseMeasureHolder {

    @BindView(R.id.iv_icon)
    ImageView ivIcon; //icon
    @BindView(R.id.tv_title)
    TextView tvTitle; //标题
    @BindView(R.id.tv_desc)
    TextView tvDesc;  //文字描述
    private MeasureBean.DataBean mMeasureData; //数据源
    private int mIndex; //这个是处理后的角标
    private boolean mReportTag = true;//是否已经上报的标识


    public ImmediatelyMeasureHolder(Context context, ViewGroup viewGroup) {
        super(LayoutInflater.from(context).inflate(R.layout.item_immediately, viewGroup, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindData(MeasureBean.DataBean mMeasureData, int position) {
        this.mMeasureData = mMeasureData;
        if (isDataSafe(mMeasureData)) return;
        mIndex = position - 3;
        if(mReportTag){
            reportEvent(mIndex);
        }
        GlideApp.with(mContext).load(mMeasureData.ads.get(0).image).into(ivIcon);
        tvTitle.setText(mMeasureData.ads.get(0).title);
        tvDesc.setText(mMeasureData.ads.get(0).infos);
    }

    /**
     * 判断数据源是否安全
     * @param mMeasureData
     * @return
     */
    private boolean isDataSafe(MeasureBean.DataBean mMeasureData) {
        return mMeasureData == null || mMeasureData.ads == null || mMeasureData.ads.size() == 0;
    }

    /**
     * 展示事件的上报
     * @param mIndex
     */
    private void reportEvent(int mIndex) {
        String eventName = "CC.Card.IM"+mIndex;
        AppAnalytics.onEvent(eventName);
    }

    @OnClick(R.id.bt_measure)
    public void onViewClicked() {
        if (isDataSafe(mMeasureData)) return;
        AppAnalytics.onEvent("CC.Card.C"+mIndex);
        ARouter.getInstance().build("/ui/web")
                .withString("url", mMeasureData.ads.get(0).url)
                .navigation();

    }
}
