package com.youloft.lilith.cons.card;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.SafeUtil;
import com.youloft.lilith.cons.bean.ConsPredictsBean;
import com.youloft.lilith.cons.bean.LuckData;
import com.youloft.lilith.cons.view.LuckView;

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

    private static final String formatDate = "yyyy-MM-dd";
    private SimpleDateFormat format = null;
    @BindView(R.id.cons_luck_view)
    LuckView mConsLuckView;
    @BindView(R.id.cons_luck_msg)
    TextView mConsLuckMsg;

    public ConsYSHolder(Context context, ViewGroup parent, int type) {
        super(context, parent, R.layout.cons_ys_holder);
        this.type = type;
        ButterKnife.bind(this, itemView);
        format = new SimpleDateFormat(formatDate);
        mConsLuckView.setType(type);
    }


    @Override
    public void bindData(ConsPredictsBean data) {
        super.bindData(data);
        ConsPredictsBean.DataBean detailData = data.data;
        String msg = "";
        switch (type) {
            case CONS_YS:
                msg = String.valueOf(detailData.msgAvg);
                break;
            case CONS_LOVE:
                msg = String.valueOf(detailData.msglove) ;
                break;
            case CONS_WORK:
                msg = String.valueOf(detailData.msgcareer);
                break;
            case CONS_MONEY:
                msg = String.valueOf(detailData.msgwealth);
                break;
        }
        mConsLuckMsg.setText(msg);
        mConsLuckMsg.getBackground().setLevel(type);

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
