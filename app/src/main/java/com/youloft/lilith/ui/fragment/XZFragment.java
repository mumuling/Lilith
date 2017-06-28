package com.youloft.lilith.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.common.utils.LogUtils;
import com.youloft.lilith.common.widgets.picker.TimePicker;

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
        TextView viewById = (TextView) view.findViewById(R.id.text);
        final TimePicker timeP = (TimePicker) view.findViewById(R.id.time);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeP.mHourDay == TimePicker.HOUR_24) {
                    timeP.setTimeRange(TimePicker.HOUR_12);
                } else {
                    timeP.setTimeRange(TimePicker.HOUR_24);
                }
            }
        });
        timeP.setDateChangedListener(new TimePicker.onTimeChangedListener() {
            @Override
            public void onDateChanged(int hour, int min, int amOrpm) {
                LogUtils.e("aaa", hour + ":"  + min +" " + amOrpm);
            }
        });
    }
}
