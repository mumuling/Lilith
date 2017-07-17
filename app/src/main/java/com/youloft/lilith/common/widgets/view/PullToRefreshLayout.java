package com.youloft.lilith.common.widgets.view;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.youloft.lilith.R;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Desc: 下拉刷新控件，带下拉刷新的控件必须实现{@link Pullable}接口，
 * 否者会报错;（目前本工程只有{@link android.support.v7.widget.RecyclerViewCanPullAble}支持下拉刷新）
 * <p>
 * 在需要进行刷新操作的地方实现{@link OnRefreshListener},
 * 在接口实现方法里边进行刷新操作，刷新完成后记得调用{@link #refreshFinish(int)}来影藏刷新控件
 * Change:
 *
 * @author zchao created at 2017/7/12 12:24
 * @see
 */
public class PullToRefreshLayout extends RelativeLayout {
    public static final String TAG = "PullToRefreshLayout";
    // 初始状态
    public static final int INIT = 0;
    // 释放刷新
    public static final int RELEASE_TO_REFRESH = 1;
    // 正在刷新
    public static final int REFRESHING = 2;
    // 操作完毕
    public static final int DONE = 5;
    // 当前状态
    private int state = INIT;
    // 刷新回调接口
    private OnRefreshListener mListener;
    // 刷新成功
    public static final int SUCCEED = 0;
    // 刷新失败
    public static final int FAIL = 1;
    // 按下Y坐标，上一个事件点Y坐标
    private float downY, lastY, downX, lastX;

    // 下拉的距离
    public float pullDownY = 0;

    // 释放刷新的距离
    private float refreshDist = 200;
    //帧动画开始的距离,这个距离是根据帧动画开始播放的位置来定的大致位置
    private float refreshStartDist = 100;

    private MyTimer timer;
    // 回滚速度
    public float MOVE_SPEED = 8;
    // 第一次执行布局
    private boolean isLayout = false;
    // 在刷新过程中滑动操作
    private boolean isTouch = false;
    // 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
    private float radio = 2;
    //当开始刷新时候的时间，用于处理时间过短动画显示都不全的问题
    private long mStartTime = 0;
    private RefreshImageView mRefreshIcon;


    // 实现了Pullable接口的View
    private View pullableView;
    // 过滤多点触碰
    private int mEvents;
    // 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
    private boolean canPullDown = true;

    private Context mContext;

    /**
     * 执行自动回滚的handler
     */
    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // 回弹速度随下拉距离moveDeltaY增大而增大
            MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2
                    / getMeasuredHeight() * (pullDownY)));
            if (!isTouch) {
                if (state == REFRESHING && pullDownY <= refreshDist) {
                    pullDownY = refreshDist;
                    timer.cancel();
                }

            }
            if (pullDownY > 0)
                pullDownY -= MOVE_SPEED;
            if (pullDownY < 0) {
                // 已完成回弹
                pullDownY = 0;
                // 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (state != REFRESHING)
                    changeState(INIT, 0);
                timer.cancel();
                requestLayout();
            }
            // 刷新布局,会自动调用onLayout
            requestLayout();
            // 没有拖拉或者回弹完成
            if (pullDownY == 0)
                timer.cancel();
        }

    };
    private View refreshView;
    private int mTouchSlop;

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public PullToRefreshLayout(Context context) {
        super(context);
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        timer = new MyTimer(updateHandler);
        View inflate = LayoutInflater.from(context).inflate(R.layout.refresh_group, this);
        final ViewConfiguration configuration = ViewConfiguration.get(context);

        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
    }

    private void hide() {
        timer.schedule(5);
    }

    /**
     * 完成刷新操作，显示刷新结果。注意：刷新完成后一定要调用这个方法
     */
    /**
     * @param refreshResult PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
     */
    public void refreshFinish(int refreshResult) {
        if (state != REFRESHING) {
            return;
        }
        long l = System.currentTimeMillis() - mStartTime;
        //这儿处理开始刷新到刷新成功时间过短
        if (l < 1000) {
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    closeRefreshBar();
                }
            }.sendEmptyMessageDelayed(0, 1000 - l);
        }

    }

    private void closeRefreshBar() {
        //设置静止状态
        mRefreshIcon.setState(RefreshImageView.REFRESH_DONE, 0);
        if (pullDownY > 0) {
            // 刷新结果停留1秒
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    changeState(DONE, 0);
                    hide();
                }
            }.sendEmptyMessageDelayed(0, 500);
        } else {
            changeState(DONE, 0);
            hide();
        }
    }


    private void changeState(int to, float offset) {
        state = to;
        switch (state) {
            case INIT:
                // 下拉布局初始状态
                mRefreshIcon.setState(RefreshImageView.REFRESH_PULL, 0);    //保持最后不动
                break;
            case RELEASE_TO_REFRESH:
                offset -= refreshStartDist;
                if (offset < refreshDist) {
                    float rota = (offset) / refreshDist * 44;
                    mRefreshIcon.setState(RefreshImageView.REFRESH_PULL, Math.round(rota));
                } else {
                    mRefreshIcon.setState(RefreshImageView.REFRESH_PULL, 44);   //保持一张最后不动
                }
                break;
            case REFRESHING:
                // 正在刷新状态
                mRefreshIcon.setState(RefreshImageView.REFRESH_IN, 1);
                break;
            case DONE:
                //只是跟新状态
                mStartTime = 0;
                break;
        }
    }

    /**
     * 不限制上拉或下拉
     */
    private void releasePull() {
        canPullDown = true;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int actionMasked = ev.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                downX = ev.getX();
                lastY = downY;
                lastX = downX;
                timer.cancel();
                mEvents = 0;
                releasePull();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // 过滤多点触碰
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                float defX = Math.abs(ev.getX() - downX);
                float defY = Math.abs(ev.getY() - downY);
                if (defX > mTouchSlop && defX > defY) {
                    //直接跳出，交给系统分发；感觉不是很科学啊。
                    break;
                }
                if (mEvents == 0) {
                    if (pullDownY > 0
                            || (((Pullable) pullableView).canPullDown()
                            && canPullDown)) {
                        // 可以下拉，正在加载时不能下拉
                        // 对实际滑动距离做缩小，造成用力拉的感觉
                        pullDownY = pullDownY + (ev.getY() - lastY) / radio;
                        if (pullDownY < 0) {
                            pullDownY = 0;
                            canPullDown = false;
                        }
                        if (pullDownY > getMeasuredHeight())
                            pullDownY = getMeasuredHeight();
                        if (state == REFRESHING) {
                            // 正在刷新的时候触摸移动
                            isTouch = true;
                        }
                    } else
                        releasePull();
                } else
                    mEvents = 0;
                lastY = ev.getY();
                // 根据下拉距离改变比例
                radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()
                        * (pullDownY)));
                if (pullDownY > 0 && pullDownY > mTouchSlop)
                    requestLayout();
                if (pullDownY > 0 && pullDownY > mTouchSlop) {
                    if (pullDownY <= refreshStartDist && (state == RELEASE_TO_REFRESH || state == DONE || state == INIT)) {
                        changeState(INIT, pullDownY);
                    }
                    if (pullDownY >= refreshStartDist && (state == RELEASE_TO_REFRESH || state == DONE || state == INIT)) {
                        // 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
                        changeState(RELEASE_TO_REFRESH, pullDownY);
                    }
                }
                if ((pullDownY) > 8) {
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (pullDownY > refreshDist) {
                    isTouch = false;
                }
                if (state == RELEASE_TO_REFRESH) {
                    changeState(REFRESHING, 1);
                    // 刷新操作
                    if (mListener != null) {
                        mListener.onRefresh(this);
                        mStartTime = System.currentTimeMillis();
                    }
                }
                hide();
            default:
                break;
        }
        // 事件分发交给父类
        super.dispatchTouchEvent(ev);
        return true;
    }

    /**
     * @author chenjing 自动模拟手指滑动的task
     */
    private class AutoRefreshAndLoadTask extends
            AsyncTask<Integer, Float, String> {

        @Override
        protected String doInBackground(Integer... params) {
            while (pullDownY < 4 / 3 * refreshDist) {
                pullDownY += MOVE_SPEED;
                publishProgress(pullDownY);
                try {
                    Thread.sleep(params[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            changeState(REFRESHING, 0);
            // 刷新操作
            if (mListener != null)
                mListener.onRefresh(PullToRefreshLayout.this);
            hide();
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            if (pullDownY > refreshDist)
                changeState(RELEASE_TO_REFRESH, 0);
            requestLayout();
        }

    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {
        AutoRefreshAndLoadTask task = new AutoRefreshAndLoadTask();
        task.execute(20);
    }


    private void initView() {
        // 初始化下拉布局
        mRefreshIcon = (RefreshImageView) refreshView.findViewById(R.id.refresh_icon);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isLayout) {
            refreshView = getChildAt(0);
            pullableView = getChildAt(1);
            isLayout = true;
            initView();
            refreshDist = ((ViewGroup) refreshView).getChildAt(0)
                    .getMeasuredHeight();
        }
        refreshView.layout(0,
                (int) (pullDownY) - refreshView.getMeasuredHeight(),
                refreshView.getMeasuredWidth(), (int) (pullDownY));
        pullableView.layout(0, (int) (pullDownY),
                pullableView.getMeasuredWidth(), (int) (pullDownY)
                        + pullableView.getMeasuredHeight());

    }

    class MyTimer {
        private Handler handler;
        private Timer timer;
        private MyTask mTask;

        public MyTimer(Handler handler) {
            this.handler = handler;
            timer = new Timer();
        }

        public void schedule(long period) {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
            mTask = new MyTask(handler);
            timer.schedule(mTask, 0, period);
        }

        public void cancel() {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
        }

        class MyTask extends TimerTask {
            private Handler handler;

            public MyTask(Handler handler) {
                this.handler = handler;
            }

            @Override
            public void run() {
                handler.obtainMessage().sendToTarget();
            }

        }
    }

    /**
     * 刷新加载回调接口
     *
     * @author chenjing
     */
    public interface OnRefreshListener {
        /**
         * 刷新操作
         */
        void onRefresh(PullToRefreshLayout pullToRefreshLayout);
    }

}
