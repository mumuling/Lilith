package com.youloft.lilith.info.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.CalendarHelper;
import com.youloft.lilith.common.utils.LoginUtils;
import com.youloft.lilith.common.utils.StringUtil;
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
    @BindView(R.id.iv_tips_sex)
    ImageView ivTipsSex;  //性别前面的感叹号
    @BindView(R.id.iv_tips_date)
    ImageView ivTipsDate; //日期前面的感叹号
    @BindView(R.id.iv_tips_time)
    ImageView ivTipsTime; //时间前面的感叹号
    @BindView(R.id.iv_tips_birth_place)
    ImageView ivTipsBirthPlace; //出生地前面的感叹号
    @BindView(R.id.iv_tips_now)
    ImageView ivTipsNow;   //现居地前面的感叹号
    @BindView(R.id.iv_delete_nick_name)
    ImageView ivDeleteNickName; //清空昵称的按钮
    @BindView(R.id.sv_scroller)
    ScrollView svScroller; //滚动控件

    private String birthLongi = "";//出生经度
    private String birthLati = "";//出生纬度
    private String liveLongi = "";//现居地经度
    private String liveLati = "";//现居地纬度

    private String sex;
    private UserFileHandle fileHandle;//文件选择处理器

    private boolean canSave = false;
    private String mName;//姓名
    private String mSex;//性别
    private String mBirthDay;//出生日期
    private String mBirthTime;//出生时间
    private String mBirthLocation;//出生地
    private String mHomeLocation;//现居地

    private TextWatcher textWatcher;
    private String mTempContent;//edittext失去焦点的时候临时存储文字
    private String mNickName;

    private boolean needSave = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);
        ButterKnife.bind(this);
        //初始化标题栏的显示,和监听
        initTitle();

        //根据数据做对应的初始化
        initView();
        svScroller.post(new Runnable() {
            @Override
            public void run() {
                svScroller.fullScroll(ScrollView.FOCUS_UP);
            }
        });
        //失去焦点隐藏光标,获得焦点显示光标
        etNickName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etNickName.setCursorVisible(true);
                    ivDeleteNickName.setVisibility(View.VISIBLE);
                    etNickName.setCursorVisible(true);
                    ivDeleteNickName.setVisibility(View.VISIBLE);
                    etNickName.setText(mTempContent);
                    needSave = true;
                    etNickName.setSelection(mTempContent.length());

                } else {
                    etNickName.setCursorVisible(false);
                    ivDeleteNickName.setVisibility(View.INVISIBLE);

                }
            }
        });
        etNickName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
    }


    /**
     * 对edittext文字的处理
     */
    private void handleEditText() {
        needSave = false;
        String con = etNickName.getText().toString();
        int measuredWidth = etNickName.getMeasuredWidth();
        TextPaint paint = etNickName.getPaint();
        int textWidth = (int) paint.measureText(mTempContent);
        if(textWidth > measuredWidth){
            while (textWidth > measuredWidth) {
                con = con.substring(0,con.length()-1);
                textWidth = (int) paint.measureText(con);
            }
            etNickName.setText(con.substring(0,con.length()-2)+"...");
        }

    }

    private void handleEditText1() {
        String con = AppSetting.getUserInfo().data.userInfo.nickName;
        mTempContent =con;
        int measuredWidth = etNickName.getMeasuredWidth();
        TextPaint paint = etNickName.getPaint();
        int textWidth = (int) paint.measureText(con);
        if(textWidth > measuredWidth && con.length() > 2){
            while (textWidth > measuredWidth) {
                con = con.substring(0,con.length()-1);
                textWidth = (int) paint.measureText(con);
            }
            etNickName.setText(con.substring(0,con.length()-2)+"...");
        }


    }

    /**
     * 初始化Title相关
     */
    private void initTitle() {
        btlEditInformation.setBackgroundColor(Color.TRANSPARENT);
        btlEditInformation.setTitle(getResources().getString(R.string.edit_info));
        btlEditInformation.setShowShareBtn(false);
        btlEditInformation.setShowSaveBtn(false);
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
                if (canSave) {
                    savaUserInfo();
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

            UserBean.DataBean.UserInfoBean detail = userInfo.data.userInfo;
            //头像的初始化相关,如果有头像显示头像,如果没有显示默认头像
            if (!TextUtils.isEmpty(detail.headImg)) {
                GlideApp.with(this).asBitmap().dontAnimate().load(detail.headImg).into(new GlideBlurTwoViewTarget(ivHeader, ivBlurBg));
            } else {
                ivHeader.setImageResource(R.drawable.default_user_head_img);
            }

            mName = detail.nickName;
            tvNickName.setText(StringUtil.toNameString(mName));
            etNickName.setText(detail.nickName);


            //性别
            if (detail.sex == 2) {
                tvSex.setText(R.string.man);
            } else {
                tvSex.setText(R.string.woman);
            }
            mSex = tvSex.getText().toString();

            tvSex.setTextColor(getResources().getColor(R.color.white));
            ivTipsSex.setVisibility(View.GONE);

            //时间日期
            String birthDay = detail.birthDay;
            Date date = CalendarHelper.parseDate(birthDay, DATE_FORMAT);
            mCal.setTime(date);
            tvDateBirth.setText(CalendarHelper.format(mCal.getTime(), "yyyy-MM-dd"));
            tvDateBirth.setTextColor(getResources().getColor(R.color.white));
            mBirthDay = tvDateBirth.getText().toString();
            ivTipsDate.setVisibility(View.GONE);

            tvTimeBirth.setText(CalendarHelper.format(mCal.getTime(), "HH:mm"));
            tvTimeBirth.setTextColor(getResources().getColor(R.color.white));
            mBirthTime = tvTimeBirth.getText().toString();
            ivTipsTime.setVisibility(View.GONE);

            if (TextUtils.isEmpty(detail.birthLongi)) { //资料没完善过
                tvPlaceBirth.setText(R.string.select_place_birth);
                tvPlaceBirth.setTextColor(getResources().getColor(R.color.white_50));
                ivTipsBirthPlace.setVisibility(View.VISIBLE);


                tvPlaceNow.setText(R.string.select_place_now);
                tvPlaceNow.setTextColor(getResources().getColor(R.color.white_50));
                ivTipsNow.setVisibility(View.VISIBLE);

            } else { //已经有完整的资料了
                //需要给四个经纬度赋值
                birthLongi = detail.birthLongi;//出生经度
                birthLati = detail.birthLati;//出生纬度
                liveLongi = detail.liveLongi;//现居地经度
                liveLati = detail.liveLati;//现居地纬度


                tvPlaceBirth.setText(detail.birthPlace);
                tvPlaceBirth.setTextColor(getResources().getColor(R.color.white));
                ivTipsBirthPlace.setVisibility(View.GONE);
                mBirthLocation = tvPlaceBirth.getText().toString();

                tvPlaceNow.setText(detail.livePlace);
                tvPlaceNow.setTextColor(getResources().getColor(R.color.white));
                ivTipsNow.setVisibility(View.GONE);
                mHomeLocation = tvPlaceNow.getText().toString();

            }
            final TextWatcher etNameWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (needSave) {
                        mTempContent = s.toString();
                    }
                    if (!canSave  && needSave) {
                        canSave = true;
                        btlEditInformation.setShowSaveBtn(true);
                    }
                }
            };
            etNickName.post(new Runnable() {
                @Override
                public void run() {
                    handleEditText1();
                    etNickName.addTextChangedListener(etNameWatcher);
                }
            });
            textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!canSave ) {
                        canSave = true;
                        btlEditInformation.setShowSaveBtn(true);
                    }
                }
            };


            tvSex.addTextChangedListener(textWatcher);
            tvDateBirth.addTextChangedListener(textWatcher);
            tvTimeBirth.addTextChangedListener(textWatcher);
            tvPlaceBirth.addTextChangedListener(textWatcher);
            tvPlaceNow.addTextChangedListener(textWatcher);


        }





    }


    /**
     * 保存用户信息的网络请求
     */
    private void savaUserInfo() {
        //这里输入框的内容有可能为  昵称加上...
        //这时候真实的内容可能在 mTempContent 里面
        if(TextUtils.isEmpty(mTempContent)){
            mNickName = etNickName.getText().toString();
        }else {
            mNickName = mTempContent;
        }
        final String sexStr = tvSex.getText().toString();
        String dateBirth = tvDateBirth.getText().toString();
        String timeBirth = tvTimeBirth.getText().toString();
        final String placeBirth = tvPlaceBirth.getText().toString();
        final String placeNow = tvPlaceNow.getText().toString();
        if (TextUtils.isEmpty(mNickName) || TextUtils.isEmpty(sexStr)
                || TextUtils.isEmpty(dateBirth) || TextUtils.isEmpty(timeBirth)
                || TextUtils.isEmpty(placeBirth) || TextUtils.isEmpty(placeNow)) {
            Toaster.showShort("请完善信息");
            return;
        }

        if (mNickName.trim().length() > 20) {//昵称长度不能超过20个
            Toaster.showShort("昵称过长");
            return;
        }
        //当这些字符串为默认字符串的时候 不让修改
        if (sexStr.equals(getResources().getString(R.string.select_sex))
                || dateBirth.equals(getResources().getString(R.string.select_date_birth))
                || timeBirth.equals(getResources().getString(R.string.select_time_birth))
                || placeBirth.equals(getResources().getString(R.string.select_place_birth))
                || placeNow.equals(getResources().getString(R.string.select_place_now))) {
            Toaster.showShort("请完善信息");
            return;
        }

        if (mNickName.equals(mName)
                && mBirthDay.equals(dateBirth)
                && mBirthTime.equals(timeBirth)
                && placeBirth.equals(mBirthLocation)
                && mSex.equals(sexStr)
                && placeNow.equals(mHomeLocation)) {

            Toaster.showShort("个人资料未变化");
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

        UpdateUserRepo.updateUserInfo(userId, mNickName, headImg, sex, time, placeBirth, birthLongi, birthLati, placeNow, liveLongi, liveLati)
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
        ivDeleteNickName.setVisibility(View.INVISIBLE);
        handleEditText();
        switch (view.getId()) {
            case R.id.fl_sex:    //弹出性别选择器
                if (!LoginUtils.canClick()) {
                    return;
                }
                showGenderPicker();
                break;
            case R.id.fl_date_birth://弹出日期选择器
                if (!LoginUtils.canClick()) {
                    return;
                }
                showDatePicker();
                break;
            case R.id.fl_time_birth://弹出时间选择器
                if (!LoginUtils.canClick()) {
                    return;
                }
                showTimePicker();
                break;
            case R.id.fl_place_birth://弹出城市选择器
                if (!LoginUtils.canClick()) {
                    return;
                }
                cityPick(tvPlaceBirth, true);
                break;
            case R.id.fl_place_now://弹出城市选择器
                if (!LoginUtils.canClick()) {
                    return;
                }
                cityPick(tvPlaceNow, false);
                break;
        }
    }

    /**
     * 弹出时间选择器
     */
    private void showTimePicker() {
        TimePickerPop.getDefaultTimePicker(this)
                .setDate(mCal.getTime())
                .setOnSelectListener(new OnPickerSelectListener<GregorianCalendar>() {
                    @Override
                    public void onSelected(GregorianCalendar data) {
                        mCal.set(Calendar.HOUR_OF_DAY, data.get(Calendar.HOUR_OF_DAY));
                        mCal.set(Calendar.MINUTE, data.get(Calendar.MINUTE));
                        tvTimeBirth.setText(CalendarHelper.format(mCal.getTime(), "HH:mm"));
                        tvTimeBirth.setTextColor(getResources().getColor(R.color.white));
                        ivTipsTime.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .show();
    }

    /**
     * 弹出日期选择器
     */
    private void showDatePicker() {
        DatePickerPop.getDefaultDatePicker(this)
                .setDate(mCal)
                .setOnSelectListener(new OnPickerSelectListener<GregorianCalendar>() {
                    @Override
                    public void onSelected(GregorianCalendar data) {
                        mCal.set(Calendar.YEAR, data.get(Calendar.YEAR));
                        mCal.set(Calendar.MONTH, data.get(Calendar.MONTH));
                        mCal.set(Calendar.DAY_OF_MONTH, data.get(Calendar.DAY_OF_MONTH));
                        tvDateBirth.setText(CalendarHelper.format(mCal.getTime(), "yyyy-MM-dd"));
                        tvDateBirth.setTextColor(getResources().getColor(R.color.white));
                        ivTipsDate.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .show();
    }

    /**
     * 弹出性别选择器
     */
    private void showGenderPicker() {

        GenderPickerPop picker;//这里需要判断一下 picker的初始化值
        if (AppSetting.getUserInfo() == null) {
            picker = GenderPickerPop.getDefaultGenderPicker(this);
        } else {
            int sex = AppSetting.getUserInfo().data.userInfo.sex;
            String gender;
            if (sex == 2) {//男
                gender = "男";
            } else {//女
                gender = "女";
            }
            picker = GenderPickerPop.getDefaultGenderPicker(this)
                    .setGender(gender);
        }

        picker
                .setOnSelectListener(new OnPickerSelectListener() {
                    @Override
                    public void onSelected(Object data) {
                        String sex = (String) data;
                        tvSex.setText(sex);
                        tvSex.setTextColor(getResources().getColor(R.color.white));
                        ivTipsSex.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .show();
    }

    /**
     * 城市的选择
     *
     * @param tv
     * @param b  来源  true代表出生地   false 现居地
     */
    private void cityPick(final TextView tv, final boolean b) {

        CityPickerPop cityPicker = getCityPicker(b);

        cityPicker
                .setOnCityItemClickListener(new OnPickerSelectListener<CityInfo>() {
                    @Override
                    public void onSelected(CityInfo data) {
                        StringBuilder builder = new StringBuilder("");
                        builder.append(data.pProvice).append("-").append(data.pCity).append("-").append(data.pDistrict);
                        String content = builder.toString();
                        tv.setText(content);
                        if (b) {
                            birthLongi = data.pLongitude;
                            birthLati = data.pLatitude;
                            ivTipsBirthPlace.setVisibility(View.GONE);
                        } else {
                            liveLongi = data.pLongitude;
                            liveLati = data.pLatitude;
                            ivTipsNow.setVisibility(View.GONE);
                        }
                        tv.setTextColor(getResources().getColor(R.color.white));
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
            return getCityPicker2(content);

        } else {
            String content = tvPlaceNow.getText().toString();
            return getCityPicker2(content);

        }
    }

    private CityPickerPop getCityPicker2(String content) {
        if (!TextUtils.isEmpty(content) && content.contains("-")) {
            String[] split = content.split("-");
            return CityPickerPop.getDefCityPicker(this).province(split[0]).city(split[1]).district(split[2]);
        } else {
            return CityPickerPop.getDefCityPicker(this);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick(R.id.et_nick_name)
    public void onViewClicked() {
        etNickName.setCursorVisible(true);
        ivDeleteNickName.setVisibility(View.VISIBLE);
        etNickName.setText(mTempContent);
        needSave = true;
        etNickName.setSelection(mTempContent.length());
    }

    @OnClick(R.id.iv_delete_nick_name)
    public void onDeleteNickClicked() {
        etNickName.setText(null);
    }

    @OnClick(R.id.ll_nick_name)
    public void onLLNIckClicked() {
        ivDeleteNickName.setVisibility(View.VISIBLE);
    }
}
