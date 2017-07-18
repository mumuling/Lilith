package com.youloft.lilith.login;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.OnFocusChange;

public class PhoneTextWatcher extends BaseTextWatcher {

    private EditText _text;
    private ImageView ivCleanNumber;
    public PhoneTextWatcher(EditText _text, ImageView ivCleanNumber, OnTextChange listener) {
        this._text = _text;
        this.ivCleanNumber = ivCleanNumber;
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
        if (s == null || s.length() == 0) return;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i != 3 && i != 8 && s.charAt(i) == '-') {
                continue;
            } else {
                sb.append(s.charAt(i));
                if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != '-') {
                    sb.insert(sb.length() - 1, '-');
                }
            }
        }
        if (!sb.toString().equals(s.toString())) {
            int index = start + 1;
            if (sb.charAt(start) == '-') {
                if (before == 0) {
                    index++;
                } else {
                    index--;
                }
            } else {
                if (before == 1) {
                    index--;
                }
            }
            _text.setText(sb.toString());
            _text.setSelection(index);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (_text.getText().toString().length() != 0) {
            ivCleanNumber.setVisibility(View.VISIBLE);
        } else {
            ivCleanNumber.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    boolean isValid() {
        return !TextUtils.isEmpty(_text.getText().toString());
    }

}