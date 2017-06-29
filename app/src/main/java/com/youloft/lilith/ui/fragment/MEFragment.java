package com.youloft.lilith.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zchao on 2017/6/27.
 * desc: 我fragment，只是个样子。可自己修改
 * version:
 */

public class MEFragment extends BaseFragment {
    @BindView(R.id.iv_header)
    CircleImageView ivHeader;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;
    @BindView(R.id.tv_password)
    TextView tvPassword;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.ll_phone_number)
    LinearLayout llPhoneNumber;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.ll_password)
    LinearLayout llPassword;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;

    Unbinder unbinder;

    public MEFragment() {
        super(R.layout.fragment_me);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        initAction();
        return rootView;
    }

    private void initAction() {
        //点击手机号码 隐藏对应的textview  显示edittext
        llPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPhoneNumber.setVisibility(View.INVISIBLE);
                etPhoneNumber.setVisibility(View.VISIBLE);
            }
        });





        //点击密码项 隐藏对应的textview  显示edittext
        llPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPassword.setVisibility(View.INVISIBLE);
                etPassword.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
