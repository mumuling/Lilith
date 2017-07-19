package com.youloft.lilith.common.widgets.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.youloft.lilith.R;

/**
 * Created by zchao on 2017/7/11.
 * desc: 此view仅用于下拉刷新头部。
 * version:
 */

public class RefreshImageView extends AppCompatImageView {
    public static final int REFRESH_PULL = 1;   //下拉过程中
    public static final int REFRESH_IN = 2;     //刷新动画开始
    public static final int REFRESH_DONE = 3;   //刷新完成，需要保持的状态

    public RefreshImageView(Context context) {
        this(context, null);
    }

    public RefreshImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    int st = 3;

    public void setState(int state, int rate) {
        //下拉或上啦过程，设置的rate来改变drawable的level
        if (state == REFRESH_PULL) {
            if (st != state) {
                setImageResource(R.drawable.grou);
                st = state;
            }
            Drawable ani = getDrawable();
            ani.setLevel(rate);
        } else if (state == REFRESH_IN) {    //刷新中，帧动画一直转圈
            if (st != state) {
                setImageResource(R.drawable.refresh);
                st = state;
            }
            AnimationDrawable ani = ((AnimationDrawable) getDrawable());
            if (!ani.isRunning()) {
                ani.start();
            }
        } else if (state == REFRESH_DONE) {    //请求完成时的静止状态
            if (getDrawable() == null) {
                return;
            }
            if (getDrawable() instanceof AnimationDrawable) {
                ((AnimationDrawable) getDrawable()).stop();
            }
            setImageResource(R.drawable.grow_00044);
        }
    }
}
