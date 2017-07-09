package com.youloft.lilith.setting;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.common.widgets.dialog.PhotoSelectDialog;
import com.youloft.lilith.common.widgets.picker.CityPicker;
import com.youloft.lilith.common.widgets.picker.DatePicker;
import com.youloft.lilith.ui.view.BaseToolBar;

import java.io.FileNotFoundException;
import java.io.InputStream;

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
    public static final int CODE_PICK_IMAGE = 8;//打开相册的状态码
    public static final int CODE_CAMERA = 7;//打开相机

    @BindView(R.id.btl_edit_information)
    BaseToolBar btlEditInformation; //标题栏
    @BindView(R.id.iv_header)
    CircleImageView ivHeader;  //头像
    @BindView(R.id.iv_blur_bg)
    ImageView ivBlurBg;  //背景
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;  //上面的昵称
    @BindView(R.id.tv_sex)
    TextView tvSex;   //性别
    @BindView(R.id.tv_date_birth)
    TextView tvDateBirth;  //出生日期
    @BindView(R.id.tv_time_birth)
    TextView tvTimeBirth;  //出生时间
    @BindView(R.id.tv_place_birth)
    TextView tvPlaceBirth; //出生地点
    @BindView(R.id.tv_place_now)
    TextView tvPlaceNow;   //现居地点

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);
        ButterKnife.bind(this);
        btlEditInformation.setBackgroundColor(Color.TRANSPARENT);
        btlEditInformation.setTitle("编辑资料");
        btlEditInformation.setOnToolBarItemClickListener(new BaseToolBar.OnToolBarItemClickListener() {
            @Override
            public void OnBackBtnClick() {
                onBackPressed();
            }

            @Override
            public void OnTitleBtnClick() {

            }

            @Override
            public void OnShareBtnClick() {
                //1.确认从资料是否都有数据
                //2.对应服务器需要的参数,做一下数据组装
                //3.发起请求修改用户信息
            }
        });
    }

    @OnClick(R.id.iv_header)
    public void onHeaderClicked() {
        PhotoSelectDialog dialog = new PhotoSelectDialog(this);
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = data.getData();
        Bitmap photo = null;
        switch (requestCode) {
            case CODE_CAMERA:
//                if (data.getData() != null || data.getExtras() != null) { //防止没有返回结果
//
//                    if (uri != null) {
//                        photo = BitmapFactory.decodeFile(uri.getPath()); //拿到图片
//                    }
//                    if (photo == null) {
//                        Bundle bundle = data.getExtras();
//                        if (bundle != null) {
//                            photo = (Bitmap) bundle.get("data");
//                        }
//                    }
//                }
            case CODE_PICK_IMAGE:
                ContentResolver resolver = getContentResolver();
                try {
                    InputStream inputStream = resolver.openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    ivHeader.setImageBitmap(bitmap);
                    ivBlurBg.setImageBitmap(ViewUtil.blurBitmap(bitmap));


//                    String realPathFromUri = getRealPathFromUri(this, uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                break;

        }
    }




    @OnClick({R.id.fl_sex, R.id.fl_date_birth, R.id.fl_time_birth, R.id.fl_place_birth, R.id.fl_place_now})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_sex:

                break;
            case R.id.fl_date_birth:
                break;
            case R.id.fl_time_birth:
                break;
            case R.id.fl_place_birth://弹出城市选择器
                cityPick(tvPlaceBirth);
                break;
            case R.id.fl_place_now://弹出城市选择器
                cityPick(tvPlaceNow);
                break;
        }
    }

    /**
     * 城市的选择
     * @param tv
     */
    private void cityPick(final TextView tv) {
        CityPicker.getDefCityPicker(this)
                .setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... citySelected) {
                        StringBuilder builder = new StringBuilder("");
                        for (int i = 0; i < citySelected.length; i++) {
                            builder.append(citySelected[i]);
                        }
                        String content = builder.toString();
                        tv.setText(content);
                        tv.setCompoundDrawables(null,null,null,null);
                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .show();
    }
}
