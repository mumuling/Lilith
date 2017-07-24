package com.youloft.lilith.ui.view;

import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;

import com.youloft.lilith.R;
import com.youloft.lilith.common.widgets.dialog.BaseDialog;
import android.os.Handler;


/**
 * Created by zchao on 2017/7/12.
 * desc: 网络错误dialog
 * version:
 */

public class NetErrDialog extends BaseDialog {

    public NetErrDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.net_err_dialog);
    }

    @Override
    public void show() {
        super.show();

        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                dismiss();
            }
        }.sendEmptyMessageDelayed(0, 2000);
    }


}
