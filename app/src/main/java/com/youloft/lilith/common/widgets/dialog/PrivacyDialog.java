package com.youloft.lilith.common.widgets.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.TextView;

import com.youloft.lilith.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 隐私注册的弹窗
 * Created by gyh on 2017/7/11.
 */

public class PrivacyDialog extends BaseDialog {


    @BindView(R.id.tv_content)
    TextView tvContent;

    public PrivacyDialog(@NonNull Context context) {
        super(context);
        initView();
    }

    public PrivacyDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_privacy);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.iv_close, R.id.fl_root})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
            case R.id.fl_root:
                dismiss();
                break;
        }
    }

    public static final String CONTENT= "dasda";
}
