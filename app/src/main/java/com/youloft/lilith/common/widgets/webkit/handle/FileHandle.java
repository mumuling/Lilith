package com.youloft.lilith.common.widgets.webkit.handle;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;
import android.widget.Toast;

import com.youloft.lilith.common.widgets.ActionSheet;
import com.youloft.lilith.common.widgets.webkit.utils.BitmapUtil;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * 文件处理器
 * Created by coder on 2017/6/28.
 */
public class FileHandle extends AbsHandle {

    static int REQ_PHOTOLIBRARY = 17621;
    static int REQ_CAMERA = REQ_PHOTOLIBRARY + 1;

    static int MAX_WIDTH = 640;

    private ActionSheet mPreActionSheet;

    private WebView mWebView;

    @Override
    public void handle(final Activity activity, final WebView webView, String url, String action, String params) {
        if (activity instanceof FragmentActivity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity.isDestroyed()) {
                    mPreActionSheet = null;
                    return;
                }
            }
            if (mPreActionSheet != null && mPreActionSheet.isVisible()) {
                return;
            }
            mPreActionSheet = ActionSheet
                    .createBuilder(activity, ((FragmentActivity) activity).getSupportFragmentManager())
                    .setCancelButtonTitle("取消")
                    .setOtherButtonTitles("拍照", "手机相册")
                    .setCancelableOnTouchOutside(true)
                    .setListener(new ActionSheet.ActionSheetListener() {
                        @Override
                        public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                            mPreActionSheet = null;
                            System.out.println("choose dismiss");
                        }

                        @Override
                        public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                            System.out.println("choose index:" + index);
                            if (index == 0) {
                                handleCamera(activity, webView);
                            } else if (index == 1) {
                                handlePhotoLibrary(activity, webView);
                            }
                        }
                    }).show();
        }
    }

    private Uri photoUri;

    /**
     * 处理摄像头
     *
     * @param activity
     * @param webView
     */
    protected void handleCamera(Activity activity, WebView webView) {
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
            /***
             * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
             * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
             */
            ContentValues values = new ContentValues();
            photoUri = activity.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
            activity.startActivityForResult(intent, REQ_CAMERA);
            this.mWebView = webView;
        } else {
            Toast.makeText(activity, "内存卡不存在", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理相册
     *
     * @param activity
     * @param webView
     */
    protected void handlePhotoLibrary(Activity activity, WebView webView) {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(i, REQ_PHOTOLIBRARY);
        this.mWebView = webView;
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (activity == null) {
            this.mWebView = null;
            return;
        }
        Cursor cursor = null;
        try {
            if ((requestCode == REQ_PHOTOLIBRARY || requestCode == REQ_CAMERA) && resultCode == Activity.RESULT_OK) {
                Uri selectedImage = photoUri;
                if (requestCode == REQ_PHOTOLIBRARY) {
                    if (data == null) {
                        this.mWebView = null;
                        return;
                    }
                    selectedImage = data.getData();
                }
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                cursor = activity.getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    doUploadImage(picturePath);
                }
            }
        } catch (Throwable e) {
            this.mWebView = null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 上传图片到WebView
     *
     * @param picturePath
     */
    protected void doUploadImage(String picturePath) {
        Observable.just(picturePath)
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception {
                        return BitmapUtil.bitmapToString(s, MAX_WIDTH);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        if (mWebView != null) {
                            mWebView.loadUrl(String.format("javascript:filecodecallback('%s')", s));
                        }
                        mWebView = null;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (throwable != null) {
                            throwable.printStackTrace();
                        }
                        mWebView = null;
                    }
                });
    }
}
