package com.youloft.lilith.common.widgets.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;

import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.setting.EditInformationActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 相册选择的dialog
 * <p>
 * Created by GYH on 2017/7/6.
 */

public class PhotoSelectDialog extends BaseDialog {

    private Activity mContext;

    public PhotoSelectDialog(@NonNull Context context) {
        super(context);
        mContext = (Activity) context;
        initView();
    }


    public PhotoSelectDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initView();
    }


    private void initView() {
        setContentView(R.layout.dialog_photo_select);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.take_photo, R.id.photo_select, R.id.cancel, R.id.fl_root})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.take_photo:
                Toaster.showShort("拍照");
                Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                mContext.startActivityForResult(getImageByCamera, EditInformationActivity.CODE_CAMERA);
                break;
            case R.id.photo_select:
                Intent albumIntent = new Intent(Intent.ACTION_PICK);
                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                mContext.startActivityForResult(albumIntent, EditInformationActivity.CODE_PICK_IMAGE);
                dismiss();
                break;
            case R.id.cancel:
                dismiss();
                break;
            case R.id.fl_root:
                dismiss();
                break;
        }
    }


    protected void getImageFromPhone() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        mContext.startActivityForResult(intent, EditInformationActivity.CONTEXT_INCLUDE_CODE);
    }
}
