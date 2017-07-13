package com.youloft.lilith.info.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.info.bean.OldPasswordBean;
import com.youloft.lilith.info.repo.UpdateUserRepo;
import com.youloft.lilith.login.bean.ModifyPasswordBean;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 修改密码界面
 * <p>
 * Created by GYH on 2017/7/5.
 */
@Route(path = "/test/ModifyPasswordActivity")
public class ModifyPasswordActivity extends BaseActivity {


    @BindView(R.id.et_old_password)
    EditText etOldPassword;     //旧密码
    @BindView(R.id.iv_old_error)
    ImageView ivOldError;       //旧密码错误显示的图标
    @BindView(R.id.iv_old_right)
    ImageView ivOldRight;       //旧密码正确显示的图标
    @BindView(R.id.et_password)
    EditText etPassword;        //新密码
    @BindView(R.id.iv_is_show_pwd01)
    ImageView ivIsShowPwd01;    //控制新密码是否明文显示的按钮01
    @BindView(R.id.iv_clean_password01)
    ImageView ivCleanPassword01; //清除密码按钮01
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword; //确认新密码
    @BindView(R.id.iv_is_show_pwd02)
    ImageView ivIsShowPwd02;    //控制新密码是否明文显示的按钮02
    @BindView(R.id.iv_clean_password02)
    ImageView ivCleanPassword02;  //清除密码按钮02
    @BindView(R.id.btn_confirm)
    Button btnConfirm;   //确认按钮

    private String userID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        ButterKnife.bind(this);
        editTextSetting();
    }

    /**
     * 密码框的UI变化设定
     */
    private void editTextSetting() {
        //旧密码先关
        etOldPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        etOldPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {//失去焦点的时候,验证老密码是否正确
                    checkOldPassword();
                }
            }
        });
        etOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0) {
                    ivOldRight.setVisibility(View.INVISIBLE);
                    ivOldError.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //输入密码输入框的监听
        etPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //变化之后如果有字符串 就显示叉叉和眼睛, 如果没有就隐藏叉叉和眼睛
                if (etPassword.getText().toString().length() != 0) {
                    ivCleanPassword01.setVisibility(View.VISIBLE);
                    ivIsShowPwd01.setVisibility(View.VISIBLE);
                } else {
                    ivCleanPassword01.setVisibility(View.INVISIBLE);
                    ivIsShowPwd01.setVisibility(View.INVISIBLE);
                }
            }
        });
        //确认密码输入框的监听
        etConfirmPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //变化之后如果有字符串 就显示叉叉和眼睛, 如果没有就隐藏叉叉和眼睛
                if (etConfirmPassword.getText().toString().length() != 0) {
                    ivCleanPassword02.setVisibility(View.VISIBLE);
                    ivIsShowPwd02.setVisibility(View.VISIBLE);
                } else {
                    ivCleanPassword02.setVisibility(View.INVISIBLE);
                    ivIsShowPwd02.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**
     * 验证旧密码是否正确
     */
    private void checkOldPassword() {
        //1.判断老密码是否为空
        //2.发起请求,验证老密码
        String oldPassword = etOldPassword.getText().toString();
        if (TextUtils.isEmpty(oldPassword)) {
            return;
        }
        UserBean userBean = AppSetting.getUserInfo();
        if(userBean == null){
            return;
        }
        userID = String.valueOf(userBean.data.userInfo.id);
        //发起请求
        UpdateUserRepo.checkOldPassword(userID, oldPassword)
                .compose(this.<OldPasswordBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<OldPasswordBean>() {
                    @Override
                    public void onDataSuccess(OldPasswordBean oldPasswordBean) {
                        boolean result = oldPasswordBean.data.result;
                        if (result) {//老密码正确
                            //显示钩钩
                            ivOldError.setVisibility(View.INVISIBLE);
                            ivOldRight.setVisibility(View.VISIBLE);
                        } else {
                            //老密码错误  显示叉叉
                            ivOldError.setVisibility(View.VISIBLE);
                            ivOldRight.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }


    private boolean isShowPassword01 = false;//是否显示密码的标识
    private boolean isShowPassword02 = false;//是否显示密码的标识

    @OnClick({R.id.iv_is_show_pwd01, R.id.iv_clean_password01, R.id.iv_is_show_pwd02, R.id.iv_clean_password02})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_is_show_pwd01:
                if (isShowPassword01) {
                    etPassword.setTransformationMethod(null);
                    ivIsShowPwd01.setImageResource(R.drawable.login_password_on_icon);
                    isShowPassword01 = false;
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivIsShowPwd01.setImageResource(R.drawable.login_password_off_icon);
                    isShowPassword01 = true;
                }
                etPassword.setSelection(etPassword.getText().toString().length());
                break;
            case R.id.iv_clean_password01:
                etPassword.setText(null);
                break;


            case R.id.iv_is_show_pwd02:
                if (isShowPassword02) {
                    etConfirmPassword.setTransformationMethod(null);
                    ivIsShowPwd02.setImageResource(R.drawable.login_password_on_icon);
                    isShowPassword02 = false;
                } else {
                    etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivIsShowPwd02.setImageResource(R.drawable.login_password_off_icon);
                    isShowPassword02 = true;
                }
                etConfirmPassword.setSelection(etConfirmPassword.getText().toString().length());
                break;
            case R.id.iv_clean_password02:
                etConfirmPassword.setText(null);
                break;


        }
    }

    //返回按钮
    @OnClick(R.id.iv_back)
    public void onBackClicked() {
        onBackPressed();
    }


    //确认按钮的点击事件
    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        //1.验证 三个 密码是否为空
        //2.验证两个新密码是否相同
        //3.发起修改密码的请求
        String oldPassword = etOldPassword.getText().toString();
        String newPwd = etPassword.getText().toString();
        String confirmNewPwd = etConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(confirmNewPwd)) {
            Toaster.showShort("密码不能为空");
            return;
        }
        if(!newPwd.equals(confirmNewPwd)){
            Toaster.showShort("新密码不一致");
            return;
        }
        UpdateUserRepo.updatePassword(userID,oldPassword,newPwd)
                .compose(this.<ModifyPasswordBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<ModifyPasswordBean>() {
                    @Override
                    public void onDataSuccess(ModifyPasswordBean modifyPasswordBean) {
                        if(modifyPasswordBean.data.result == 0){ //修改密码成功
                            Toaster.showShort("密码修改成功");
                            finish();
                        }else {
                            //失败
                            Toaster.showShort("密码修改失败");
                        }

                    }
                });
    }
}
