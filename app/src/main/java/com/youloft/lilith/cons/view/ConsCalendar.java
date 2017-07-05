package com.youloft.lilith.cons.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.youloft.lilith.cons.ConsCalAdapter;
import com.youloft.lilith.cons.bean.ConsPredictsBean;

/**
 * Created by zchao on 2017/7/1.
 * desc: 星座日历View
 * version:
 */

public class ConsCalendar extends RecyclerView {

    public static final int CONS_CAL_TYPE_WEEK = 1;     //显示为周视图
    public static final int CONS_CAL_TYPE_MONTH = 2;    //显示为月视图
    private ConsCalAdapter adapter;
    private OnClickListener listener;

    public ConsCalendar(@NonNull Context context) {
        this(context, null);
    }

    public ConsCalendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 设置整个calendar的点击
     * @param listener
     */
    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        gestureDetector.onTouchEvent(e);    //自己接管手势，否者直接点击无效果
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return true;
    }

    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (listener != null) {
                listener.onClick();
                return true;
            } else {
                return false;
            }
        }
    });

    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
        setLayoutManager(new GridLayoutManager(getContext(), 7));
        addItemDecoration(new ConsCalItemDecoration(getContext()));
        adapter = new ConsCalAdapter(getContext());
        setAdapter(adapter);

    }

    public void setCalType(int type) {
        adapter.setCalType(type);
    }

    /**
     * 传入数据
     * @param data
     */
    public void setData(ConsPredictsBean data) {
        if (data != null && data.data != null) {
            adapter.setConsData(data.data);
        }
    }

    public interface  OnClickListener{
        void onClick();
    }
}
