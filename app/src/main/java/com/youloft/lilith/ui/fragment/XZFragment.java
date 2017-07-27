package com.youloft.lilith.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewCanPullAble;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.common.event.ConsChangeEvent;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.CalendarHelper;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.common.widgets.view.PullToRefreshLayout;
import com.youloft.lilith.cons.ConsRepo;
import com.youloft.lilith.cons.bean.ConsPredictsBean;
import com.youloft.lilith.cons.card.ConsFragmentCardAdapter;
import com.youloft.lilith.cons.consmanager.LoddingCheckEvent;
import com.youloft.lilith.cons.consmanager.ShareConsEvent;
import com.youloft.lilith.cons.view.ConsGuideDialog;
import com.youloft.lilith.cons.view.LogInOrCompleteDialog;
import com.youloft.lilith.info.activity.EditInformationActivity;
import com.youloft.lilith.info.event.UserInfoUpDateEvent;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.login.event.LoginEvent;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.share.ShareBuilder;
import com.youloft.lilith.ui.view.NetErrDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.GregorianCalendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.blurry.internal.Blur;
import jp.wasabeef.blurry.internal.BlurFactor;

/**
 * Created by zchao on 2017/6/27.
 * desc: 星座fragment
 * version:
 */

public class XZFragment extends BaseFragment implements PullToRefreshLayout.OnRefreshListener {
    private FrameLayout mRoot;
    private View mState;
    private PullToRefreshLayout mRefreshLayout;
    private RecyclerView mConsList;
    private ConsFragmentCardAdapter mCardAdapter;
    private GregorianCalendar mCal = new GregorianCalendar();
    private float changeStateRange = ViewUtil.dp2px(200);
    private ConsGuideDialog consGuideDialog;

