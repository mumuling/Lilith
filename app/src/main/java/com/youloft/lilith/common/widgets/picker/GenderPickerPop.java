package com.youloft.lilith.common.widgets.picker;

import android.content.Context;
import android.view.View;

import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.SafeUtil;
import com.youloft.lilith.common.widgets.dialog.PickerBaseDialog;
import com.youloft.lilith.common.widgets.picker.wheel.OnWheelChangedListener;
import com.youloft.lilith.common.widgets.picker.wheel.WheelView;
import com.youloft.lilith.common.widgets.picker.wheel.adapters.ArrayWheelAdapter;

/**
 * Created by zchao on 2017/7/9.
 * desc:性别选择弹窗，如果没有特殊需求，请直接调用{@link #getDefaultGenderPicker(Context)} (Context)},
 *       然后通过接口来获得选择结果{@link #setOnSelectListener(OnPickerSelectListener)}
 *       最后必须要调用show()才会弹出弹窗;
 * version:
 */

public class GenderPickerPop extends PickerBaseDialog implements CanShow, OnWheelChangedListener {
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

    private int visibleItems = DEF_VISIBLE_ITEMS;
    private OnPickerSelectListener<String> listener;

    public GenderPickerPop(final Context mContext) {
        super(mContext);
        setContentView(R.layout.pop_genderpicker);
        this.mContext = mContext;
        mConfirm = findViewById(R.id.tv_confirm);
        mCancel = findViewById(R.id.tv_cancel);
        mGenderWheel = (WheelView) findViewById(R.id.gender_wheel_view);
        mGenderWheel.addChangingListener(this);

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

    public GenderPickerPop setGender(String gender) {
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
        if (isShowing()) {
            dismiss();
        }
    }

    @Override
    public boolean isShow() {
        return isShowing();
    }


    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mGenderWheel) {
            mCurrentIndex = newValue;
        }
    }
}
