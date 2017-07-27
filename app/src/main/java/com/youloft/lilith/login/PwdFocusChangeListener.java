package com.youloft.lilith.login;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/7/13.
 */

public class PwdFocusChangeListener implements View.OnFocusChangeListener{

    private EditText _text;
    private ImageView ivCleanPassword;
    private ImageView ivIsShowPwd;
    public PwdFocusChangeListener(EditText _text,ImageView ivCleanPassword,ImageView ivIsShowPwd) {
        this._text = _text;
        this.ivCleanPassword = ivCleanPassword;
        this.ivIsShowPwd = ivIsShowPwd;
    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {//有内容显示,无内容,隐藏
            if(android.text.TextUtils.isEmpty(_text.getText().toString())){
                ivCleanPassword.setVisibility(View.INVISIBLE);
                ivIsShowPwd.setVisibility(View.INVISIBLE);
            }else {
                ivCleanPassword.setVisibility(View.VISIBLE);
                ivIsShowPwd.setVisibility(View.VISIBLE);
            }

        } else {//无脑隐藏
            ivCleanPassword.setVisibility(View.INVISIBLE);
            ivIsShowPwd.setVisibility(View.INVISIBLE);
        }
    }
}
