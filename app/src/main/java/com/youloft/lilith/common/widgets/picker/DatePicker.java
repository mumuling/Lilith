package com.youloft.lilith.common.widgets.picker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.youloft.lilith.R;
import com.youloft.lilith.common.widgets.picker.wheel.NumberPicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 日期选择器
 * Created by javen on 15/4/18.
 */
public class DatePicker extends FrameLayout {


    private GregorianCalendar minDate = makeDate(1900, 1, 1);
    private GregorianCalendar maxDate = makeDate(2099, 12, 31);

    private GregorianCalendar mDate = new GregorianCalendar();

    private int nowYear;

    private int nowMonth;

    private int nowDay;

    public void setMaxDate(GregorianCalendar calendar) {
        maxDate.setTimeInMillis(calendar.getTimeInMillis());
//        initPicker();
        updatePickerRange();
    }

    private boolean wYear = false;
    private boolean wMonth = true;
    private boolean wDay = true;

    public void setPickerWheel(boolean year, boolean month, boolean day) {
        this.wYear = year;
        this.wMonth = month;
        this.wDay = day;
        mYear.setWrapSelectorWheel(year);
        mMonth.setWrapSelectorWheel(month);
        mDay.setWrapSelectorWheel(day);
    }

    private NumberPicker mYear;
    private NumberPicker mMonth;
    private NumberPicker mDay;

    private void assignViews() {
        mYear = (NumberPicker) findViewById(R.id.year);
        mMonth = (NumberPicker) findViewById(R.id.month);
        mDay = (NumberPicker) findViewById(R.id.day);

    }

    private onDateChangedListener mListener = null;


    public interface onDateChangedListener {
        void onDateChanged(GregorianCalendar date);
    }
    
    public void setDateChangedListener(onDateChangedListener listener) {
        this.mListener = listener;
    }

    /**
     * 通知日期修改
     */
    protected void notifyDateChanged() {
        if (mListener != null) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(mDate.getTimeInMillis());
            mListener.onDateChanged(gc);
        }

    }

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.picker_date, this);
        GregorianCalendar curr = new GregorianCalendar();
        nowYear = curr.get(Calendar.YEAR);
        nowMonth = curr.get(Calendar.MONTH);
        nowDay = curr.get(Calendar.DAY_OF_MONTH);
        assignViews();
        initPicker();
        updatePickerRange();
    }

    public void setDate(GregorianCalendar calendar) {
        if (calendar != null) {
            mDate.setTimeInMillis(calendar.getTimeInMillis());
        }
        updatePickerRange();
    }

    /**
     * 初始化Picker
     */
    private void initPicker() {
        mYear.setMinValue(minDate.get(Calendar.YEAR));
        mYear.setMaxValue(maxDate.get(Calendar.YEAR));
        mYear.setValue(mDate.get(Calendar.YEAR));
        mYear.setWrapSelectorWheel(wYear);
        mYear.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return value + "年";
            }
        });

        mMonth.setMinValue(minDate.get(Calendar.MONTH));
        mMonth.setMaxValue(12);
        mMonth.setValue(mDate.get(Calendar.MONTH) + 1);
        mMonth.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return value + "月";
            }
        });
        mMonth.setWrapSelectorWheel(wMonth);

        mDay.setMinValue(1);
        mDay.setMaxValue(31);
        mDay.setValue(mDate.get(Calendar.DATE));
        mDay.setWrapSelectorWheel(wDay);
        mDay.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                if (mYear.getValue() == nowYear && mMonth.getValue() == nowMonth && value == nowDay) {
                    return "今天";
                }
                return value + "日";
            }
        });

        mYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mDate.set(Calendar.YEAR, newVal);
                updatePickerRange();
                notifyDateChanged();
            }
        });


        mMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mDate.set(Calendar.MONTH, newVal - 1);
                updatePickerRange();
                notifyDateChanged();
            }
        });

        mDay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mDate.set(Calendar.DATE, newVal);
                notifyDateChanged();
            }
        });


    }

    /**
     * 更新Picker的范围
     */
    private void updatePickerRange() {
        mYear.setValue(mDate.get(Calendar.YEAR));
        mYear.setMinValue(minDate.get(Calendar.YEAR));
        mYear.setMaxValue(maxDate.get(Calendar.YEAR));

        //月更新
        boolean isMinYear = mYear.getValue() == mYear.getMinValue();
        boolean isMaxYear = mYear.getValue() == mYear.getMaxValue();
        mMonth.setMinValue(isMinYear ? minDate.get(Calendar.MONTH) + 1 : mDate.getActualMinimum(Calendar.MONTH) + 1);
        mMonth.setMaxValue(isMaxYear ? maxDate.get(Calendar.MONTH) + 1 : mDate.getActualMaximum(Calendar.MONTH) + 1);
        mMonth.setValue(mDate.get(Calendar.MONTH) + 1);
        //日更新
        boolean isMinMonth = mMonth.getValue() == mMonth.getMinValue();
        boolean isMaxMonth = mMonth.getValue() == mMonth.getMaxValue();
        mDay.setMinValue((isMinYear && isMinMonth) ? minDate.get(Calendar.DATE) : mDate.getActualMinimum(Calendar.DATE));
        mDay.setMaxValue((isMaxYear && isMaxMonth) ? maxDate.get(Calendar.DATE) : mDate.getActualMaximum(Calendar.DATE));
        mDay.setValue(Math.max(Math.min(mDay.getMaxValue(), mDate.get(Calendar.DATE)), mDay.getMinValue()));


    }

    public GregorianCalendar getDate() {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(mDate.getTimeInMillis());
        return gregorianCalendar;
    }


    /**
     * 生成一个Date
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    private GregorianCalendar makeDate(int year, int month, int day) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, day);
        return gregorianCalendar;
    }


}
