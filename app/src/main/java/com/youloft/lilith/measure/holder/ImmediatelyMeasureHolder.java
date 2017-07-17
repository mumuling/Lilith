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
    ImageView ivIcon;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    private MeasureBean.DataBean mMeasureData;
    private int mIndex;
    public ImmediatelyMeasureHolder(Context context, ViewGroup viewGroup) {
        super(LayoutInflater.from(context).inflate(R.layout.item_immediately, viewGroup, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindData(MeasureBean.DataBean mMeasureData, int position) {
        this.mMeasureData = mMeasureData;
        if(mMeasureData == null || mMeasureData.ads == null || mMeasureData.ads.size() == 0){
            return;
        }

        mIndex = position - 3;
        GlideApp.with(mContext).load(mMeasureData.ads.get(mIndex).image).into(ivIcon);
        tvTitle.setText(mMeasureData.ads.get(mIndex).title);
        tvDesc.setText(mMeasureData.ads.get(mIndex).infos);
    }

    @OnClick(R.id.bt_measure)
    public void onViewClicked() {

        ARouter.getInstance().build("/ui/web")
                .withString("url", mMeasureData.ads.get(mIndex).url)
                .navigation();

    }
}
