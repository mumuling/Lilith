package com.youloft.lilith.ui.fragment;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
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

        initDate();
    }

    /**
     * 初始化数据
     */
    private void initDate() {
//        ConsRepo.getConsPredicts("1989-11-11","","","").subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<ConsPredictsBean>() {
//                    @Override
//                    public void accept(@NonNull ConsPredictsBean consPredictsBean) throws Exception {
//                        if (consPredictsBean != null) {
//                            mCardAdapter.setData(consPredictsBean);
//                        }
//                    }
//                });

        ConsPredictsBean consPredictsBean = JSON.parseObject(test, ConsPredictsBean.class);
        if (consPredictsBean != null) {
            mCardAdapter.setData(consPredictsBean);
        }
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

    String test = "{\"data\":{\"bgImg\":\"http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg\",\"msg\":\"完美的一句话概括，运势的情况.一定要有逼格！\",\"eMsg\":\"Therearenoshortcutstoanyplaceworthgoing.\",\"signs\":1,\"msgAvg\":\"个人心情十分复杂，此时的你最需要的是让心静下来，调整好心情。\",\"msglove\":\"个人心情十分复杂，此时的你最需要的是让心静下来，调整好心情。\",\"msgcareer\":\"个人心情十分复杂，此时的你最需要的是让心静下来，调整好心情。\",\"msgwealth\":\"个人心情十分复杂，此时的你最需要的是让心静下来，调整好心情。\",\"predicts\":[{\"avg\":5,\"date\":\"2017-07-02\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":3,\"date\":\"2017-07-03\",\"ptlove\":2,\"ptcareer\":3,\"ptwealth\":4},{\"avg\":6,\"date\":\"2017-07-04\",\"ptlove\":4,\"ptcareer\":8,\"ptwealth\":8},{\"avg\":5,\"date\":\"2017-07-05\",\"ptlove\":8,\"ptcareer\":9,\"ptwealth\":8},{\"avg\":7,\"date\":\"2017-07-06\",\"ptlove\":4,\"ptcareer\":4,\"ptwealth\":4},{\"avg\":9,\"date\":\"2017-07-07\",\"ptlove\":7,\"ptcareer\":9,\"ptwealth\":3},{\"avg\":7,\"date\":\"2017-07-08\",\"ptlove\":4,\"ptcareer\":8,\"ptwealth\":9},{\"avg\":4,\"date\":\"2017-07-09\",\"ptlove\":5,\"ptcareer\":6,\"ptwealth\":2},{\"avg\":5,\"date\":\"2017-07-10\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":5,\"date\":\"2017-07-11\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":5,\"date\":\"2017-07-12\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":5,\"date\":\"2017-07-13\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":5,\"date\":\"2017-07-14\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":5,\"date\":\"2017-07-15\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":5,\"date\":\"2017-07-16\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":5,\"date\":\"2017-07-17\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":5,\"date\":\"2017-07-18\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":5,\"date\":\"2017-07-19\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":5,\"date\":\"2017-07-20\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":5,\"date\":\"2017-07-21\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":5,\"date\":\"2017-07-22\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":5,\"date\":\"2017-07-23\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":5,\"date\":\"2017-07-24\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":5,\"date\":\"2017-07-25\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":5,\"date\":\"2017-07-26\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":5,\"date\":\"2017-07-27\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":5,\"date\":\"2017-07-28\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":5,\"date\":\"2017-07-29\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5},{\"avg\":5,\"date\":\"2017-07-30\",\"ptlove\":5,\"ptcareer\":5,\"ptwealth\":5}]},\"status\":200,\"sign\":\"360a71caa7a4086d6b83201ea4444353\"}";

}
