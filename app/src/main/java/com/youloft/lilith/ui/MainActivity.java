package com.youloft.lilith.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.event.TabChangeEvent;
import com.youloft.lilith.common.net.OnlineConfigAgent;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.cons.ConsRepo;
import com.youloft.lilith.ui.view.NavBarLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @Autowired(name = "/repo/cons")
    ConsRepo mConsRepo;
    @BindView(R.id.main_content)
    FrameLayout mContent;
    @BindView(R.id.main_nav_bar)
    NavBarLayout mNavBar;

    @BindView(R.id.main_content_tv)
    TextView tv;
    private TabManager mMainTabManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lilith);
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        //更新配置项
        OnlineConfigAgent.getInstance().onAppStart(getApplicationContext());
        mMainTabManager = new TabManager(this);

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 切换tab通知
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(TabChangeEvent event) {
        int selectTab = event.selectTab;
        if (mMainTabManager != null) {
            mMainTabManager.selectChange(selectTab);
        }
    }



    public static Bitmap shotRecyclerView(RecyclerView view) {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount() - 3;
            int height = 0;
            Paint paint = new Paint();
            paint.setTextSize(ViewUtil.dp2px(10));
            paint.setColor(Color.WHITE);
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            RecyclerView.RecycledViewPool recycledViewPool = view.getRecycledViewPool();
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 1; i < size; i++) {

                RecyclerView.ViewHolder holder = recycledViewPool.getRecycledView(adapter.getItemViewType(i));
                if(i==1) {
                    holder = view.getChildViewHolder(view.getChildAt(1));
                }else {
                    holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                    adapter.onBindViewHolder(holder, i);
                    holder.itemView.measure(
                            View.MeasureSpec.makeMeasureSpec(view.getWidth() - view.getPaddingLeft() - view.getPaddingRight(), View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(),
                            holder.itemView.getMeasuredHeight());
                }
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {
                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
                height += holder.itemView.getMeasuredHeight();
            }

            height += ViewUtil.dp2px(122);

            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);

            bigCanvas.drawColor(view.getResources().getColor(R.color.tab_share_color));

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                if (bitmap == null || bigBitmap.isRecycled()) {
                    continue;
                }
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }
            Bitmap bitmap = BitmapFactory.decodeResource(view.getResources(), R.drawable.login_phone_icon);
            if (bigBitmap != null && !bigBitmap.isRecycled()) {
                bigCanvas.drawBitmap(bitmap, (view.getWidth() - bitmap.getWidth())/2, iHeight + ViewUtil.dp2px(16), paint);
            }
            bigCanvas.drawText("定制我的运势", (view.getWidth() - paint.measureText("定制我的运势"))/2, height - ViewUtil.dp2px(17), paint);
        }
        return bigBitmap;
    }

}
