package com.youloft.lilith.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewEx;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.youloft.lilith.AppConfig;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.common.event.ConsChangeEvent;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.CalendarHelper;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.cons.ConsRepo;
import com.youloft.lilith.cons.bean.ConsPredictsBean;
import com.youloft.lilith.cons.card.ConsFragmentCardAdapter;
import com.youloft.lilith.cons.consmanager.LoddingCheckEvent;
import com.youloft.lilith.cons.consmanager.ShareConsEvent;
import com.youloft.lilith.cons.view.LogInOrCompleteDialog;
import com.youloft.lilith.info.activity.EditInformationActivity;
import com.youloft.lilith.info.bean.UpdateUserInfoBean;
import com.youloft.lilith.info.event.UserInfoUpDateEvent;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.login.event.LoginEvent;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.share.ShareBuilder;
import com.youloft.lilith.ui.MainActivity;

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

    public static final int LOG_IN = 1;
    public static final int COMPLETE_INFO = 2;


    /**
     * 初始化数据
     */
    private void initDate() {
        UserBean userInfo = AppSetting.getUserInfo();
        if (userInfo == null ||
                userInfo.data == null ||
                userInfo.data.userInfo == null ||
                userInfo.data.userInfo.id == 0 ||
                TextUtils.isEmpty(userInfo.data.userInfo.birthDay)) {       //需要登录
            String date = "1990-04-02";
            String time = "12:00:00";
            mCardAdapter.setTitle("");
            getData(date, time, "", "");  //没登录选双鱼
        } else {
            //有资料
            UserBean.DataBean.UserInfoBean userInfo1 = userInfo.data.userInfo;
            Date date = CalendarHelper.parseDate(userInfo1.birthDay, EditInformationActivity.DATE_FORMAT);
            mCal.setTime(date);
            mCardAdapter.setTitle(TextUtils.isEmpty(userInfo1.nickName)? userInfo1.phone : userInfo1.nickName);
            getData(CalendarHelper.format(date, "yyyy-MM-dd"), CalendarHelper.format(date, "HH:mm:ss"), userInfo1.birthLongi, userInfo1.birthLati);
        }
    }

    private void getData(String birdt, String birtm, String birlongi, String birlati) {
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
                    }
                });
    }


    /**
     * 显示登录画面或显示填写资料画面
     */
    private void showDialog(int type) {
        new LogInOrCompleteDialog(mContext).setStatus(type).show();
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
        Log.d(TAG, "onLoddingChagne() called with: event = [" + event + "]");
    }

    /**
     * 资料改变
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUpdate(UserInfoUpDateEvent event) {
        initDate();
        Log.d(TAG, "onLoddingChagne() called with: event = [" + event + "]");
    }

    private static final String TAG = "XZFragment";
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
        if (!AppConfig.LOGIN_STATUS) {
            showDialog(LOG_IN);
        } else {
            UserBean userInfo = AppSetting.getUserInfo();
            UserBean.DataBean data = userInfo.data;
            if (data == null ||
                    data.userInfo == null ||
                    data.userInfo.id == 0 ||
                    TextUtils.isEmpty(data.userInfo.birthDay)||
                    TextUtils.isEmpty(data.userInfo.birthPlace)) {
                showDialog(COMPLETE_INFO);
            }
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

        mCardAdapter = new ConsFragmentCardAdapter(getContext());
        mConsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mConsList.setAdapter(mCardAdapter);

    }
}
