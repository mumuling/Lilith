package com.youloft.lilith.login;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by gyh on 2017/7/13.
 */

public class PhoneFocusChangeListener implements View.OnFocusChangeListener {
    private EditText _text;
    private ImageView ivCleanNumber;
    public PhoneFocusChangeListener(EditText _text,ImageView ivCleanNumber) {
        this._text = _text;
        this.ivCleanNumber = ivCleanNumber;
    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {//有内容显示,无内容,隐藏
            if(android.text.TextUtils.isEmpty(_text.getText().toString())){
                ivCleanNumber.setVisibility(View.INVISIBLE);
            }else {
                ivCleanNumber.setVisibility(View.VISIBLE);
            }

        } else {//无脑隐藏
            ivCleanNumber.setVisibility(View.INVISIBLE);
        }
    }
}
