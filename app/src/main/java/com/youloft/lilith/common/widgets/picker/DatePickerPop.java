package com.youloft.lilith.common.widgets.picker;

import android.content.Context;
import android.view.View;

import com.youloft.lilith.R;
import com.youloft.lilith.common.widgets.dialog.PickerBaseDialog;

import java.util.GregorianCalendar;

/**
 * Created by zchao on 2017/7/9.
 * desc:日期选择弹窗，如果没有特殊需求，请直接调用{@link #getDefaultDatePicker(Context)} (Context)},
 *       然后通过接口来获得选择结果{@link #setOnSelectListener(OnPickerSelectListener)}
 *       最后必须要调用show()才会弹出弹窗;
 * version:
 */

public class DatePickerPop extends PickerBaseDialog implements CanShow , DatePickerView.onDateChangedListener{

    private final View mCancel;
    private final View mConfirm;
    Context mContext;
    private final DatePickerView mDatePickerView;
    private GregorianCalendar date = new GregorianCalendar();

    private OnPickerSelectListener<GregorianCalendar> listener;
    public DatePickerPop(Context mContext) {
        super(mContext);
        setContentView(R.layout.pop_datepicker);
        this.mContext = mContext;
        mConfirm = findViewById(R.id.tv_confirm);
        mCancel = findViewById(R.id.tv_cancel);
        mDatePickerView = (DatePickerView) findViewById(R.id.date_picker);

        mDatePickerView.setDateChangedListener(this);
        setUpData();

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSelected(date);
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

    public DatePickerPop setOnSelectListener(OnPickerSelectListener listener){
        this.listener = listener;
        return this;
    }

    public DatePickerPop setDate(GregorianCalendar cal){
        date.setTime(cal.getTime());
        setUpData();
        return this;
    }

    public static DatePickerPop getDefaultDatePicker(Context context){
        return new DatePickerPop(context);
    }


    @Override
    public void setType(int var1) {

    }


    private void setUpData() {
        mDatePickerView.setDate(date);
    }

    @Override
    public void hide() {
        if (isShow()) {
            dismiss();
        }
    }

    @Override
    public boolean isShow() {
        return isShowing();
    }


    @Override
    public void onDateChanged(GregorianCalendar date) {
        this.date.setTime(date.getTime());
    }
}
