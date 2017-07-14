package com.youloft.lilith.common.widgets.dialog;

import android.view.animation.Interpolator;

/**
 * Created by zchao on 2017/5/5.
 * desc: Dialog弹出动画插值器
 * version:
 */

public class EaseInOutCubicInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float input) {
        if (input < 0.5) {
            return (float) (4 * Math.pow(input, 3));
        } else {
            return (float) ((input - 1) * Math.pow(2 * input - 2, 2) + 1);
        }
    }
}
