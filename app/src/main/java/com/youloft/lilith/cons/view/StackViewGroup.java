package com.youloft.lilith.cons.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by zchao on 2017/7/5.
 * desc: 用于星座日历icon的布局,不是很完善。修改时候注意哟，
 * version:
 */

public class StackViewGroup extends FrameLayout {
    public StackViewGroup(@NonNull Context context) {
        super(context);
    }

    public StackViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StackViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layoutChild(left, top, right, bottom);
    }

    /**
     * 布局子视图
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    private void layoutChild(int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        int width = right - left - getPaddingLeft() - getPaddingRight();
        if (childCount == 0) {
            return;
        } else if (childCount == 1) {
            View childAt = getChildAt(0);
            childAt.layout((width - childAt.getMeasuredWidth()) / 2 + getPaddingLeft(), top + getPaddingTop(), (width + childAt.getMeasuredWidth()) / 2 + getPaddingLeft(), bottom - getPaddingBottom());
            return;
        }
        int space = 0;
        if (childCount > 1) {
            space = (width - getChildAt(0).getMeasuredWidth()) / (childCount - 1);
        }
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.layout(right - child.getMeasuredWidth() - space * i - getPaddingRight(), top + getPaddingTop(), right - space * i - getPaddingRight(), bottom - getPaddingBottom());
        }
    }
}
