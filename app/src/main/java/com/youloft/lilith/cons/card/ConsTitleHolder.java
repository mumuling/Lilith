package com.youloft.lilith.cons.card;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.cons.consmanager.LoddingCheckEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zchao on 2017/7/4.
 * desc: 星座页面顶部holder
 * version:
 */

public class ConsTitleHolder extends BaseHolder<String> {

    private static String unLoggin = "%s好, 请您完善资料获取准确运势!";
    private static String loggin = "%s好 %s, 您的今日运势如下";
    @BindView(R.id.cons_title_text)
    TextView mConsTitleText;
    @BindView(R.id.cons_title_root)
    ViewGroup mConsTitleRoot;
    private ForegroundColorSpan highLight;

    public ConsTitleHolder(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.cons_card_title, parent, false), context);
        ButterKnife.bind(this, itemView);
        setDefaultString();
        unLoggin = mContext.getResources().getString(R.string.cons_title_word_unlod);
        loggin = mContext.getResources().getString(R.string.cons_title_word_lod);
    }

    /**
     *
     */
    @OnClick(R.id.cons_title_root)
    public void getLogginState(){
        //TODO 这儿需要去判断登录状态，如果没登录则直接跳转到登录界面
        EventBus.getDefault().post(new LoddingCheckEvent());
    }

    /**
     * 没登录显示
     */
    private void setDefaultString() {
        SpannableString ss = new SpannableString(String.format(unLoggin, getStringInDay()));
        highLight = new ForegroundColorSpan(mContext.getResources().getColor(R.color.cons_title_high_light));

        ss.setSpan(highLight, (ss.length() - 11), (ss.length() - 7), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mConsTitleText.setText(ss);
    }

    @Override
    public void bindData(String data) {
        if (TextUtils.isEmpty(data)) {
            setDefaultString();
            return;
        }
        if (data.length() > 7) {
            data = data.substring(0,6) + "...";
        }
        SpannableString ss = new SpannableString(String.format(loggin, getStringInDay(), data));
        ss.setSpan(highLight, 4, ss.length() - 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mConsTitleText.setText(ss);
    }



    /**s
     * 获取当前时间是在上午 下午还是晚上
     * @return
     */
    private String getStringInDay() {
        int i = GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (i >= 5 && i < 13) {
            return "上午";
        } else if (i >= 13 && i < 20) {
            return "下午";
        } else {
            return "晚上";
        }
    }
}
