package com.youloft.lilith.info.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.AppConfig;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.common.widgets.dialog.PhotoSelectDialog;
import com.youloft.lilith.common.widgets.picker.CityInfo;
import com.youloft.lilith.common.widgets.picker.CityPicker;
import com.youloft.lilith.common.widgets.picker.DatePickerPop;
import com.youloft.lilith.common.widgets.picker.GenderPickerPop;
import com.youloft.lilith.common.widgets.picker.OnPickerSelectListener;
import com.youloft.lilith.common.widgets.picker.TimePickerPop;
import com.youloft.lilith.info.bean.UpdateUserInfoBean;
import com.youloft.lilith.info.repo.UpdateUserRepo;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.ui.view.BaseToolBar;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
    @BindView(R.id.et_nick_name)
    EditText etNickName;  //下面的可编辑昵称
    private String sex;

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
                //1.确认资料是否都有数据
                //2.对应服务器需要的参数,做一下数据组装
                //3.发起请求修改用户信息
                savaUserInfo();

            }
        });

        //根据数据做对应的初始化
        initView();


        etNickName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etNickName.setCursorVisible(true);
                    etNickName.setSelection(etNickName.length());
                }else {
                    etNickName.setCursorVisible(false);
                }
            }
        });
    }

    /**
     * 根据数据做对应的初始化
     */
    private void initView() {
        if (AppConfig.LOGIN_STATUS) {//处于登录状态
            //获取用户信息,并且展示

            UserBean userInfo = AppSetting.getUserInfo();
            if (userInfo == null) {
                return;
            }
            UserBean.DataBean.UserInfoBean detail = userInfo.data.userInfo;
            GlideApp.with(this).load(detail.headImg).into(ivHeader);
            tvNickName.setText(detail.nickName);
            etNickName.setText(detail.nickName);
            tvSex.setText(detail.sex+"");
            String birthDay = detail.birthDay;
            String[] split = birthDay.split(" ");
            String dateBirth = split[0];
            String timeBirth = split[1];
            tvDateBirth.setText(dateBirth);
            tvTimeBirth.setText(timeBirth);
            tvPlaceBirth.setText(detail.birthPlace);
            tvPlaceNow.setText(detail.livePlace);
        }
    }

    private void savaUserInfo() {
        final String nickName = etNickName.getText().toString();
        final String sexStr = tvSex.getText().toString();

        String dateBirth = tvDateBirth.getText().toString();
        String timeBirth = tvTimeBirth.getText().toString();
        final String placeBirth = tvPlaceBirth.getText().toString();
        final String placeNow = tvPlaceNow.getText().toString();
        if (TextUtils.isEmpty(nickName) || TextUtils.isEmpty(sexStr)
                || TextUtils.isEmpty(dateBirth) || TextUtils.isEmpty(timeBirth)
                || TextUtils.isEmpty(placeBirth) || TextUtils.isEmpty(placeNow)) {
            Toaster.showShort("请完善信息");
            return;
        }
        sex = "1";
        if (sexStr.equals(getResources().getString(R.string.man))) {//女 1   男 2
            sex = String.valueOf(2);
        }
        String userId = String.valueOf(AppSetting.getUserInfo().data.userInfo.id);
        // TODO: 2017/7/9 需要拿到上传头像后的url
        String headImg = "";
        final String time = dateBirth + " " + timeBirth + " :00";
        String birthLongi = "";//出生经度
        String birthLati = "";//出生纬度
        String liveLongi = " ";//现居地经度
        String liveLati = "";//现居地纬度


        UpdateUserRepo.updateUserInfo(userId, nickName, headImg, sex, time, placeBirth, birthLongi, birthLati, placeNow, liveLongi, liveLati)
                .compose(this.<UpdateUserInfoBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<UpdateUserInfoBean>() {
                    @Override
                    public void onDataSuccess(UpdateUserInfoBean updateUserInfoBean) {
                        if (updateUserInfoBean.data.result == 0) {//保存数据成功
                            //把这套数据存一份到本地
                            UserBean userInfo = AppSetting.getUserInfo();
                            UserBean.DataBean.UserInfoBean userInfoDetail = userInfo.data.userInfo;
                            userInfoDetail.nickName = nickName;
                            userInfoDetail.sex = Integer.parseInt(sex);
                            userInfoDetail.birthDay = time;
                            userInfoDetail.birthPlace = placeBirth;
                            userInfoDetail.livePlace = placeNow;
                            AppSetting.saveUserInfo(userInfo);
                            finish();
                        } else {
                            Toaster.showShort("保存数据失败");
                        }

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
                GenderPickerPop.getDefaultDatePicker(this)
                        .setOnSelectListener(new OnPickerSelectListener() {
                            @Override
                            public void onSelected(Object data) {
                                String sex = (String) data;
                                tvSex.setText(sex);
                                deleteTextDrawable(tvSex);
                            }

                            @Override
                            public void onCancel() {

                            }
                        })
                        .show();
                break;
            case R.id.fl_date_birth:
                DatePickerPop.getDefaultDatePicker(this)
                        .setOnSelectListener(new OnPickerSelectListener() {
                            @Override
                            public void onSelected(Object data) {
                                Date date = (Date) data;
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String format = sdf.format(date);
                                tvDateBirth.setText(format);
                                deleteTextDrawable(tvDateBirth);
                            }

                            @Override
                            public void onCancel() {

                            }
                        })
                        .show();
                break;
            case R.id.fl_time_birth:
                TimePickerPop.getDefaultTimePicker(this)
                        .setOnSelectListener(new OnPickerSelectListener() {
                            @Override
                            public void onSelected(Object data) {
                                String time = (String) data;
                                tvTimeBirth.setText(time);
                                deleteTextDrawable(tvTimeBirth);
                            }

                            @Override
                            public void onCancel() {

                            }
                        })
                        .show();
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
     *
     * @param tv
     */
    private void cityPick(final TextView tv) {
        CityPicker.getDefCityPicker(this)
                .setOnCityItemClickListener(new OnPickerSelectListener<CityInfo>() {
                    @Override
                    public void onSelected(CityInfo data) {

                        StringBuilder builder = new StringBuilder("");
                        builder.append(data.pProvice).append(data.pCity).append(data.pDistrict);
                        String content = builder.toString();
                        tv.setText(content);
                        deleteTextDrawable(tv);
                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .show();
    }

    /**
     * 去掉TextView的左边的小图标
     */
    private void deleteTextDrawable(TextView tv) {
        tv.setCompoundDrawables(null, null, null, null);
    }
}