    public XZFragment() {
        super(R.layout.fragment_xz);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        init(view);
        initDate();
        mConsList.post(new Runnable() {
            @Override
            public void run() {
                int[] location = new int[2];
                View childAt = mConsList.getChildAt(1);
                if (childAt != null) {
                    View viewById = childAt.findViewById(R.id.cons_my_info_cons_img);
                    if (viewById != null) {
                        viewById.getLocationOnScreen(location);
                    }
                }
                showGuide(location);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 显示引导页
     */
    private void showGuide(int[] location) {
        if (AppSetting.isGuideShown()) {
            return;
        }
        Bitmap bitmap = null;
        if (getActivityContext() instanceof BaseActivity) {
            bitmap = ((BaseActivity) getActivityContext()).takeScreenShot(false, 0);
            if (bitmap != null && !bitmap.isRecycled()) {
                BlurFactor bf = new BlurFactor();
                bf.width = bitmap.getWidth();
                bf.height = bitmap.getHeight();
                bf.sampling = 5;
                bf.radius = 5;
                bitmap = Blur.of(getContext(), bitmap, bf);
            }
        }
        ConsGuideDialog.mBg = bitmap;
        consGuideDialog = new ConsGuideDialog(getActivityContext()).setConsImageLocation(location);
        consGuideDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (consGuideDialog != null && consGuideDialog.isShowing()) {
            consGuideDialog.dismiss();
        }
        EventBus.getDefault().unregister(this);
    }

    public static final int LOG_IN = 1;
    public static final int COMPLETE_INFO = 2;


    /**
     * 来自强制刷新
     */
    private void initDate() {
        refreshDate(null);
    }


    /**
     * 初始化数据
     */
    private void refreshDate(PullToRefreshLayout pullToRefreshLayout) {
        UserBean userInfo = AppSetting.getUserInfo();
        if (userInfo == null) {       //需要登录
            String date = "1990-04-02";
            String time = "12:00:00";
            mCardAdapter.setTitle("");
            getData(date, time, "", "", pullToRefreshLayout);  //没登录选双鱼
        } else {
            //有资料
            UserBean.DataBean.UserInfoBean userInfo1 = userInfo.data.userInfo;
            Date date = CalendarHelper.parseDate(userInfo1.birthDay, EditInformationActivity.DATE_FORMAT);
            mCal.setTime(date);
            mCardAdapter.setTitle(TextUtils.isEmpty(userInfo1.nickName) ? userInfo1.phone : userInfo1.nickName);
            getData(CalendarHelper.format(date, "yyyy-MM-dd"), CalendarHelper.format(date, "HH:mm:ss"), userInfo1.birthLongi, userInfo1.birthLati, pullToRefreshLayout);
        }
    }

    /**
     * 请求数据
     *
     * @param birdt
     * @param birtm
     * @param birlongi
     * @param birlati
     * @param pullToRefreshLayout
     */
    private void getData(String birdt, String birtm, String birlongi, String birlati, final PullToRefreshLayout pullToRefreshLayout) {
        ConsRepo.getConsPredicts(birdt, birtm, birlongi, birlati)
                .compose(this.<ConsPredictsBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<ConsPredictsBean>() {
                    @Override
                    public void onDataSuccess(ConsPredictsBean bean) {
                        if (bean != null) {
                            mCardAdapter.setData(bean);
                            if (bean.data != null) {
                                EventBus.getDefault().post(new ConsChangeEvent(bean.data.signs));
                            }
                        }
                        sendFinish(pullToRefreshLayout);
                        mConsList.post(new Runnable() {
                            @Override
                            public void run() {
                                mConsList.scrollToPosition(0);
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                        sendFinish(pullToRefreshLayout);
                        new NetErrDialog(getActivityContext()).show();
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                        sendFinish(pullToRefreshLayout);
                        new NetErrDialog(getActivityContext()).show();
                    }
                });
    }

    private void sendFinish(final PullToRefreshLayout pullToRefreshLayout) {
        if (pullToRefreshLayout != null) {
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
        }
    }


    /**
     * 显示登录画面或显示填写资料画面
     */
    private void showDialog(int type) {
        new LogInOrCompleteDialog(mContext).withBlurBg().setStatus(type).show();
    }

    /**
     * 分享星座
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(ShareConsEvent event) {
        String mSource = event.mSource;
        share();
    }

    /**
     * 登录状态改变
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onLoddingChagne(LoginEvent event) {
        initDate();
    }

    /**
     * 资料改变
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUpdate(UserInfoUpDateEvent event) {
        initDate();
    }


    /**
     * 拉起分享
     */
    private void share() {
        Bitmap bitmap = ViewUtil.shotRecyclerView((RecyclerViewCanPullAble) mConsList);
        if (bitmap != null && !bitmap.isRecycled()) {
            new ShareBuilder(getContext()).withImg(bitmap).share();
        }
    }

    /**
     * 检查登录状态，或个人信息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onLoddingCheck(LoddingCheckEvent event) {
        if (event != null) {
            checkUserInfo();
        }
    }

    private void checkUserInfo() {
        UserBean userInfo = AppSetting.getUserInfo();
        if (userInfo == null) {
            showDialog(LOG_IN);
        } else if (TextUtils.isEmpty(userInfo.data.userInfo.birthLongi) ||
                TextUtils.isEmpty(userInfo.data.userInfo.birthLati)) {
            showDialog(COMPLETE_INFO);
        }
    }


    /**
     * 初始化viewe
     *
     * @param view
     */
    private void init(View view) {
        mConsList = (RecyclerView) view.findViewById(R.id.cons_card);
        mRoot = (FrameLayout) view.findViewById(R.id.root);
        mState = view.findViewById(R.id.cons_statebar);
        mRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.cons_pull_to_refresh_layout);

        ViewGroup.LayoutParams layoutParams = mState.getLayoutParams();
        layoutParams.height = ViewUtil.getStatusHeight();
        mState.setLayoutParams(layoutParams);

        mRefreshLayout.setOnRefreshListener(this);

        mCardAdapter = new ConsFragmentCardAdapter(getContext());
        mConsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mConsList.setAdapter(mCardAdapter);

        mConsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int totalDy = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totalDy += dy;
                if (totalDy <= 0) {
                    mState.setAlpha(0f);
                } else if (totalDy <= changeStateRange) {
                    mState.setAlpha(totalDy / changeStateRange);
                }
            }
        });
    }

    /**
     * 下拉刷新时候回调此处,刷新业务逻辑写在这里边；在数据请求完成后，
     * 要调用pullToRefreshLayout的refreshFinish()方法；这样才能关闭刷新动作
     *
     * @param pullToRefreshLayout
     */
    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        refreshDate(pullToRefreshLayout);
    }
}
