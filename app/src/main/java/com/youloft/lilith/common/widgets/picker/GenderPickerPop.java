package com.youloft.lilith.common.widgets.picker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.SafeUtil;
import com.youloft.lilith.common.widgets.picker.wheel.OnWheelChangedListener;
import com.youloft.lilith.common.widgets.picker.wheel.WheelView;
import com.youloft.lilith.common.widgets.picker.wheel.adapters.ArrayWheelAdapter;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by zchao on 2017/7/9.
 * desc:
 * version:
 */

public class GenderPickerPop implements CanShow, OnWheelChangedListener{

    private final PopupWindow popwindow;
    private final View popview;
    private final View mCancel;
    private final View mConfirm;
    Context mContext;
    private final WheelView mGenderWheel;

    private static final String[] list = {"男", "女"};
    private int mCurrentIndex = 0;


    /**
     * Default text color
     */
    public static final int DEFAULT_TEXT_COLOR = 0xFFFFFFFF;

    /**
     * Default text size
     */
    public static final int DEFAULT_TEXT_SIZE = 18;
    private int textColor = DEFAULT_TEXT_COLOR;

    private int textSize = DEFAULT_TEXT_SIZE;


    /**
     * 区滚轮是否循环滚动
     */
    private boolean isCyclic = false;

    /**
     * item间距
     */
    private int padding = 5;
    /**
     * 滚轮显示的item个数
     */
    private static final int DEF_VISIBLE_ITEMS = 5;

    // Count of visible items
    private int visibleItems = DEF_VISIBLE_ITEMS;
    private OnPickerSelectListener<String> listener;

    public GenderPickerPop(final Context mContext) {
        this.mContext = mContext;

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        popview = layoutInflater.inflate(R.layout.pop_genderpicker, null);
        mConfirm = popview.findViewById(R.id.tv_confirm);
        mCancel = popview.findViewById(R.id.tv_cancel);
        mGenderWheel = (WheelView) popview.findViewById(R.id.gender_wheel_view);
        mGenderWheel.addChangingListener(this);
        popwindow = new PopupWindow(popview, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        popwindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popwindow.setAnimationStyle(R.style.AnimBottom);
        popwindow.setTouchable(true);
        popwindow.setOutsideTouchable(false);
        popwindow.setFocusable(true);

        setUpData();

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSelected(SafeUtil.getSafeArrayData(list, mCurrentIndex));
                }
                hide();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCancel();
                }
                hide();
            }
        });
    }

    public GenderPickerPop setOnSelectListener(OnPickerSelectListener listener) {
        this.listener = listener;
        return this;
    }

    public static GenderPickerPop getDefaultGenderPicker(Context context) {
        return new GenderPickerPop(context);
    }

    public GenderPickerPop setGender(String gender){
        if (gender.equals("男")) {
            mGenderWheel.setCurrentItem(0);
        } else if (gender.equals("女")) {
            mGenderWheel.setCurrentItem(1);
        } else {
            mGenderWheel.setCurrentItem(0);
        }
        return this;
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
        mGenderWheel.setCyclic(isCyclic);
        mGenderWheel.setVisibleItems(visibleItems);
        ArrayWheelAdapter genderWheelAdapter = new ArrayWheelAdapter<String>(mContext, list);
        // 设置可见条目数量
        genderWheelAdapter.setTextColor(textColor);
        genderWheelAdapter.setTextSize(textSize);
        mGenderWheel.setViewAdapter(genderWheelAdapter);
        mGenderWheel.setCurrentItem(0);
        //获取默认设置的区
        mCurrentIndex = 0;
        genderWheelAdapter.setPadding(padding);
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
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mGenderWheel) {
            mCurrentIndex = newValue;
        }
    }
}
