package com.youloft.lilith.topic.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import com.youloft.lilith.common.utils.ViewUtil;

/**
 *
 */

public class ScrollFrameLayout extends RecyclerView {
    protected float mCurrentMarginTop;
    protected float mTop;
    private float mMinMoveDistance;
    protected static float FINISH_DISTANCE = 300;
    protected boolean needRcover = true;
    protected boolean needScrollDown = true;//默认不滑动，若要设置滑动，调用setNeedScrollDown()；
    protected  boolean needAculateTop = true;

    private ScrollFrameLayout.IscrollChange iscrollChange;
    public ScrollFrameLayout(Context context) {
        this(context, null);
    }

    private AccelerateInterpolator mAccelerateInterpolator = new AccelerateInterpolator(.15f);


    public ScrollFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setClickable(true);
        mMinMoveDistance = ViewUtil.dp2px(7);
        mTop = ViewUtil.getStatusHeight();
        mCurrentMarginTop = mTop;


    }

//    private boolean isBounceScrollEnable = false;
//    public void setBounceScrollEnable(boolean enable){
//        isBounceScrollEnable = enable;
//    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (needScrollDown) {
            if(handleEvent(ev))return true;
        }
        return super.onTouchEvent(ev);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int downX;
        if (needScrollDown) {
            return handleEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }
    public void  setNeedScrollDown(boolean needScrollDown) {
        this.needScrollDown = needScrollDown;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void onScroll(float goaldistanceY, boolean isScollOver) {
        View view = (View) this.getParent();
        if (view == null )return;
        if (isScollOver) {
            ObjectAnimator translateY = ObjectAnimator.ofFloat(view, "y", mCurrentMarginTop);
            translateY.setInterpolator(mAccelerateInterpolator);
            translateY.setDuration(200);
            translateY.start();

        } else {
            ObjectAnimator translateY = ObjectAnimator.ofFloat(view, "y", goaldistanceY);
            translateY.setDuration(0);
            translateY.start();
        }

    }

    private float beforeY;
    private boolean mIsFilt = false;
    private float mStartY;
    private boolean isIntercept  =false;
    private boolean handleEvent(MotionEvent event) {
//        if(!isBounceScrollEnable){
//            return false;
//        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                beforeY = event.getRawY();
                mStartY = event.getRawY();
                return false;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                mIsFilt = true;
                return super.onTouchEvent(event);
            case MotionEvent.ACTION_MOVE:
                if(!isIntercept && Math.abs(event.getRawY() - mStartY) <= mMinMoveDistance){
                    return false;
                } else{
                    isIntercept = true;
                }

                if (mIsFilt) {
                    mIsFilt = false;
                    beforeY = event.getRawY();
                }

                float distance = event.getRawY() - beforeY;
                beforeY = event.getRawY();
                if (Math.abs(distance) <= 0) {
                    return false;
                }

                if (mCurrentMarginTop <=this.getTop()) {
                    if ( getChildAt(0).getTop() ==0 && distance > 0) {
                        mCurrentMarginTop = mCurrentMarginTop + distance;
                        onScroll(mCurrentMarginTop, false);
                        if (iscrollChange != null) {
                            iscrollChange.move(mCurrentMarginTop);
                        }
                        return true;
                    }
                }


                if (mCurrentMarginTop > mTop) {
                    mCurrentMarginTop = mCurrentMarginTop + distance;
                    if (mCurrentMarginTop < mTop ) mCurrentMarginTop = mTop;
                    needRcover = mCurrentMarginTop <= FINISH_DISTANCE;
//                    if (mCurrentMarginTop + distance > 0) {
//                        mCurrentMarginTop = 0;
//                    }
                    onScroll(mCurrentMarginTop, false);
                    if (iscrollChange != null) {
                        iscrollChange.move(mCurrentMarginTop);
                    }
                   // iscrollChange.scrolling(mCurrentMarginTop,FINISH_DISTANCE);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isIntercept = false;
                if (needRcover) {
                    mCurrentMarginTop = mTop;
                    iscrollChange.recover();

                } else {
                    mCurrentMarginTop = getHeight();
                    if (mCurrentMarginTop > 10) {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (iscrollChange != null) {
                                    iscrollChange.goFinish();
                                }
                            }
                        }, 200);
                        // iscrollChange.onBacks();
                    }
                }
                onScroll(mCurrentMarginTop, true);

                 return false;
        }

         return false;
    }

    //将接口设置进去
    public void setInterface(ScrollFrameLayout.IscrollChange iscrollChange){
        this.iscrollChange = iscrollChange;
    }
    public interface IscrollChange{
        void goFinish();
        void recover();
        void move(float distance);
    }

}
