package com.youloft.lilith.cons.card;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.utils.CalendarHelper;
import com.youloft.lilith.common.utils.SafeUtil;
import com.youloft.lilith.cons.bean.ConsPredictsBean;
import com.youloft.lilith.cons.consmanager.ConsManager;
import com.youloft.lilith.cons.view.ConstellationViewFactory;

import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.cons_my_info_share_icon)
    ImageView mConsMyInfoShareIcon;
    private final GregorianCalendar pCalendar;

    private int a = 0;
    @OnClick(R.id.cons_my_info_cons_img)
    public void change() {
        if (detailInfo != null) {
            a++;
            a %= 12;
            detailInfo.signs = a + 1;
            bind();
        }
    }

    String formatDate = "dd.MM.yyyy";
    private ConsPredictsBean.DataBean detailInfo;

    public ConsMyInfoHolder(Context context, ViewGroup parent) {
        super(context, parent, R.layout.cons_my_info);
        ButterKnife.bind(this, itemView);
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
        GregorianCalendar date = new GregorianCalendar();
        mConsMyInfoDate.setText(CalendarHelper.format(date, formatDate));
        mConsMyInfoWeek.setText(CalendarHelper.getWeekInEN(date));

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

        mConsMyInfoEnWord.setText(detailInfo.eMsg);
        mConsMyInfoCnWord.setText(detailInfo.msg);

        GlideApp.with(mContext).load(detailInfo.bgImg).into(mConsMyInfoBg);
        mConsMyInfoContentRoot.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams layoutParams = mRoot.getLayoutParams();
                layoutParams.height = mConsMyInfoContentRoot.getHeight();
                mRoot.setLayoutParams(layoutParams);

            }
        });
    }
}