package com.youloft.lilith.topic.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;

import com.youloft.lilith.common.utils.ViewUtil;

/**
 *
 */

public class ScrollFrameLayout extends FrameLayout {
    protected float mCurrentMarginTop;
    private float mMinMoveDistance;
    protected static float FINISH_DISTANCE = 300;
    protected boolean needRcover = true;
    protected boolean needScrollDown = true;//默认不滑动，若要设置滑动，调用setNeedScrollDown()；

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
    }

//    private boolean isBounceScrollEnable = false;
//    public void setBounceScrollEnable(boolean enable){
//        isBounceScrollEnable = enable;
//    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (needScrollDown) {
            if(handleEvent(ev))return true;
        }
        return super.onTouchEvent(ev);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (needScrollDown) {
            return true;
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
        if (isScollOver) {
            ObjectAnimator translateY = ObjectAnimator.ofFloat(this, "y", mCurrentMarginTop);
            translateY.setInterpolator(mAccelerateInterpolator);
            translateY.setDuration(200);
            translateY.start();

        } else {
            ObjectAnimator translateY = ObjectAnimator.ofFloat(this, "y", goaldistanceY);
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
                return false;
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

                if (mCurrentMarginTop == 0) {
                    if ( getChildAt(0).getTop() ==96 && distance > 0) {
                        mCurrentMarginTop = mCurrentMarginTop + distance;
                        onScroll(mCurrentMarginTop, false);
                        if (iscrollChange != null) {
                            iscrollChange.move();
                        }
                        return true;
                    }
                }

                if (mCurrentMarginTop > 0) {
                    distance = distance;
                    mCurrentMarginTop = mCurrentMarginTop + distance;
                    needRcover = mCurrentMarginTop <= FINISH_DISTANCE;
//                    if (mCurrentMarginTop + distance > 0) {
//                        mCurrentMarginTop = 0;
//                    }
                    onScroll(mCurrentMarginTop, false);
                    if (iscrollChange != null) {
                        iscrollChange.move();
                    }
                   // iscrollChange.scrolling(mCurrentMarginTop,FINISH_DISTANCE);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isIntercept = false;
                if (needRcover) {
                    mCurrentMarginTop = 0;
                    iscrollChange.recover();
                } else {
                    mCurrentMarginTop = getHeight();
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (iscrollChange != null) {
                                iscrollChange.goFinish();
                            }
                        }
                    },200);
                   // iscrollChange.onBacks();
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
        void move();
    }

}
