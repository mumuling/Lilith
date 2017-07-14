package com.youloft.lilith.info.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.CalendarHelper;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.common.widgets.picker.CityInfo;
import com.youloft.lilith.common.widgets.picker.CityPickerPop;
import com.youloft.lilith.common.widgets.picker.DatePickerPop;
import com.youloft.lilith.common.widgets.picker.GenderPickerPop;
import com.youloft.lilith.common.widgets.picker.OnPickerSelectListener;
import com.youloft.lilith.common.widgets.picker.TimePickerPop;
import com.youloft.lilith.common.widgets.webkit.handle.UserFileHandle;
import com.youloft.lilith.glide.GlideBlurTwoViewTarget;
import com.youloft.lilith.info.bean.UpLoadHeaderBean;
import com.youloft.lilith.info.event.UserInfoUpDateEvent;
import com.youloft.lilith.info.repo.UpdateUserRepo;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.ui.view.BaseToolBar;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 编辑资料界面
 * <p>
 * Created by GYH on 2017/7/3.
 */
@Route(path = "/test/EditInformationActivity")
public class EditInformationActivity extends BaseActivity {

    GregorianCalendar mCal = new GregorianCalendar();
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    @BindView(R.id.btl_edit_information)
    BaseToolBar btlEditInformation; //标题栏
    @BindView(R.id.iv_header)
    ImageView ivHeader;  //头像
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

    private String birthLongi = "";//出生经度
    private String birthLati = "";//出生纬度
    private String liveLongi = "";//现居地经度
    private String liveLati = "";//现居地纬度

