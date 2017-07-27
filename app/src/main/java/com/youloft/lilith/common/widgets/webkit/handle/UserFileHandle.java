package com.youloft.lilith.common.widgets.webkit.handle;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;

import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.widgets.webkit.utils.BitmapUtil;
import com.youloft.lilith.glide.GlideBlurTwoViewTarget;
import com.youloft.lilith.info.activity.CropActivity;

import java.io.File;

/**
 * Created by Administrator on 2017/7/10.
 */

public class UserFileHandle extends FileHandle {
    ImageView ivHeader;
    ImageView ivBlurBg;
    Activity mActivity;
    public UserFileHandle(ImageView ivHeader, ImageView ivBlurBg, Activity activity) {
        super();
        this.ivHeader = ivHeader;
        this.ivBlurBg = ivBlurBg;
        mActivity = activity;
    }

    /**
     * 重写 实现图片设置,和文件上传
     *
     * @param picturePath
     */
    @Override
    protected void doUploadImage(String picturePath) {
        if (TextUtils.isEmpty(picturePath) || !new File(picturePath).exists()) {
            return;
        }
        Intent intent = new Intent(mActivity, CropActivity.class);
        intent.putExtra("picturePath",picturePath);
        mActivity.startActivityForResult(intent,20);
//        String upBit = BitmapUtil.bitmapToString(picturePath, 800);
//        GlideApp.with(ivHeader.getContext()).asBitmap().load(new File(picturePath)).into(new GlideBlurTwoViewTarget(ivHeader, ivBlurBg));
//        if (onUpLoadListener != null) {
//            onUpLoadListener.upLoad(upBit, "jpg");
//        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if(resultCode == 20){
            byte[] picBytes = data.getByteArrayExtra("picBytes");
            String upBit = new String(Base64.encode(picBytes, 0)).replaceAll("[\r|\n]", "");
            GlideApp.with(ivHeader.getContext()).asBitmap().load(picBytes).into(new GlideBlurTwoViewTarget(ivHeader, ivBlurBg));
            if (onUpLoadListener != null) {
                onUpLoadListener.upLoad(upBit, "jpg");
            }
            return;
        }
        super.onActivityResult(activity, requestCode, resultCode, data);
    }

    private OnUpLoadListener onUpLoadListener;

    public interface OnUpLoadListener {
        void upLoad(String upBit, String nameEx);
    }

    public void setOnUpLoadListener(OnUpLoadListener onUpLoadListener) {
        this.onUpLoadListener = onUpLoadListener;
    }
}
