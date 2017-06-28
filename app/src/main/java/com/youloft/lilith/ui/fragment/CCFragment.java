package com.youloft.lilith.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.common.base.GlideApp;
import com.youloft.lilith.common.net.IRequestResult;
import com.youloft.lilith.common.net.OkHttpUtils;
import com.youloft.lilith.common.utils.LogUtils;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by zchao on 2017/6/27.
 * desc: 测测fragment，只是个样子。可自己修改
 * version:
 */

public class CCFragment extends BaseFragment {
    public CCFragment() {
        super(R.layout.fragment_cc);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView img = (ImageView) view.findViewById(R.id.img);
        final TextView tv = (TextView) view.findViewById(R.id.text);
//        Glide.with()
//        GlideApp.with(this);
        GlideApp.with(this).asBitmap()
                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1498627427494&di=3b1ca86fd98e8044541f68a8e5243902&imgtype=0&src=http%3A%2F%2Ftupian.enterdesk.com%2F2015%2Fxu%2F04%2F20%2F12%2Fzweihua11.jpg")
                .into(img);

        String url = "http://apis.baidu.com/showapi_open_bus/pic/pic_search";
        String apikey = "99d5fcb6ca3f46b33431daa2b02dac04";
        String type = "4002";
        String page = "1";
        HashMap<String, String> param = new HashMap<>();
        HashMap<String, String> hparam = new HashMap<>();
        hparam.put("apikey", apikey);
        param.put("type", type);
        param.put("page", page);
        OkHttpUtils.getInstance().requestEnqueue(url, hparam, param, false, String.class, new IRequestResult<String>() {
            @Override
            public void onSuccessful(String result) {
                tv.setText(result);
            }

            @Override
            public void onFailure(String errorMsg) {
                tv.setText(errorMsg);
            }

            @Override
            public void onCompleted() {
                LogUtils.d("aaa", "wanc");
            }
        });
    }
}
