package com.youloft.lilith.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.cons.bean.LuckData;
import com.youloft.lilith.cons.view.LuckView;

import java.util.ArrayList;

/**
 * Created by zchao on 2017/6/27.
 * desc: 星座fragment，只是个样子。可自己修改
 * version:
 */

public class XZFragment extends BaseFragment {
    public int n = 1;

    public XZFragment() {
        super(R.layout.fragment_xz);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final LuckView luckview1 = (LuckView) view.findViewById(R.id.luck1);
        LuckView luckview2 = (LuckView) view.findViewById(R.id.luck2);
        LuckView luckview3 = (LuckView) view.findViewById(R.id.luck3);
        LuckView luckview4 = (LuckView) view.findViewById(R.id.luck4);

        final LuckData luckData = new LuckData();
        luckData.type = 1;
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
        luckview1.setDate(luckData);

        LuckData luckData2 = new LuckData();
        luckData2.type = 2;
        luckData2.data = (ArrayList<LuckData.LuckItem>) luckItems.clone();
        luckview2.setDate(luckData2);

        LuckData luckData3 = new LuckData();
        luckData3.type = 3;
        luckData3.data = (ArrayList<LuckData.LuckItem>) luckItems.clone();
        luckview3.setDate(luckData3);

        LuckData luckData4 = new LuckData();
        luckData4.type = 4;
        luckData4.data = (ArrayList<LuckData.LuckItem>) luckItems.clone();
        luckview4.setDate(luckData4);
        luckview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n++;
                n %= 5;
                n = n == 0 ? 1 : n;
                luckData.type = (n);
                luckview1.setDate(luckData);
            }
        });
    }
}
