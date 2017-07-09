package com.youloft.lilith.cons.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;

import com.youloft.lilith.R;
import com.youloft.lilith.common.widgets.dialog.BaseDialog;

/**
 * Created by zchao on 2017/7/9.
 * desc:
 * version:
 */

public class LogInOrCompleteDialog extends BaseDialog {
    public LogInOrCompleteDialog(@NonNull Context context) {
        this(context, R.style.DialogTheme);
    }

    public LogInOrCompleteDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        LayoutInflater.from(context).inflate(R.layout.lod_in_jump_dialog, null);
    }
}
