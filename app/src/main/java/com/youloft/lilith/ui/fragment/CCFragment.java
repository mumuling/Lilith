package com.youloft.lilith.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerViewCanPullAble;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.widgets.view.PullToRefreshLayout;
import com.youloft.lilith.measure.MeasureRepo;
import com.youloft.lilith.measure.adapter.MeasureAdapter;
import com.youloft.lilith.measure.bean.MeasureBean;
import com.youloft.lilith.ui.view.BaseToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zchao on 2017/6/27.
 * desc: 测测fragment，只是个样子。可自己修改
 * version:
 */

public class CCFragment extends BaseFragment implements PullToRefreshLayout.OnRefreshListener {
    @Autowired(name = "/repo/measure")
    MeasureRepo measureRepo;

    @BindView(R.id.btl_CC)
    BaseToolBar btlCC;  //标题栏
    @BindView(R.id.rv_CC)
    RecyclerViewCanPullAble rvCC;  //recyclerView
    Unbinder unbinder;
    @BindView(R.id.ptr_CC)
    PullToRefreshLayout ptrCC;
    private MeasureAdapter mMeasureAdapter;

    public CCFragment() {
        super(R.layout.fragment_cc);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        ptrCC.setOnRefreshListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvCC.setLayoutManager(manager);
        mMeasureAdapter = new MeasureAdapter(mContext);
        rvCC.setAdapter(mMeasureAdapter);
        btlCC.setShowShareBtn(false);
        btlCC.setShowBackBtn(false);
        btlCC.setTitle(getResources().getString(R.string.cece));
        getMeasureData();
        return rootView;
    }

    /**
     * 获取测测的数据
     */
    private void getMeasureData() {
        MeasureRepo.getMeasureData()
                .compose(this.<MeasureBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<MeasureBean>() {
                    @Override
                    public void onDataSuccess(MeasureBean measureBean) {
                        mMeasureAdapter.setData(measureBean.data);
                    }

                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
        MeasureRepo.getMeasureData()
                .compose(this.<MeasureBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<MeasureBean>() {
                    @Override
                    public void onDataSuccess(MeasureBean measureBean) {
                        mMeasureAdapter.setData(measureBean.data);
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        mMeasureAdapter.setData(measureBean.data);
                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                    }
                });
    }
}
