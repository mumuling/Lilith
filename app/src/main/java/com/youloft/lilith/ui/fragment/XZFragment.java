package com.youloft.lilith.ui.fragment;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.cons.ConsCalDetailActivity;
import com.youloft.lilith.cons.ConsRepo;
import com.youloft.lilith.cons.bean.ConsPredictsBean;
import com.youloft.lilith.cons.bean.LuckData;
import com.youloft.lilith.cons.card.ConsFragmentCardAdapter;
import com.youloft.lilith.cons.view.ConsCalendar;
import com.youloft.lilith.cons.view.ConsCalendarDetailView;
import com.youloft.lilith.cons.view.LuckView;

import java.util.ArrayList;

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


        init(view);

//        initDate();
    }

    /**
     * 初始化数据
     */
    private void initDate() {
        ConsRepo.getConsPredicts("1989-11-11","","","").subscribeOn(Schedulers.newThread())
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
     * 初始化view
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
