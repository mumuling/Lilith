package com.youloft.lilith.common.widgets.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;

import com.youloft.lilith.R;
import com.youloft.lilith.info.DownloadService;
import com.youloft.lilith.info.activity.SettingActivity;
import com.youloft.lilith.info.bean.CheckVersionBean;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gyh on 2017/7/11.
 */

public class DownloadSelectDialog extends BaseDialog {
    private Activity mActivity;
    private CheckVersionBean checkVersionBean;
    public DownloadSelectDialog(@NonNull Context context) {
        super(context);
        mActivity = (Activity) context;
    }

    public DownloadSelectDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public DownloadSelectDialog(Context context, CheckVersionBean checkVersionBean) {
        super(context);
        mActivity = (Activity) context;
        this.checkVersionBean = checkVersionBean;
    }

    private void initView() {
        setContentView(R.layout.dialog_download_select);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.iv_close, R.id.tv_update, R.id.fl_root})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_update:
                Intent intent = new Intent(mActivity, DownloadService.class);
                intent.putExtra("url",checkVersionBean.data.downPath);
                mActivity.startService(intent);
                break;
            case R.id.fl_root:
            case R.id.iv_close:
                dismiss();
                break;
        }
    }
}
