package com.youloft.lilith.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewEx;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.youloft.lilith.AppConfig;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.CalendarHelper;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.cons.ConsRepo;
import com.youloft.lilith.cons.bean.ConsPredictsBean;
import com.youloft.lilith.cons.card.ConsFragmentCardAdapter;
import com.youloft.lilith.cons.consmanager.ShareConsEvent;
import com.youloft.lilith.cons.view.LogInOrCompleteDialog;
import com.youloft.lilith.info.activity.EditInformationActivity;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.login.event.LoginEvent;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.share.ShareBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.GregorianCalendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zchao on 2017/6/27.
 * desc: 星座fragment
 * version:
 */

public class XZFragment extends BaseFragment {
    private FrameLayout mRoot;
    private ImageView mMaskView;
    private RecyclerView mConsList;
    private ConsFragmentCardAdapter mCardAdapter;
    private GregorianCalendar mCal = new GregorianCalendar();
    public XZFragment() {
        super(R.layout.fragment_xz);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        init(view);

        initDate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private static final int LOG_IN = 1;
    private static final int COMPLETE_INFO = 2;

    /**
     * 初始化数据
     */
    private void initDate() {
        if (!AppConfig.LOGIN_STATUS) {      //需要登录
            showDialog(LOG_IN);
            return;
        }
        UserBean userInfo = AppSetting.getUserInfo();
        UserBean.DataBean data = userInfo.data;
        if (data == null || data.userInfo == null || data.userInfo.id == 0) {       //需要登录
            showDialog(LOG_IN);
            return;
        }
        if (TextUtils.isEmpty(data.userInfo.birthDay)) {        //需要完善资料
            showDialog(COMPLETE_INFO);
            return;
        }
        UserBean.DataBean.UserInfoBean userInfo1 = data.userInfo;

        Date date = CalendarHelper.parseDate(userInfo1.birthDay, EditInformationActivity.DATE_FORMAT);
        mCal.setTime(date);

        ConsRepo.getConsPredicts(CalendarHelper.format(date, "yyyy-MM-dd"),CalendarHelper.format(date, "HH:mm:ss"),userInfo1.birthLongi,userInfo1.birthLati)
                .compose(this.<ConsPredictsBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<ConsPredictsBean>() {
                    @Override
                    public void onDataSuccess(ConsPredictsBean bean) {
                        if (bean != null) {
                            mCardAdapter.setData(bean);
                        }
                    }
                });

    }

    /**
     * 显示登录画面或显示填写资料画面
     */
    private void showDialog(int type) {
        new LogInOrCompleteDialog(mContext).show();
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
     * 拉起分享
     */
    private void share() {
        Bitmap bitmap = ViewUtil.shotRecyclerView((RecyclerViewEx) mConsList);
        if (bitmap != null && !bitmap.isRecycled()) {
            new ShareBuilder(getContext()).withImg(bitmap).share();
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
        mMaskView = (ImageView) view.findViewById(R.id.cons_mask);

        mCardAdapter = new ConsFragmentCardAdapter(getContext());
        mConsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mConsList.setAdapter(mCardAdapter);

        mMaskView.setAlpha(0.1f);
        mConsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }
            int totalDy = 0;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totalDy += dy;
                int height = mMaskView.getHeight();
                if (totalDy <= height) {
                    float alpha = 1 - (height- totalDy) * 1f / height;
                    mMaskView.setAlpha(alpha);
                    if (mMaskView.getVisibility() != View.VISIBLE) {
                        mMaskView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public int getScollYDistance(RecyclerView view) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) view.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }
}
