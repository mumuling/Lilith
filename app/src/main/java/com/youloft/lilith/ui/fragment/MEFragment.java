package com.youloft.lilith.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zchao on 2017/6/27.
 * desc: 我fragment，只是个样子。可自己修改
 * version:
 */

public class MEFragment extends BaseFragment {



    Unbinder unbinder;

    public MEFragment() {
        super(R.layout.fragment_me);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
