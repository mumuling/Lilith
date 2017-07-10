package com.youloft.lilith.common.widgets.webkit.handle;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.common.widgets.webkit.utils.BitmapUtil;

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
        String upBit = BitmapUtil.bitmapToString(picturePath, ivHeader.getWidth());
        Bitmap bitmap = BitmapUtil.readZoomImage(picturePath, ivHeader.getWidth());
        ivHeader.setImageBitmap(bitmap);
        ivBlurBg.setImageBitmap(ViewUtil.blurBitmap(bitmap));
        if (onUpLoadListener != null) {
            onUpLoadListener.upLoad(upBit,"jpg");
        }
    }

    private OnUpLoadListener onUpLoadListener;

    public interface OnUpLoadListener {
        void upLoad(String upBit,String nameEx);
    }

    public void setOnUpLoadListener(OnUpLoadListener onUpLoadListener) {
        this.onUpLoadListener = onUpLoadListener;
    }
}
