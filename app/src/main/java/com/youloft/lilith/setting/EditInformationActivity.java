package com.youloft.lilith.setting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.widgets.dialog.PhotoSelectDialog;
import com.youloft.lilith.ui.view.BaseToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 编辑资料界面
 * <p>
 * Created by GYH on 2017/7/3.
 */
@Route(path = "/test/EditInformationActivity")
public class EditInformationActivity extends BaseActivity {
    public static final int CODE_PICK_IMAGE = 80001;

    @BindView(R.id.btl_edit_information)
    BaseToolBar btlEditInformation;
    @BindView(R.id.iv_header)
    CircleImageView ivHeader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);
        ButterKnife.bind(this);
        btlEditInformation.setBackgroundColor(Color.TRANSPARENT);
        btlEditInformation.setTitle("编辑资料");
    }

    @OnClick(R.id.iv_header)
    public void onHeaderClicked() {
        PhotoSelectDialog dialog = new PhotoSelectDialog(this);
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
