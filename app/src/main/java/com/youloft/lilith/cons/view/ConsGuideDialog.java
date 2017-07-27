package com.youloft.lilith.cons.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.common.widgets.dialog.BaseDialog;
import com.youloft.lilith.setting.AppSetting;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zchao on 2017/7/14.
 * desc: xz首页第一次引导弹窗
 * version:
 */

public class ConsGuideDialog extends BaseDialog {
    @BindView(R.id.cons_guide_bg)
    ImageView mConsGuideBg;
    @BindView(R.id.cons_view)
    ImageView mConsView;
    @BindView(R.id.cons_guide_confirm_btn)
    TextView mConsGuideConfirmBtn;
    @BindView(R.id.cons_content_root)
    LinearLayout mConsContentRoot;
    private int contentRootTop = 0;
    public static Bitmap mBg = null;

    public ConsGuideDialog(@NonNull Context context) {
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

    public ConsGuideDialog setConsImageLocation(int[] location) {
        this.contentRootTop = location[1];
        return this;
    }

    @Override
    public void show() {
        if (contentRootTop == 0) {
            contentRootTop = (int) ViewUtil.dp2px(20);
        }
        mConsContentRoot.setPadding(mConsContentRoot.getPaddingLeft(),
                mConsContentRoot.getPaddingTop() + contentRootTop,
                mConsContentRoot.getPaddingRight(),
                mConsContentRoot.getPaddingBottom());
        super.show();
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

    @Override
    public void onBackPressed() {
        close();
    }
}
