package com.youloft.lilith.common.widgets.webkit.handle;

import android.text.TextUtils;
import android.widget.ImageView;

import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.widgets.webkit.utils.BitmapUtil;
import com.youloft.lilith.glide.GlideBlurTwoViewTarget;

import java.io.File;

/**
 * Created by Administrator on 2017/7/10.
 */

public class UserFileHandle extends FileHandle {
    ImageView ivHeader;
    ImageView ivBlurBg;

    public UserFileHandle(ImageView ivHeader, ImageView ivBlurBg) {
        super();
        this.ivHeader = ivHeader;
        this.ivBlurBg = ivBlurBg;
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
        String upBit = BitmapUtil.bitmapToString(picturePath, 800);
        GlideApp.with(ivHeader.getContext()).asBitmap().load(new File(picturePath)).into(new GlideBlurTwoViewTarget(ivHeader, ivBlurBg));
        if (onUpLoadListener != null) {
            onUpLoadListener.upLoad(upBit, "jpg");
        }
    }


    private OnUpLoadListener onUpLoadListener;

    public interface OnUpLoadListener {
        void upLoad(String upBit, String nameEx);
    }

    public void setOnUpLoadListener(OnUpLoadListener onUpLoadListener) {
        this.onUpLoadListener = onUpLoadListener;
    }
}
