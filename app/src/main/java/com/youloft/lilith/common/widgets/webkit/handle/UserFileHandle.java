package com.youloft.lilith.common.widgets.webkit.handle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import com.youloft.lilith.common.utils.ViewUtil;

import java.io.ByteArrayOutputStream;

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
        String upBit = bitmapToString(picturePath);
        Bitmap bitmap = getSmallBitmap(picturePath);
        ivHeader.setImageBitmap(bitmap);
        ivBlurBg.setImageBitmap(ViewUtil.blurBitmap(bitmap));
        if (onUpLoadListener != null) {
            onUpLoadListener.upLoad(upBit,"jpg");
        }
    }


    /**
     * 把bitmap转换成String
     *
     * @param filePath
     * @return
     */
    public String bitmapToString(String filePath) {

        Bitmap bm = getSmallBitmap(filePath);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    /**
     * 根据路径获得图片并压缩返回bitmap用于显示
     *
     * @param filePath
     * @return
     */
    public Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, ivHeader.getWidth(), ivHeader.getWidth());
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }



    private OnUpLoadListener onUpLoadListener;

    public interface OnUpLoadListener {
        void upLoad(String upBit,String nameEx);
    }

    public void setOnUpLoadListener(OnUpLoadListener onUpLoadListener) {
        this.onUpLoadListener = onUpLoadListener;
    }
}
