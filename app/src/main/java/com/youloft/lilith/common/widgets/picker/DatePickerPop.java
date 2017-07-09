package com.youloft.lilith.common.widgets.picker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.youloft.lilith.R;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by zchao on 2017/7/9.
 * desc:
 * version:
 */

public class DatePickerPop implements CanShow ,DatePicker.onDateChangedListener{

    private final PopupWindow popwindow;
    private final View popview;
    private final View mCancel;
    private final View mConfirm;
    Context mContext;
    private final DatePicker mDatePicker;
    private GregorianCalendar date = new GregorianCalendar();

    private OnPickerSelectListener<Date> listener;
    public DatePickerPop(Context mContext) {
        this.mContext = mContext;

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        popview = layoutInflater.inflate(R.layout.pop_datepicker, null);
        mConfirm = popview.findViewById(R.id.tv_confirm);
        mCancel = popview.findViewById(R.id.tv_cancel);
        mDatePicker = (DatePicker) popview.findViewById(R.id.date_picker);

        popwindow = new PopupWindow(popview, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        popwindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popwindow.setAnimationStyle(R.style.AnimBottom);
        popwindow.setTouchable(true);
        popwindow.setOutsideTouchable(false);
        popwindow.setFocusable(true);

        mDatePicker.setDateChangedListener(this);
        setUpData();

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSelected(date.getTime());
                }
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCancel();
                    hide();
                }
            }
        });
    }

    public DatePickerPop setOnSelectListener(OnPickerSelectListener listener){
        this.listener = listener;
        return this;
    }

    public static DatePickerPop getDefaultDatePicker(Context context){
        return new DatePickerPop(context);
    }


    @Override
    public void setType(int var1) {

    }

    @Override
    public void show() {
        if (!isShow()) {
//            setUpData();
            popwindow.showAtLocation(popview, Gravity.BOTTOM, 0, 0);
        }
    }

    private void setUpData() {
        mDatePicker.setDate(date);
    }

    @Override
    public void hide() {
        if (isShow()) {
            popwindow.dismiss();
        }
    }

    @Override
    public boolean isShow() {
        return popwindow.isShowing();
    }


    @Override
    public void onDateChanged(GregorianCalendar date) {
        this.date.setTime(date.getTime());
    }
}
