package com.youloft.lilith.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.common.widgets.picker.CityPicker;

/**
 * Created by zchao on 2017/6/27.
 * desc: 星座fragment，只是个样子。可自己修改
 * version:
 */

public class XZFragment extends BaseFragment {

    private CityPicker cityPicker;

    public XZFragment() {
        super(R.layout.fragment_xz);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Button viewById = (Button) view.findViewById(R.id.text);
        Button show = (Button) view.findViewById(R.id.btn1);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityPicker cp = CityPicker.getDefCityPicker(getContext());
                cp.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... citySelected) {
                        String s = "";
                        for (int i = 0; i < citySelected.length; i++) {
                            s += citySelected[i];
                        }
                        viewById.setText(s);
                    }

                    @Override
                    public void onCancel() {

                    }
                }).show();


            }
        });



    }
}
