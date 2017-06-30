package com.youloft.lilith.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.common.widgets.picker.CityPicker;
import com.youloft.lilith.cons.view.ConstellationViewFactory;

/**
 * Created by zchao on 2017/6/27.
 * desc: 星座fragment，只是个样子。可自己修改
 * version:
 */

public class XZFragment extends BaseFragment {


    public XZFragment() {
        super(R.layout.fragment_xz);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView img = (ImageView) view.findViewById(R.id.img);

        Bitmap bitmap = ConstellationViewFactory.getInstance().getConsImg("白羊座", "33", "5", "6");
        if (bitmap != null) {
            img.setImageBitmap(bitmap);
        }

    }
}
