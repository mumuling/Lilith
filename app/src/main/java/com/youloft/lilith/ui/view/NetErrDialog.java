package com.youloft.lilith.ui.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;

import com.youloft.lilith.R;
import com.youloft.lilith.common.widgets.dialog.BaseDialog;
import android.os.Handler;

import java.lang.ref.WeakReference;


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
        MyHandler myHandler = new MyHandler(this);
        myHandler.sendEmptyMessageDelayed(0, 2000);

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    /**
     * 使用弱引用，防止内存泄漏
     */
    static class MyHandler extends Handler
    {
        WeakReference<Dialog> mWeakReference;
        public MyHandler(Dialog activity)
        {
            mWeakReference=new WeakReference<Dialog>(activity);
        }
        @Override
        public void handleMessage(Message msg)
        {
            final Dialog dialog=mWeakReference.get();
            if(dialog!=null)
            {
                dialog.dismiss();
            }
        }
    }
}
