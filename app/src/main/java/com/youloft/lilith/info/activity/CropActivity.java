package com.youloft.lilith.info.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.theartofdev.edmodo.cropper.CropImageView;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.widgets.webkit.utils.BitmapUtil;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 图片裁剪
 * Created by GYH on 2017/7/27.
 */

public class CropActivity extends BaseActivity {

    @BindView(R.id.civ_crop)
    CropImageView civCrop;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        ButterKnife.bind(this);
        String picturePath = getIntent().getStringExtra("picturePath");
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
        civCrop.setImageBitmap(bitmap);
    }

    @OnClick({R.id.tv_cancel, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_confirm:
                Bitmap croppedImage = civCrop.getCroppedImage();
                //这里做一下判断,如果用户裁剪过后的图还是很大,那么给他压缩一下
                byte[] bytes = compressBitmap(croppedImage);
                Intent intent = new Intent();
                intent.putExtra("picBytes", bytes);
                setResult(20, intent);
                finish();
                break;
        }
    }

    /**
     * 对bitmap的宽高做判断,如果过高或者过宽 就压缩一把
     *
     * @param croppedImage
     */
    private byte[] compressBitmap(Bitmap croppedImage) {
        int width = croppedImage.getWidth();
        int height = croppedImage.getHeight();
        byte[] bytes = BitmapUtil.bitmapToBytes(croppedImage, Bitmap.CompressFormat.JPEG);
        if (width > 100 || height > 100) {
            int i = height > width ? height : width;
            int j = Math.round(i / 800);
            if (j < 1) {
                j = 1;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = j;
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length, options);
            if(croppedImage != null){
                croppedImage.recycle();
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bytes = bos.toByteArray();
        }
        return bytes;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
