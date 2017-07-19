package com.youloft.lilith.cons.card;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by zchao on 2017/7/19.
 * desc:
 * version:
 */

public class ShadowCard extends FrameLayout {
    public ShadowCard(@NonNull Context context) {
        this(context, null);
    }

    public ShadowCard(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowCard(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setBackgroundResource(R.drawable.card_shadow_center_pic);
    }
}
