package com.youloft.lilith.setting;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.widgets.picker.CityPicker;

/**
 * 编辑资料界面
 * <p>
 * Created by GYH on 2017/7/3.
 */
@Route(path = "/test/EditInformationActivity")
public class EditInformationActivity extends BaseActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);


    }
}
