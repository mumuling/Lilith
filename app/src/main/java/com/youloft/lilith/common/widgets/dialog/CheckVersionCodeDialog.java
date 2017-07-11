package com.youloft.lilith.common.widgets.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

/**
 * Created by Administrator on 2017/7/11.
 */

public class CheckVersionCodeDialog extends BaseDialog{
    public CheckVersionCodeDialog(@NonNull Context context) {
        super(context);
    }

    public CheckVersionCodeDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }
}
