package com.youloft.lilith.cons.card;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.utils.CalendarHelper;
import com.youloft.lilith.common.utils.SafeUtil;
import com.youloft.lilith.cons.bean.ConsPredictsBean;
import com.youloft.lilith.cons.consmanager.ConsManager;
import com.youloft.lilith.cons.consmanager.LoddingCheckEvent;
import com.youloft.lilith.cons.consmanager.ShareConsEvent;
import com.youloft.lilith.cons.view.ConstellationViewFactory;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.statistics.AppAnalytics;

import org.greenrobot.eventbus.EventBus;

import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by zchao on 2017/7/4.
 * desc: 星座顶部，什么逼格一句话卡片
 * version:
 */

public class ConsMyInfoHolder extends ConsBaseHolder {
    @BindView(R.id.cons_my_info_bg)
    ImageView mConsMyInfoBg;
    @BindView(R.id.cons_my_info_root)
    FrameLayout mRoot;
    @BindView(R.id.cons_my_info_date)
    TextView mConsMyInfoDate;
    @BindView(R.id.cons_my_info_week)
    TextView mConsMyInfoWeek;
    @BindView(R.id.cons_my_info_cons_img)
    ImageView mConsMyInfoConsImg;
    @BindView(R.id.cons_my_info_xz)
    TextView mConsMyInfoXz;
    @BindView(R.id.cons_my_info_date_range)
    TextView mConsMyInfoDateRange;
    @BindView(R.id.cons_my_info_en_word)
    TextView mConsMyInfoEnWord;
    @BindView(R.id.cons_my_info_cn_word)
    TextView mConsMyInfoCnWord;
    @BindView(R.id.cons_my_info_content_root)
    LinearLayout mConsMyInfoContentRoot;
    @BindView(R.id.cons_my_info_en_blur_word)
    ImageView mConsBlurEnWord;
    @BindView(R.id.cons_my_info_share_icon)
    ImageView mConsMyInfoShareIcon;
    private final GregorianCalendar pCalendar;


    /**
     * 检查登录状态
     */
    @OnClick(R.id.cons_my_info_root)
    public void checkLogStatus() {
        AppAnalytics.onEvent("Fortuneslist", "C");
        EventBus.getDefault().post(new LoddingCheckEvent());
    }



    String formatDate = "MM.dd.yyyy";
    private ConsPredictsBean.DataBean detailInfo;

    public ConsMyInfoHolder(Context context, ViewGroup parent) {
        super(context, parent, R.layout.cons_my_info);
        ButterKnife.bind(this, itemView);
        Typeface num = Typeface.createFromAsset(mContext.getAssets(), "fonts/lilisi_number.ttf");
        Typeface en = Typeface.createFromAsset(mContext.getAssets(), "fonts/lilisi_english.ttf");
        Typeface cons = Typeface.createFromAsset(mContext.getAssets(), "fonts/lilisi_constellation_regular.ttf");
        mConsMyInfoDate.setTypeface(num);
        mConsMyInfoDateRange.setTypeface(num);
        mConsMyInfoWeek.setTypeface(en);
        mConsMyInfoEnWord.setTypeface(en);
        mConsMyInfoXz.setTypeface(cons);

        bindDefault();

        pCalendar = new GregorianCalendar();
    }

    @Override
    public void bindData(ConsPredictsBean data) {
        super.bindData(data);
        if (data == null || data.data == null) {
            return;
        }
        detailInfo = data.data;
        bind();
    }

    private void bind() {
        bindDefault();

        ConsPredictsBean.DataBean.PredictsBean todayCons = SafeUtil.getSafeData(detailInfo.predicts, 1);
        Bitmap consImg = null;
        if (todayCons != null) {
            consImg = ConstellationViewFactory.getInstance().getConsImg(detailInfo.signs, todayCons.ptlove, todayCons.ptcareer, todayCons.ptwealth);
        } else {
            consImg = ConstellationViewFactory.getInstance().getConsImg(detailInfo.signs, 5, 5, 5);
        }
        mConsMyInfoConsImg.setImageBitmap(consImg);

        ConsManager.ConsInfo consSrc = ConsManager.getConsSrc(String.valueOf(detailInfo.signs));
        if (consSrc != null) {
            mConsMyInfoXz.setText(consSrc.pKey);
            mConsMyInfoDateRange.setText(consSrc.pRang);
        }
        mConsBlurEnWord.setImageResource(ConsManager.getConsBlurWord(detailInfo.signs));
        mConsMyInfoEnWord.setText(detailInfo.eMsg);
        mConsMyInfoCnWord.setText(detailInfo.msg);
        GlideApp.with(mContext).load(detailInfo.bgImg).into(mConsMyInfoBg);
    }

    private void bindDefault() {
        GregorianCalendar date = new GregorianCalendar();
        mConsMyInfoDate.setText(CalendarHelper.format(date.getTime(), formatDate));
        mConsMyInfoWeek.setText(CalendarHelper.getWeekInEN(date));

        RxView.clicks(mConsMyInfoShareIcon)
                .throttleFirst(800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        UserBean userInfo = AppSetting.userDataIsComplete();
                        if (userInfo == null) {
                            EventBus.getDefault().post(new LoddingCheckEvent());
                            return;
                        }
                        AppAnalytics.onEvent("Homeshare1", "C");
                        EventBus.getDefault().post(new ShareConsEvent("1"));
                    }
                });
    }

}
