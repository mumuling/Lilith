package com.youloft.lilith.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;

import java.lang.ref.WeakReference;

/**
 * Created by zchao on 2017/7/18.
 * desc: 先大致写着，
 * version:
 */

public class SplashActivity extends BaseActivity {

    //    private ViewPager mGuidePager;
//    private int[] pagerSrc = {R.drawable.welcome, R.drawable.welcome, R.drawable.welcome};
//    private HashMap<String, View> views = new HashMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (AppSetting.isFirst()) {
//            setContentView(R.layout.lilith_guide);
//            init();
//        } else {
        MyHandler handler = new MyHandler(this);

        handler.sendEmptyMessageDelayed(0, 2000);

//        }
    }

    static class MyHandler extends Handler {
        WeakReference<Activity> mWeakReference;

        public MyHandler(Activity activity) {
            mWeakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mWeakReference.get();
            if (activity != null) {
                activity.finish();
            }
        }
    }
//    private void init() {
//        mGuidePager = (ViewPager) findViewById(R.id.guide_pager);
//        mGuidePager.setAdapter(new PagerAdapter() {
//            @Override
//            public int getCount() {
//                return pagerSrc.length;
//            }
//
//            @Override
//            public boolean isViewFromObject(View view, Object object) {
//                return view == object;
//            }
//
//            @Override
//            public Object instantiateItem(ViewGroup container, int position) {
//                View inflate = null;
//                if (position == pagerSrc.length - 1) {
//                    inflate = getLayoutInflater().inflate(R.layout.guide_page_last, null);
//                    inflate.findViewById(R.id.guide_close).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            AppSetting.setFirst();
//                            finish();
//                        }
//                    });
//                } else {
//                    inflate = getLayoutInflater().inflate(R.layout.guide_page, null);
//                }
//                ImageView viewById = (ImageView) inflate.findViewById(R.id.guide_page_img);
//                viewById.setImageResource(pagerSrc[position]);
//                container.addView(inflate);
//                views.put(String.valueOf(position), inflate);
//                return inflate;
//            }
//
//            @Override
//            public void destroyItem(ViewGroup container, int position, Object object) {
//                container.removeView(views.get(String.valueOf(position)));
//            }
//        });
//    }


    /**
     * 打开开屏,如果是重新加载的则不会展示开屏
     *
     * @param activity
     * @param savedInstanceState
     */
    public static void startSplashActivity(Activity activity, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            return;
        }
        Intent intent = new Intent(activity, SplashActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_alpha, R.anim.slide_out_alpha);
    }
}
