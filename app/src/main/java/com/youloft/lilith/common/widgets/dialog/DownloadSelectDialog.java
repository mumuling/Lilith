package com.youloft.lilith.common.widgets.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import com.youloft.lilith.R;

import butterknife.ButterKnife;

/**
 * Created by gyh on 2017/7/11.
 */

public class DownloadSelectDialog extends BaseDialog{
    public DownloadSelectDialog(@NonNull Context context) {
        super(context);
    }

    public DownloadSelectDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    private void initView() {
        setContentView(R.layout.dialog_download_select);
        ButterKnife.bind(this);

    }
}
