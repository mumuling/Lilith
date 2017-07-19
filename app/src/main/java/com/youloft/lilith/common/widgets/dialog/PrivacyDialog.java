package com.youloft.lilith.common.widgets.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.youloft.lilith.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 隐私注册的弹窗
 * Created by gyh on 2017/7/11.
 */

public class PrivacyDialog extends BaseDialog {


    @BindView(R.id.tc_content)
    TextView tvContent;
    Context context;

    public PrivacyDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public PrivacyDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_privacy);
        ButterKnife.bind(this);
        InputStream is = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            is = context.getResources().openRawResource(R.raw.privacy);
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = is.read(buffer)) != -1) {
                bos.write(buffer, 0, length);
            }

            String htmlContent = bos.toString();
            Spanned spanned = Html.fromHtml(htmlContent);
            tvContent.setText(spanned);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



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
}
