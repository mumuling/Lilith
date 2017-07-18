package com.youloft.lilith.measure.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.measure.bean.MeasureBean;
import com.youloft.statistics.AppAnalytics;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * 大师亲算的holder
 * <p>
 * Created by GYH on 2017/7/4.
 */

public class MasterMeasureHolder extends BaseMeasureHolder {

    @BindViews({R.id.iv_master01, R.id.iv_master02, R.id.iv_master03, R.id.iv_master04,
            R.id.iv_master05, R.id.iv_master06, R.id.iv_master07, R.id.iv_master08})
    ImageView[] ivMasters; //所有的icon

    @BindViews({R.id.tv_master01, R.id.tv_master02, R.id.tv_master03, R.id.tv_master04,
            R.id.tv_master05, R.id.tv_master06, R.id.tv_master07, R.id.tv_master08})
    TextView[] tvMasters; //所有的文字描述

    @BindViews({R.id.ll_master01, R.id.ll_master02, R.id.ll_master03, R.id.ll_master04,
            R.id.ll_master05, R.id.ll_master06, R.id.ll_master07, R.id.ll_master08})
    LinearLayout[] llMasters; //所有的外层容器

    private boolean reportTag = true;

    private List<MeasureBean.DataBean.AdsBean> mData;


    public MasterMeasureHolder(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.item_master_measure, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindData(MeasureBean.DataBean mMeasureData, int position) {
        mData = mMeasureData.ads;
        if (mData == null || mData.size() < 8) {
            return;
        }
        if (reportTag) {
            reportEventIM();
            reportTag = false;
        }

        for (int i = 0; i < 8; i++) {

            GlideApp.with(mContext).load(mData.get(i).image).into(ivMasters[i]);
            tvMasters[i].setText(mData.get(i).title);
            llMasters[i].setTag(i);
        }
    }

    private void reportEventIM() {
        AppAnalytics.onEvent("CC.Place", "IM"+0);
        AppAnalytics.onEvent("CC.Place", "IM"+1);
        AppAnalytics.onEvent("CC.Place", "IM"+2);
        AppAnalytics.onEvent("CC.Place", "IM"+3);
        AppAnalytics.onEvent("CC.Place", "IM"+4);
        AppAnalytics.onEvent("CC.Place", "IM"+5);
        AppAnalytics.onEvent("CC.Place", "IM"+6);
        AppAnalytics.onEvent("CC.Place", "IM"+7);
    }

    @OnClick({R.id.ll_master01, R.id.ll_master02, R.id.ll_master03, R.id.ll_master04,
            R.id.ll_master05, R.id.ll_master06, R.id.ll_master07, R.id.ll_master08})
    public void onViewClicked(View view) {
        if (view.getTag() == null) {
            return;
        }
        int index = (int) view.getTag();
        String url = mData.get(index).url;
        ARouter.getInstance().build("/ui/web")
                .withString("url", url)
                .navigation();
        AppAnalytics.onEvent("CC.Place", "C" + String.valueOf(index));

    }
}

