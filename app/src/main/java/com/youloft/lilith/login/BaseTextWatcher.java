package com.youloft.lilith.login;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public abstract class BaseTextWatcher implements TextWatcher {
    protected OnTextChange listener;

    public void setListener(OnTextChange listener) {
        this.listener = listener;
    }

    public  interface OnTextChange{
       void onChange(boolean isValid, EditText view);
   }

    @Override
    public void afterTextChanged(Editable s) {

    }

    abstract boolean isValid();
}