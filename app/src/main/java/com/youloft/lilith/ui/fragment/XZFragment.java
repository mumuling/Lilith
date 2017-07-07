package com.youloft.lilith.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewEx;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.common.event.TabChangeEvent;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.cons.ConsRepo;
import com.youloft.lilith.cons.bean.ConsPredictsBean;
import com.youloft.lilith.cons.card.ConsFragmentCardAdapter;
import com.youloft.lilith.cons.consmanager.ShareConsEvent;
import com.youloft.lilith.share.ShareBuilder;
import com.youloft.lilith.ui.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zchao on 2017/6/27.
 * desc: 星座fragment
 * version:
 */

public class XZFragment extends BaseFragment {
    private FrameLayout mRoot;
    private RecyclerView mConsWeek;
    private ConsFragmentCardAdapter mCardAdapter;

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


    /**
     * 初始化数据
     */
    private void initDate() {
        ConsRepo.getConsPredicts("1989-11-11", "", "29.35", "106.33")
                .compose(this.<ConsPredictsBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ConsPredictsBean>() {
                    @Override
                    public void accept(@NonNull ConsPredictsBean consPredictsBean) throws Exception {
                        if (consPredictsBean != null) {
                            mCardAdapter.setData(consPredictsBean);
                        }
                    }
                });

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
     * 拉起分享
     */
    private void share() {
        Bitmap bitmap = ViewUtil.shotRecyclerView((RecyclerViewEx) mConsWeek);
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
        mConsWeek = (RecyclerView) view.findViewById(R.id.cons_card);
        mRoot = (FrameLayout) view.findViewById(R.id.root);
        mCardAdapter = new ConsFragmentCardAdapter(getContext());
        mConsWeek.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mConsWeek.setAdapter(mCardAdapter);
    }
}
