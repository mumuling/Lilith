package com.youloft.lilith.cons.card;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.cons.bean.ConsPredictsBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zchao on 2017/7/4.
 * desc: 星座顶部，什么逼格一句话卡片
 * version:
 */

public class ConsMyInfoHolder extends ConsBaseHolder {
    @BindView(R.id.cons_my_info_bg)
    ImageView mConsMyInfoBg;
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

    public ConsMyInfoHolder(Context context, ViewGroup parent) {
        super(context, parent, R.layout.cons_my_info);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindData(ConsPredictsBean data) {
        super.bindData(data);
        if (data == null || data.data == null) {
            return;
        }
        ConsPredictsBean.DataBean detailInfo = data.data;

    }
}
