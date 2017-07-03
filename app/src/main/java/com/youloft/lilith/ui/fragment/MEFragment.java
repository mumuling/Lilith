package com.youloft.lilith.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.login.LoginActivity;
import com.youloft.lilith.router.AppRouter;
import com.youloft.lilith.setting.EditInformationActivity;
import com.youloft.lilith.setting.SettingActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zchao on 2017/6/27.
 * desc: 我fragment，只是个样子。可自己修改
 * version:
 */

public class MEFragment extends BaseFragment {

    @BindView(R.id.btn)
    Button btn;
    Unbinder unbinder;
    @BindView(R.id.iv_constellation)
    ImageView ivConstellation; //用户星座
    @BindView(R.id.iv_header)
    ImageView ivHeader; //用户头像
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;  //用户昵称
    @BindView(R.id.iv_rise)
    ImageView ivRise; //上升星座
    @BindView(R.id.iv_sun)
    CircleImageView ivSun; //太阳星座
    @BindView(R.id.iv_moon)
    CircleImageView ivMoon; //月亮星座


    public MEFragment() {
        super(R.layout.fragment_me);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        return rootView;
    }


    //话题  资料  设置  的点击事件
    @OnClick({R.id.rl_topic,R.id.rl_personal_data,R.id.rl_setting})
    public void clickMyItem(View view){
        switch (view.getId()) {
            case R.id.rl_topic:
                Toast.makeText(getActivity(),"话题",Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_personal_data:
                ARouter.getInstance().build("/test/EditInformationActivity").navigation();
                break;
            case R.id.rl_setting:
                ARouter.getInstance().build("/test/SettingActivity").navigation();
                break;

        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
