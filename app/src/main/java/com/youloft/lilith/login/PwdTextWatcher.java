package com.youloft.lilith.login;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.umeng.socialize.media.Base;

/**
 * Created by gyh on 2017/7/13.
 */

public class PwdTextWatcher extends BaseTextWatcher{

    private EditText _text;
    private ImageView ivCleanPassword;
    private ImageView ivIsShowPwd;
    public PwdTextWatcher(EditText _text,ImageView ivCleanPassword,ImageView ivIsShowPwd, OnTextChange listener) {
        this._text = _text;
        this.ivCleanPassword = ivCleanPassword;
        this.ivIsShowPwd = ivIsShowPwd;
        setListener(listener);
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (listener != null) {
            listener.onChange(isValid(), _text);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        //变化之后如果有字符串 就显示叉叉和眼睛, 如果没有就隐藏叉叉和眼睛
        if (_text.getText().toString().length() != 0) {
            ivCleanPassword.setVisibility(View.VISIBLE);
            ivIsShowPwd.setVisibility(View.VISIBLE);
        } else {
            ivCleanPassword.setVisibility(View.INVISIBLE);
            ivIsShowPwd.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    boolean isValid() {
        return !TextUtils.isEmpty(_text.getText().toString());
    }
}
