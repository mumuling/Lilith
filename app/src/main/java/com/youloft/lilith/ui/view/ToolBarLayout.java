package com.youloft.lilith.ui.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.youloft.lilith.common.utils.Utils;

/**
 * Created by zchao on 2017/6/26.
 * desc: 顶部Toolbar的容器，可以在使用了沉浸式菜单栏的时候自动加上一个statubar的高度
 * version:
 */

public class ToolBarLayout extends FrameLayout {
    private boolean isFixed = false;
    private int mAddedHeight = 0;
    public ToolBarLayout(@NonNull Context context) {
        this(context, null);
    }

    public ToolBarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setToolBarLayoutFixWindow(true);
    }

    /**
     * 设置NavBarLayout填充，
     * @param fix 如果设置为false则不会在顶部添加一个statubar的高度；
     */
    public void setToolBarLayoutFixWindow(boolean fix){
        if (fix == isFixed) {
            return;
        }
        if (fix) {
            mAddedHeight = Utils.getStatusHeight(getContext());
        } else {
            mAddedHeight = -Utils.getStatusHeight(getContext());
        }
        if (Build.VERSION.SDK_INT >= 19) {
            setPadding(getPaddingLeft(), getPaddingTop()
                    + mAddedHeight, getPaddingRight(), getPaddingBottom());
        }
        isFixed = !isFixed;
    }

}

