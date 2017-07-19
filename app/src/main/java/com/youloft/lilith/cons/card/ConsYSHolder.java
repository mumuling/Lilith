package com.youloft.lilith.cons.card;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.SafeUtil;
import com.youloft.lilith.cons.bean.ConsPredictsBean;
import com.youloft.lilith.cons.bean.LuckData;
import com.youloft.lilith.cons.consmanager.LoddingCheckEvent;
import com.youloft.lilith.cons.view.LuckView;
import com.youloft.statistics.AppAnalytics;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zchao on 2017/7/5.
 * desc:
 * version:
 */

public class ConsYSHolder extends ConsBaseHolder {
    private int type = CONS_YS;           //区别运势展示类型
    public static final int CONS_YS = 1;
    public static final int CONS_LOVE = 2;
    public static final int CONS_WORK = 3;
    public static final int CONS_MONEY = 4;
    private String mReportKey = "Summaryfortunes"; //上报key
    private boolean isReport = false;              //是否已上报

    private static final String formatDate = "yyyy-MM-dd";
    private SimpleDateFormat format = null;
    @BindView(R.id.cons_luck_view)
    LuckView mConsLuckView;
    @BindView(R.id.cons_luck_msg)
    TextView mConsLuckMsg;
    @BindView(R.id.cons_luck_msg_no_data)
    ImageView mConsNoData;
    @BindView(R.id.cons_luck_bg_group)
    FrameLayout mConsTextGroup;
    @BindView(R.id.root)
    LinearLayout mRoot;


    public ConsYSHolder(Context context, ViewGroup parent, int type) {
        super(context, parent, R.layout.cons_ys_holder);
        this.type = type;
        ButterKnife.bind(this, itemView);
        format = new SimpleDateFormat(formatDate);
        mConsLuckView.setType(type);
        initReportKey();
    }

    private void initReportKey() {
        if (type == CONS_YS) {
            mReportKey = "Summaryfortunes";
        } else if (type == CONS_LOVE) {
            mReportKey = "Feelingfortunes";
        } else if (type == CONS_WORK) {
            mReportKey = "Jobfortunes";
        } else if (type == CONS_MONEY) {
            mReportKey = "Wealthfortunes";
        }
    }

    @OnClick(R.id.root)
    public void checkLodding() {
        AppAnalytics.onEvent(mReportKey, "C");
        EventBus.getDefault().post(new LoddingCheckEvent());
    }

    @Override
    public int getShadowBG() {
        return R.drawable.card_shadow_center_pic;
    }

    @Override
    public void bindData(ConsPredictsBean data) {
        super.bindData(data);
        if (!isReport) {
            AppAnalytics.onEvent(mReportKey, "IM");
            isReport = true;
        }
        if (data == null || data.data == null) {
            return;
        }
        ConsPredictsBean.DataBean detailData = data.data;
        String msg = "";
        switch (type) {
            case CONS_YS:
                msg = detailData.msgAvg;
                break;
            case CONS_LOVE:
                msg = detailData.msglove;
                break;
            case CONS_WORK:
                msg = detailData.msgcareer;
                break;
            case CONS_MONEY:
                msg = detailData.msgwealth;
                break;
        }
        mConsLuckMsg.setText(msg);
        mConsNoData.setVisibility(TextUtils.isEmpty(msg) ? View.VISIBLE : View.GONE);
        mConsTextGroup.getBackground().setLevel(type);

        if (detailData.predicts != null) {
            LuckData luckData = new LuckData();
            luckData.type = type;
            luckData.data = new ArrayList<>();
            for (int i = 0; i < Math.min(9, detailData.predicts.size()); i++) {
                ConsPredictsBean.DataBean.PredictsBean safeData = SafeUtil.getSafeData(detailData.predicts, i);
                if (safeData == null) {
                    continue;
                }
                LuckData.LuckItem luckItem = new LuckData.LuckItem();
                try {
                    Date parse = format.parse(safeData.date);
                    GregorianCalendar cal = new GregorianCalendar();
                    cal.setTimeInMillis(parse.getTime());
                    luckItem.day = cal;

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                switch (type) {
                    case CONS_YS:
                        luckItem.luckLevel = safeData.avg;
                        break;
                    case CONS_LOVE:
                        luckItem.luckLevel = safeData.ptlove;
                        break;
                    case CONS_WORK:
                        luckItem.luckLevel = safeData.ptcareer;
                        break;
                    case CONS_MONEY:
                        luckItem.luckLevel = safeData.ptwealth;
                        break;
                }
                luckData.data.add(luckItem);
            }
            mConsLuckView.setDate(luckData);
        }
    }
}
