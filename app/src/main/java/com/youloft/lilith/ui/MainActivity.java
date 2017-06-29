package com.youloft.lilith.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;
import com.youloft.lilith.LLApplication;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.cache.CacheObj;
import com.youloft.lilith.common.cache.CacheStore;
import com.youloft.lilith.common.net.OkHttpUtils;
import com.youloft.lilith.common.net.OnlineConfigAgent;
import com.youloft.lilith.common.rx.RxFlowableUtil;
import com.youloft.lilith.common.rx.RxObservableUtil;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.cons.ConsRepo;
import com.youloft.lilith.share.ShareEventListener;
import com.youloft.lilith.ui.view.NavBarLayout;
import com.youloft.socialize.SocializeAction;
import com.youloft.socialize.SocializePlatform;
import com.youloft.socialize.media.ShareWeb;
import com.youloft.socialize.wrapper.ShareListener;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

import static com.youloft.lilith.LLApplication.getApiCache;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
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
        ButterKnife.bind(this);
        //更新配置项
        OnlineConfigAgent.getInstance().onAppStart(getApplicationContext());
        mMainTabManager = new TabManager(this);
//分享
        new SocializeAction(this)
                .setPlatform(SocializePlatform.QQ)
                .withText("fuck")
                .withMedia(new ShareWeb("http://www.baidu.com"))
                .setCallback(new ShareEventListener("Share") {
                    @Override
                    public void onStart(SocializePlatform share_media) {
                        super.onStart(share_media);
                    }
                }).share();
        new ConsRepo().testData()
                .compose(this.<HashMap>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new RxObserver<HashMap>() {
                    @Override
                    protected void onFailed(Throwable e) {
                        tv.setText("onFailed");
                    }

                    @Override
                    public void onDataSuccess(HashMap hashMap) {
                        tv.setText(hashMap.toString());
                        Log.d(TAG, "accept() called with: hashMap = [" + hashMap + "]");
                    }
                });
    }

}
