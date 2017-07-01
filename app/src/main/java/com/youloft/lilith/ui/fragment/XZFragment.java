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
import com.youloft.lilith.cons.bean.LuckData;
import com.youloft.lilith.cons.view.ConstellationViewFactory;
import com.youloft.lilith.cons.view.LuckView;

import java.util.ArrayList;

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

        LuckView luckview = (LuckView) view.findViewById(R.id.luck);
        LuckData luckData = new LuckData();
        luckData.type = 4;
        ArrayList<LuckData.LuckItem> luckItems = new ArrayList<>();
        luckItems.add(new LuckData.LuckItem("30", 9));
        luckItems.add(new LuckData.LuckItem("1", 23));
        luckItems.add(new LuckData.LuckItem("2", 34));
        luckItems.add(new LuckData.LuckItem("3", 44));

        luckItems.add(new LuckData.LuckItem("4", 67));
        luckItems.add(new LuckData.LuckItem("5", 2));
        luckItems.add(new LuckData.LuckItem("6", 45));
        luckItems.add(new LuckData.LuckItem("7", 23));
        luckItems.add(new LuckData.LuckItem("8", 67));
        luckData.data = luckItems;
        luckview.setDate(luckData);

    }
}
