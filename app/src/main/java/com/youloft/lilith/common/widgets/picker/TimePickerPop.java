package com.youloft.lilith.common.widgets.picker;

import android.content.Context;
import android.view.View;

import com.youloft.lilith.R;
import com.youloft.lilith.common.widgets.dialog.PickerBaseDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by zchao on 2017/7/9.
 * desc: 时间选择弹窗，如果没有特殊需求，请直接调用{@link #getDefaultTimePicker(Context)},
 *       然后通过接口来获得选择结果{@link #setOnSelectListener(OnPickerSelectListener)}
 *       最后必须要调用show()才会弹出弹窗;
 * version:
 */

public class TimePickerPop extends PickerBaseDialog implements CanShow, TimePickerView.onTimeChangedListener {

    private final View mCancel;
    private final View mConfirm;
    Context mContext;
    private final TimePickerView mTimePickerView;
    private int hour = 0;
    private int min = 0;
    private GregorianCalendar mCal = new GregorianCalendar();
    private OnPickerSelectListener<GregorianCalendar> listener;

    public TimePickerPop(Context mContext) {
        super(mContext);
        this.mContext = mContext;
        setContentView(R.layout.pop_timepicker);
        mConfirm = findViewById(R.id.tv_confirm);
        mCancel = findViewById(R.id.tv_cancel);
        mTimePickerView = (TimePickerView) findViewById(R.id.time_picker);

        mTimePickerView.setDateChangedListener(this);

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

    public TimePickerPop setDate(Date date) {
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

    private void setUpData() {
        hour = mCal.get(Calendar.HOUR_OF_DAY);
        min = mCal.get(Calendar.MINUTE);
        mTimePickerView.setHourAndMin(hour, min);
    }

    @Override
    public void hide() {
        if (isShow()) {
            super.dismiss();
        }
    }

    @Override
    public boolean isShow() {
        return isShowing();
    }

    @Override
    public void onDateChanged(int hour, int min, int amOrpm) {
        this.hour = hour;
        this.min = min;
        mCal.set(Calendar.HOUR_OF_DAY, hour);
        mCal.set(Calendar.MINUTE, min);
    }
}
