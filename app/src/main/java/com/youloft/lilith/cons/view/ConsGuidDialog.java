package com.youloft.lilith.cons.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.media.Base;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.widgets.dialog.BaseDialog;
import com.youloft.lilith.cons.consmanager.ConsDrawableManager;
import com.youloft.lilith.setting.AppSetting;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.blurry.internal.Blur;
import jp.wasabeef.blurry.internal.BlurFactor;

/**
 * Created by zchao on 2017/7/14.
 * desc: xz首页第一次引导弹窗
 * version:
 */

public class ConsGuidDialog extends BaseDialog {
    @BindView(R.id.cons_guide_bg)
    ImageView mConsGuideBg;
    @BindView(R.id.cons_view)
    ImageView mConsView;
    @BindView(R.id.cons_guide_confirm_btn)
    TextView mConsGuideConfirmBtn;
    @BindView(R.id.cons_content_root)
    LinearLayout mConsContentRoot;

    public static Bitmap mBg = null;

    public ConsGuidDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.cons_guid_dialog);
        ButterKnife.bind(this);
        Bitmap consImg = ConstellationViewFactory.getInstance().getConsImg("3", "5", "8", "9");
        if (consImg != null && !consImg.isRecycled()) {
            mConsView.setImageBitmap(consImg);
        }
        if (mBg != null && !mBg.isRecycled()) {
            mConsGuideBg.setImageBitmap(mBg);
        } else {
            mConsGuideBg.setBackgroundColor(getContext().getResources().getColor(R.color.black_30));
        }
    }

    /**
     * 点击知道了按钮
     */
    @OnClick(R.id.cons_guide_confirm_btn)
    public void close() {
        AppSetting.setGuideShown();
        dismiss();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mBg != null) {
            mBg.recycle();
        }
    }
}
