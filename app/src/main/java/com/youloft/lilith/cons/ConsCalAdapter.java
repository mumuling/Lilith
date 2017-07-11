package com.youloft.lilith.cons;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.CalendarHelper;
import com.youloft.lilith.common.utils.SafeUtil;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.cons.bean.ConsPredictsBean;
import com.youloft.lilith.cons.view.ConsCalendar;
import com.youloft.lilith.cons.view.StackViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by zchao on 2017/7/3.
 * desc:   星座日历日卡样式
 * version:
 */

public class ConsCalAdapter extends RecyclerView.Adapter<ConsCalAdapter.ConsCalItemHolder> {
    private Context mContext;
    private int mCalType = ConsCalendar.CONS_CAL_TYPE_MONTH;
    public static final String dateFormatString = "yyyy-MM-dd";
    List<ConsPredictsBean.DataBean.PredictsBean> mPredicts = new ArrayList<>();
    GregorianCalendar mIndexCal = new GregorianCalendar();
    private int mIconSize = (int) ViewUtil.dp2px(20);
    public ConsCalAdapter(Context context) {
        mContext = context;
    }

    public void setCalType(int type) {
        if (mCalType == type) {
            return;
        }
        mCalType = type;
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param bean
     */
    public void setConsData(ConsPredictsBean.DataBean bean) {
        if (bean == null || bean.predicts == null || bean.predicts.isEmpty()) {
            return;
        }
        mPredicts.clear();
        mPredicts.addAll(bean.predicts.subList(1, bean.predicts.size()));
        notifyDataSetChanged();
    }

    @Override
    public ConsCalItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cons_cal_item, parent, false);
        return new ConsCalItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ConsCalItemHolder holder, int position) {
        ConsPredictsBean.DataBean.PredictsBean safeData = SafeUtil.getSafeData(mPredicts, position);
        if (safeData == null) {
            bindNoData(holder, position);
            return;
        }
        bindDayView(holder, safeData);
    }

    /**
     * 无数据时候绑定样式
     */
    private void bindNoData(ConsCalItemHolder holder, int position) {
        mIndexCal.setTimeInMillis(System.currentTimeMillis());
        mIndexCal.add(Calendar.DAY_OF_MONTH, position);
        long intervalDays = CalendarHelper.getIntervalDays(new GregorianCalendar(), mIndexCal);
        String word = "";
        if (intervalDays < 0) {
            word = "昨天";
        } else if (intervalDays == 0) {
            word = "今天";
        } else if (intervalDays == 1) {
            word = "明天";
        } else if (intervalDays == 2) {
            word = "后天";
        } else {
            word = String.valueOf(mIndexCal.get(Calendar.DAY_OF_MONTH));
        }
        holder.mDayText.setText(word);
        holder.mStack.removeAllViews();
        if (holder.mStack.getChildCount() == 0) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.drawable.cons_cal_no_img_bg);
            holder.mStack.addView(imageView);
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.height = layoutParams.width = mIconSize;
            imageView.setLayoutParams(layoutParams);
        }
    }

    private void bindDayView(ConsCalItemHolder holder, ConsPredictsBean.DataBean.PredictsBean predicts) {
        Date date = CalendarHelper.parseDate(predicts.date, dateFormatString);
        mIndexCal.setTimeInMillis(date.getTime());
        long intervalDays = CalendarHelper.getIntervalDays(new GregorianCalendar(), mIndexCal);
        String word = "";
        if (intervalDays < 0) {
            word = "昨天";
        } else if (intervalDays == 0) {
            word = "今天";
        } else if (intervalDays == 1) {
            word = "明天";
        } else if (intervalDays == 2) {
            word = "后天";
        } else {
            word = String.valueOf(mIndexCal.get(Calendar.DAY_OF_MONTH));
        }
        holder.mDayText.setText(word);
        holder.mStack.removeAllViews();
        if (predicts.ptcareer >= 8) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.drawable.calendar_work_icon);
            holder.mStack.addView(imageView);
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.height = layoutParams.width = mIconSize;
            imageView.setLayoutParams(layoutParams);
        }
        if (predicts.ptwealth >= 8) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.drawable.calendar_money_icon);
            holder.mStack.addView(imageView);
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.height = layoutParams.width = mIconSize;
            imageView.setLayoutParams(layoutParams);
        }
        if (predicts.ptlove >= 8) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.drawable.calendar_love_icon);
            holder.mStack.addView(imageView);
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.height = layoutParams.width = mIconSize;
            imageView.setLayoutParams(layoutParams);
        }
        if (holder.mStack.getChildCount() == 0) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.drawable.cons_cal_no_img_bg);
            holder.mStack.addView(imageView);
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.height = layoutParams.width = mIconSize;
            imageView.setLayoutParams(layoutParams);
        }
    }

    @Override
    public int getItemCount() {
        return mCalType == ConsCalendar.CONS_CAL_TYPE_WEEK ? 7 : 28;
    }


    class ConsCalItemHolder extends RecyclerView.ViewHolder {

        private StackViewGroup mStack;
        private TextView mDayText;

        public ConsCalItemHolder(View itemView) {
            super(itemView);
            mStack = (StackViewGroup) itemView.findViewById(R.id.cons_cal_day_stack);
            mDayText = (TextView) itemView.findViewById(R.id.cons_cal_day_text);
        }
    }

}
