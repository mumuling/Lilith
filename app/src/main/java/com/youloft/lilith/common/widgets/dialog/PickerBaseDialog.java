package com.youloft.lilith.common.widgets.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.youloft.lilith.R;

/**
 * Created by zchao on 2017/7/13.
 * desc: 资料修改页面的Dialog基类，有默认动画
 * version:
 */

public class PickerBaseDialog extends BaseDialog {
    public FrameLayout mBaseRoot;
    public ImageView mBaseBg;
    public View mContent;

    public PickerBaseDialog(@NonNull Context context) {
        super(context, R.style.PickDialog);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View inflate = LayoutInflater.from(getContext()).inflate(layoutResID, null);
        this.setContentView(inflate);
    }

    @Override
    public void setContentView(@NonNull View view) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM;
        this.setContentView(view, layoutParams);
    }

    @Override
    public void setContentView(@NonNull View view, @Nullable ViewGroup.LayoutParams params) {
        super.setContentView(R.layout.picker_base_dialog);
        mBaseRoot = (FrameLayout) findViewById(R.id.picker_base_root);
        mBaseBg = (ImageView) findViewById(R.id.picker_base_bg);
        mBaseRoot.addView(view, params);
        mContent = view;
        mBaseBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        mBaseRoot.post(new Runnable() {
            @Override
            public void run() {
                inAndOutAnima(true);
            }
        });
    }

    private void inAndOutAnima(final boolean in) {
        ValueAnimator alphaAnia = ValueAnimator.ofFloat(in ? 0f : 1f, in ? 1f : 0f);
        alphaAnia.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mBaseBg.setAlpha(value);
            }
        });

        ValueAnimator transAnim = ObjectAnimator.ofFloat(mContent, View.TRANSLATION_Y, in ? mContent.getHeight() : 0, in ? 0 : mContent.getHeight());
        AnimatorSet set = new AnimatorSet();
        set.setDuration(350);
        set.setInterpolator(new EaseInOutCubicInterpolator());
        set.playTogether(alphaAnia, transAnim);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!in) {
                    PickerBaseDialog.super.dismiss();
                }
            }
        });
        set.start();
    }

    @Override
    public void dismiss() {
        inAndOutAnima(false);
    }
}
