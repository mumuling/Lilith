package com.youloft.lilith.common.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by GYH on 2017/7/5.
 */

public class BounceableLinearLayout extends LinearLayout {
    private Scroller mScroller;
    private GestureDetector mGestureDetector;

    public BounceableLinearLayout(Context context) {
        this(context, null);
    }

    public BounceableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        setLongClickable(true);
        mScroller = new Scroller(context);
        mGestureDetector = new GestureDetector(context, new GestureListenerImpl());
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            System.out.println("computeScroll()---> " +
                    "mScroller.getCurrX()=" + mScroller.getCurrX() + "," +
                    "mScroller.getCurrY()=" + mScroller.getCurrY());
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //必须执行postInvalidate()从而调用computeScroll()
            //其实,在此调用invalidate();亦可
            postInvalidate();
        }
        super.computeScroll();
    }


    private boolean mScrolling;
    private float touchDownY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownY = event.getY();
                mScrolling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(touchDownY - event.getY()) >= ViewConfiguration.get(
                        getContext()).getScaledTouchSlop()) {
                    mScrolling = true;
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    mScrolling = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                mScrolling = false;
                break;
        }
        return mScrolling;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                //手指抬起时回到最初位置
                prepareScroll(0, 0);
                break;
            default:
                //其余情况交给GestureDetector手势处理
                return mGestureDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }


    class GestureListenerImpl implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            int disY = (int) ((distanceY - 0.5) / 2);//调整一下拉动的幅度
            beginScroll(0, disY);
            return false;
        }

        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

    }


    //滚动到目标位置
    protected void prepareScroll(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        beginScroll(dx, dy);
    }


    //设置滚动的相对偏移
    protected void beginScroll(int dx, int dy) {
        System.out.println("smoothScrollBy()---> dx=" + dx + ",dy=" + dy);
        //第一,二个参数起始位置;第三,四个滚动的偏移量
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
        System.out.println("smoothScrollBy()---> " +
                "mScroller.getFinalX()=" + mScroller.getFinalX() + "," +
                "mScroller.getFinalY()=" + mScroller.getFinalY());
        if (onMyScrollerListener != null) {
            onMyScrollerListener.scrollY(dy);
        }
        //必须执行invalidate()从而调用computeScroll()
        invalidate();
    }

    private OnMyScrollerListener onMyScrollerListener;

    public interface OnMyScrollerListener {
        void scrollY(int dy);
    }

    public void setOnMyScrollerListener(OnMyScrollerListener onMyScrollerListener) {
        this.onMyScrollerListener = onMyScrollerListener;
    }
}