    private String sex;
    private UserFileHandle fileHandle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);
        ButterKnife.bind(this);

        btlEditInformation.setBackgroundColor(Color.TRANSPARENT);
        btlEditInformation.setTitle("编辑资料");
        btlEditInformation.setShowShareBtn(false);
        btlEditInformation.setShowSaveBtn(true);
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

            }

            @Override
            public void OnSaveBtnClick() {
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
                    etNickName.setCursorVisible(true);
                } else {
                    etNickName.setCursorVisible(false);
                    etNickName.setCursorVisible(false);
                }
            }
        });
    }

    /**
     * 根据数据做对应的初始化
     */
    private void initView() {

        UserBean userInfo = AppSetting.getUserInfo();
        if (userInfo != null) {//处于登录状态
            //获取用户信息,并且展示
            if (userInfo == null || userInfo.data == null || userInfo.data.userInfo == null) {
                return;
            }
            UserBean.DataBean.UserInfoBean detail = userInfo.data.userInfo;
            if (!TextUtils.isEmpty(detail.headImg)) {
                GlideApp.with(this).asBitmap().dontAnimate().load(detail.headImg).into(new GlideBlurTwoViewTarget(ivHeader, ivBlurBg));
            }else {
                ivHeader.setImageResource(R.drawable.default_user_head_img);
            }

            tvNickName.setText(detail.nickName);
            etNickName.setText(detail.nickName);

            if (detail.sex == 2) {
                tvSex.setText(R.string.man);
            } else {
                tvSex.setText(R.string.woman);
            }

            String birthDay = detail.birthDay;
            Date date = CalendarHelper.parseDate(birthDay, DATE_FORMAT);
            mCal.setTime(date);
            tvDateBirth.setText(CalendarHelper.format(mCal.getTime(), "yyyy-MM-dd"));
            tvTimeBirth.setText(CalendarHelper.format(mCal.getTime(), "HH:mm"));
            tvPlaceBirth.setText(detail.birthPlace);
            tvPlaceNow.setText(detail.livePlace);

            //去除感叹号
            deleteTextDrawable(tvSex);
            deleteTextDrawable(tvDateBirth);
            deleteTextDrawable(tvTimeBirth);
            deleteTextDrawable(tvPlaceBirth);
            deleteTextDrawable(tvPlaceNow);
        }
    }

    /**
     * 保存用户信息的网络请求
     */
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
        if (nickName.length() > 7) {//昵称长度不能超过7个
            Toaster.showShort("昵称过长");
            return;
        }
        sex = "1";
        if (sexStr.equals(getResources().getString(R.string.man))) {//女 1   男 2
            sex = String.valueOf(2);
        }
        UserBean userInfo = AppSetting.getUserInfo();
        if (userInfo == null) {
            Toaster.showShort("信息修改失败");
            return;
        }

        String userId = String.valueOf(userInfo.data.userInfo.id);
        String headImg = userInfo.data.userInfo.headImg;
        final String time = CalendarHelper.format(mCal.getTime(), DATE_FORMAT);

        UpdateUserRepo.updateUserInfo(userId, nickName, headImg, sex, time, placeBirth, birthLongi, birthLati, placeNow, liveLongi, liveLati)
                .compose(this.<UserBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<UserBean>() {
                    @Override
                    public void onDataSuccess(UserBean userBean) {
                        if (userBean.data.result == 0) {//保存数据成功
                            //把这套数据存一份到本地
                            AppSetting.saveUserInfo(userBean);
                            EventBus.getDefault().post(new UserInfoUpDateEvent());
                            Toaster.showShort("资料保存成功");
                            finish();
                        } else {
                            Toaster.showShort("资料保存失败");
                        }

                    }

                    @Override
                    protected void onFailed(Throwable e) {
                        super.onFailed(e);
                        Toaster.showShort("网络错误");
                    }
                });
    }

    //头像的点击事件
    @OnClick(R.id.iv_header)
    public void onHeaderClicked() {
        fileHandle = new UserFileHandle(ivHeader, ivBlurBg);
        fileHandle.handle(this, null, null, null, null);
        fileHandle.setOnUpLoadListener(new UserFileHandle.OnUpLoadListener() {
            @Override
            public void upLoad(String upBit, String nameEx) {
                updateUserImg(upBit, nameEx);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        fileHandle.onActivityResult(this, requestCode, resultCode, data);
    }

    /**
     * 上传图片的请求
     *
     * @param upBit  图片的base64
     * @param nameEx 后缀名
     */
    private void updateUserImg(String upBit, String nameEx) {
        final UserBean userInfo = AppSetting.getUserInfo();
        if (userInfo == null) {
            return;
        }
        UpdateUserRepo.updateImg(upBit, nameEx, String.valueOf(userInfo.data.userInfo.id))
                .compose(this.<UpLoadHeaderBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<UpLoadHeaderBean>() {

                    @Override
                    public void onDataSuccess(UpLoadHeaderBean upLoadHeaderBean) {
                        userInfo.data.userInfo.headImg = upLoadHeaderBean.data;
                        AppSetting.saveUserInfo(userInfo);
                        EventBus.getDefault().post(new UserInfoUpDateEvent());
                    }

                });
    }

    private static final String TAG = "EditInformationActivity";


    @OnClick({R.id.fl_sex, R.id.fl_date_birth, R.id.fl_time_birth, R.id.fl_place_birth, R.id.fl_place_now})
    public void onViewClicked(View view) {
        etNickName.setCursorVisible(false);
        switch (view.getId()) {
            case R.id.fl_sex:
                int sex = AppSetting.getUserInfo().data.userInfo.sex;
                String gender;
                if (sex == 2) {//男
                    gender = "男";
                } else {//女
                    gender = "女";
                }
                GenderPickerPop.getDefaultGenderPicker(this)
                        .setGender(gender) //这儿设置默认显示
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
                        .setDate(mCal)
                        .setOnSelectListener(new OnPickerSelectListener<GregorianCalendar>() {
                            @Override
                            public void onSelected(GregorianCalendar data) {
                                mCal.set(Calendar.YEAR, data.get(Calendar.YEAR));
                                mCal.set(Calendar.MONTH, data.get(Calendar.MONTH));
                                mCal.set(Calendar.DAY_OF_MONTH, data.get(Calendar.DAY_OF_MONTH));
                                tvDateBirth.setText(CalendarHelper.format(mCal.getTime(), "yyyy-MM-dd"));
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
                        .setDate(mCal.getTime())
                        .setOnSelectListener(new OnPickerSelectListener<GregorianCalendar>() {
                            @Override
                            public void onSelected(GregorianCalendar data) {
                                mCal.set(Calendar.HOUR_OF_DAY, data.get(Calendar.HOUR_OF_DAY));
                                mCal.set(Calendar.MINUTE, data.get(Calendar.MINUTE));
                                tvTimeBirth.setText(CalendarHelper.format(mCal.getTime(), "HH:mm"));
                                deleteTextDrawable(tvTimeBirth);
                            }

                            @Override
                            public void onCancel() {

                            }
                        })
                        .show();
                break;
            case R.id.fl_place_birth://弹出城市选择器
                cityPick(tvPlaceBirth, true);
                break;
            case R.id.fl_place_now://弹出城市选择器
                cityPick(tvPlaceNow, false);
                break;
        }
    }

    /**
     * 城市的选择
     *
     * @param tv
     * @param b  来源  true代表出生地   false 现居地
     */
    private void cityPick(final TextView tv, final boolean b) {

        CityPickerPop cityPickerPop = getCityPicker(b);

        cityPickerPop
                .setOnCityItemClickListener(new OnPickerSelectListener<CityInfo>() {
                    @Override
                    public void onSelected(CityInfo data) {
                        StringBuilder builder = new StringBuilder("");
                        builder.append(data.pProvice).append("-").append(data.pCity).append("-").append(data.pDistrict);
                        String content = builder.toString();
                        tv.setText(content);
                        deleteTextDrawable(tv);
                        if (b) {
                            birthLongi = data.pLongitude;
                            birthLati = data.pLatitude;
                        } else {
                            liveLongi = data.pLongitude;
                            liveLati = data.pLatitude;
                        }

                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .show();
    }

    /**
     * 根据不同的来源获取不同的city picker
     *
     * @param b 来源  true代表出生地   false 现居地
     */
    private CityPickerPop getCityPicker(boolean b) {
        if (b) {

            String content = tvPlaceBirth.getText().toString();
            if (TextUtils.isEmpty(content)) {
                return CityPickerPop.getDefCityPicker(this);
            } else {
                String[] split = content.split("-");
                return CityPickerPop.getDefCityPicker(this).province(split[0]).city(split[1]).district(split[2]);
            }

        } else {
            String content = tvPlaceNow.getText().toString();
            if (TextUtils.isEmpty(content)) {
                return CityPickerPop.getDefCityPicker(this);
            } else {
                String[] split = content.split("-");
                return CityPickerPop.getDefCityPicker(this).province(split[0]).city(split[1]).district(split[2]);
            }

        }
    }

    /**
     * 去掉TextView的左边的小图标
     */
    private void deleteTextDrawable(TextView tv) {
        tv.setCompoundDrawables(null, null, null, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick(R.id.et_nick_name)
    public void onViewClicked() {
        etNickName.setCursorVisible(true);
    }
}
