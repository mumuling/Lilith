package com.youloft.lilith.cons.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.youloft.lilith.R;
import com.youloft.lilith.cons.bean.ConsPredictsBean;
import com.youloft.lilith.cons.consmanager.LoddingCheckEvent;
import com.youloft.lilith.cons.consmanager.ShareConsEvent;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.ui.MainActivity;
import com.youloft.statistics.AppAnalytics;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by zchao on 2017/7/5.
 * desc: 分享按钮
 * version:
 */

public class ConsShareHolder extends ConsBaseHolder {

    private final View mRoot;

    public ConsShareHolder(Context context, ViewGroup parent) {
        super(context, parent, R.layout.cons_share_holder);
        mRoot = itemView.findViewById(R.id.cons_share_root);

        RxView.clicks(mRoot)
                .throttleFirst(800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        UserBean userInfo = AppSetting.userDataIsComplete();
                        if (userInfo == null) {
                            EventBus.getDefault().post(new LoddingCheckEvent());
                            return;
                        }
                        AppAnalytics.onEvent("Homeshare2", "C");
                        EventBus.getDefault().post(new ShareConsEvent("2"));//分享订阅在XZFragment中
                    }
                });
    }

    @Override
    public void bindData(ConsPredictsBean data) {
        super.bindData(data);
    }
    @Override
    public int getShadowBG() {
        return R.drawable.card_shadow_down_pic;
    }
}
