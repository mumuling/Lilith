package com.youloft.lilith.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.measure.adapter.MeasureAdapter;
import com.youloft.lilith.ui.view.BaseToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zchao on 2017/6/27.
 * desc: 测测fragment，只是个样子。可自己修改
 * version:
 */

public class CCFragment extends BaseFragment {

    @BindView(R.id.btl_CC)
    BaseToolBar btlCC;  //标题栏
    @BindView(R.id.rv_CC)
    RecyclerView rvCC;  //recyclerView
    Unbinder unbinder;

    public CCFragment() {
        super(R.layout.fragment_cc);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rvCC.setLayoutManager(manager);
        rvCC.setAdapter(new MeasureAdapter());
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
