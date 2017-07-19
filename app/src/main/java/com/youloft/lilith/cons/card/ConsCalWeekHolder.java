package com.youloft.lilith.cons.card;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.ViewGroup;

import com.youloft.lilith.R;
import com.youloft.lilith.cons.ConsCalDetailActivity;
import com.youloft.lilith.cons.bean.ConsPredictsBean;
import com.youloft.lilith.cons.consmanager.LoddingCheckEvent;
import com.youloft.lilith.cons.view.ConsCalendar;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.ui.MainActivity;
import com.youloft.statistics.AppAnalytics;

import org.greenrobot.eventbus.EventBus;

import jp.wasabeef.blurry.internal.Blur;
import jp.wasabeef.blurry.internal.BlurFactor;

/**
 * Created by zchao on 2017/7/5.
 * desc:
 * version:
 */

public class ConsCalWeekHolder extends ConsBaseHolder implements ConsCalendar.OnClickListener {


    private final ConsCalendar mWeekView;
    private ConsPredictsBean mData;

    public ConsCalWeekHolder(Context context, ViewGroup parent) {
        super(context, parent, R.layout.cons_week_holder);
        mWeekView = (ConsCalendar) itemView.findViewById(R.id.cons_week_view);
        mWeekView.setCalType(ConsCalendar.CONS_CAL_TYPE_WEEK);
        mWeekView.setListener(this);
    }

    @Override
    public void bindData(ConsPredictsBean data) {
        super.bindData(data);
        mData = data;
        mWeekView.setData(data);
    }
    @Override
    public int getShadowBG() {
        return R.drawable.card_shadow_center_pic;
    }

    @Override
    public void onClick() {
        AppAnalytics.onEvent("Weekfortunes", "C");
        UserBean userInfo = AppSetting.userDataIsComplete();
        if (userInfo == null) {
            EventBus.getDefault().post(new LoddingCheckEvent());
            return;
        }

        int[] local = new int[2];
        mWeekView.getLocationOnScreen(local);
        if (mContext instanceof MainActivity) {

            Bitmap screenShort = ((MainActivity) mContext).takeScreenShot(false, 0, mContext.getResources().getColor(R.color.black_30));

            BlurFactor bf = new BlurFactor();
            bf.width = screenShort.getWidth();
            bf.height = screenShort.getHeight();
            bf.sampling = 10;
            bf.radius = 10;
            screenShort = Blur.of(mContext, screenShort, bf);
            ConsCalDetailActivity.startConsCalDetailActivity(mContext, local, screenShort, mData);
        } else {
            ConsCalDetailActivity.startConsCalDetailActivity(mContext, local, null, mData);
        }
    }
}
