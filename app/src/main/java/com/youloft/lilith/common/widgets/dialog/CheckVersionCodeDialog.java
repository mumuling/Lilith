package com.youloft.lilith.common.widgets.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.widget.ProgressBar;

import com.youloft.lilith.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/11.
 */

public class CheckVersionCodeDialog extends BaseDialog {
    @BindView(R.id.pb_check_version)
    ProgressBar pbCheckVersion;

    public CheckVersionCodeDialog(@NonNull Context context) {
        super(context);
        initView();
    }

    public CheckVersionCodeDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_check_version);
        ButterKnife.bind(this);

    }
}
