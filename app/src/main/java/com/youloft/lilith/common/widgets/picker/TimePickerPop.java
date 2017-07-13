package com.youloft.lilith.common.widgets.picker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.youloft.lilith.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by zchao on 2017/7/9.
 * desc:
 * version:
 */

public class TimePickerPop implements CanShow, TimePicker.onTimeChangedListener {

    private final PopupWindow popwindow;
    private final View popview;
    private final View mCancel;
    private final View mConfirm;
    Context mContext;
    private final TimePicker mTimePicker;
    private int hour = 0;
    private int min = 0;
    private GregorianCalendar mCal = new GregorianCalendar();
    private OnPickerSelectListener<GregorianCalendar> listener;

    public TimePickerPop(Context mContext) {
        this.mContext = mContext;

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        popview = layoutInflater.inflate(R.layout.pop_timepicker, null);
        mConfirm = popview.findViewById(R.id.tv_confirm);
        mCancel = popview.findViewById(R.id.tv_cancel);
        mTimePicker = (TimePicker) popview.findViewById(R.id.time_picker);

        popwindow = new PopupWindow(popview, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        popwindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popwindow.setAnimationStyle(R.style.AnimBottom);
        popwindow.setTouchable(true);
        popwindow.setOutsideTouchable(false);
        popwindow.setFocusable(true);
        popwindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        mTimePicker.setDateChangedListener(this);

        setUpData();

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSelected(mCal);
                }
                hide();
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

    public TimePickerPop setDate(Date date){
        mCal.setTime(date);
        setUpData();
        return this;
    }
    public TimePickerPop setOnSelectListener(OnPickerSelectListener listener) {
        this.listener = listener;
        return this;
    }

    public static TimePickerPop getDefaultTimePicker(Context context) {
        return new TimePickerPop(context);
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
        hour = mCal.get(Calendar.HOUR_OF_DAY);
        min = mCal.get(Calendar.MINUTE);
        mTimePicker.setHourAndMin(hour, min);
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
    public void onDateChanged(int hour, int min, int amOrpm) {
        this.hour = hour;
        this.min = min;
        mCal.set(Calendar.HOUR_OF_DAY, hour);
        mCal.set(Calendar.MINUTE, min);
    }
}
