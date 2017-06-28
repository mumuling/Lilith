package com.youloft.lilith.common.widgets.picker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.youloft.lilith.R;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * 时间选择器
 * <p/>
 * Created by javen on 15/4/18.
 */
public class TimePicker extends FrameLayout {

    protected GregorianCalendar mDate = new GregorianCalendar();

    protected NumberPicker mHour;
    protected NumberPicker mMin;

    public static int HOUR_24 = 1; //使用24小时制
    public static int HOUR_12 = 2;  //使用12小时制

    public int mHourDay = HOUR_24; //当前使用的小时制，默认为24小时制

    public GregorianCalendar getCurrentDate() {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(mDate.getTimeInMillis());
        return gregorianCalendar;
    }


    public interface onTimeChangedListener {
        void onDateChanged(int hour, int min, int amOrpm);

    }

    private onTimeChangedListener mListener = null;

    public void setDateChangedListener(onTimeChangedListener listener) {
        this.mListener = listener;
    }

    /**
     * 通知日期修改
     */
    protected void notifyDateChanged() {
        if (mListener != null) {
            if (mHourDay == HOUR_24) {
                mListener.onDateChanged(mDate.get(Calendar.HOUR_OF_DAY), mDate.get(Calendar.MINUTE), mDate.get(Calendar.AM_PM));
            } else {
                mListener.onDateChanged(mDate.get(Calendar.HOUR), mDate.get(Calendar.MINUTE), mDate.get(Calendar.AM_PM));
            }
        }

    }


    public void setPickerWheel(boolean hour, boolean min) {
        mHour.setWrapSelectorWheel(hour);
        mMin.setWrapSelectorWheel(min);
    }

    private void assignViews() {
        mHour = (NumberPicker) findViewById(R.id.hour);
        mMin = (NumberPicker) findViewById(R.id.min);
    }


    public TimePicker(Context context) {
        this(context, null);
    }

    public TimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.picker_time, this);
        assignViews();
        initPicker();
    }

    /**
     * 设置小时显示模式，24小时制传入{@link #HOUR_24};12小时制传入{@link #HOUR_12}
     * 有点儿问题，还是先不用了，暂时只支持24小时制
     * @param hourFormat
     */
    private void setTimeRange(int hourFormat) {
        if (hourFormat == mHourDay) {
            return;
        }
        mHourDay = hourFormat;
        mHour.setMaxValue(mHourDay == HOUR_24 ? 23 : 12);
        updatePickerRange();
    }

    /**
     * 通过一个date来设置时间
     *
     * @param date
     */
    public void setDate(GregorianCalendar date) {
        this.mDate.setTimeInMillis(date.getTimeInMillis());
        updatePickerRange();
    }

    /**
     * 设置当前显示的小时分钟数据
     *
     * @param hour
     * @param min
     */
    public void setHourAndMin(int hour, int min) {
        if (hour > 23 || hour < 0) {
            throw new IllegalArgumentException("时间范围有需要为0~23");
        }
        if (mHourDay == HOUR_12 && hour > 12) {
            throw new IllegalArgumentException("时间范围有需要为0~12");
        }
        if (mHourDay == HOUR_24) {
            mDate.set(Calendar.HOUR_OF_DAY, hour);
        } else {
            mDate.set(Calendar.HOUR, hour);
        }
        mDate.set(Calendar.MINUTE, min);
        updatePickerRange();
    }


    /**
     * 初始化Picker
     */
    protected void initPicker() {
        initHourPicker();
        initMinPicker();
        updatePickerRange();

        mHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                onHourValueChanged(picker, oldVal, newVal);
            }
        });
        mMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                onMinValueChanged(picker, oldVal, newVal);
            }
        });

    }

    protected void onMinValueChanged(NumberPicker picker, int oldVal, int newVal) {
        mDate.set(Calendar.MINUTE, newVal);
        notifyDateChanged();
    }

    protected void onHourValueChanged(NumberPicker picker, int oldVal, int newVal) {
        mDate.set(Calendar.HOUR_OF_DAY, newVal);
        notifyDateChanged();
    }


    protected void initMinPicker() {
        mMin.setMinValue(0);
        mMin.setMaxValue(59);
        mMin.setWrapSelectorWheel(true);
        mMin.setValue(mDate.get(Calendar.MINUTE));
        mMin.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return value + "分";
            }
        });
    }

    protected void initHourPicker() {
        if (mHourDay == HOUR_24) {
            mHour.setMinValue(0);
            mHour.setMaxValue(23);
            mHour.setValue(mDate.get(Calendar.HOUR_OF_DAY));
            mHour.setWrapSelectorWheel(true);
            mHour.setFormatter(new NumberPicker.Formatter() {
                @Override
                public String format(int value) {
                    return value + "时";
                }
            });
        } else {
            mHour.setMinValue(0);
            mHour.setMaxValue(12);
            mHour.setValue(mDate.get(Calendar.HOUR));
            mHour.setWrapSelectorWheel(true);
            mHour.setFormatter(new NumberPicker.Formatter() {
                @Override
                public String format(int value) {
                    return value + "时";
                }
            });
        }
    }


    /**
     * 更新Picker的范围
     */
    protected void updatePickerRange() {
        if (mHourDay == HOUR_24) {
            mHour.setValue(mDate.get(Calendar.HOUR_OF_DAY));
        } else {
            mHour.setValue(mDate.get(Calendar.HOUR));
        }
        mMin.setValue(mDate.get(Calendar.MINUTE));

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
        GregorianCalendar _cal = new GregorianCalendar(year, month, day);
        return _cal;
    }

}
