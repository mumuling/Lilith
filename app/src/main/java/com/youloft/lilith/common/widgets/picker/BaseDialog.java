package com.youloft.lilith.common.widgets.picker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import com.youloft.lilith.R;

/**
 * Created by zchao on 2017/6/27.
 * desc:
 * version:
 */

public class BaseDialog extends Dialog {

    public BaseDialog(@NonNull Context context) {
        this(context, R.style.BaseDialog);
    }

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
