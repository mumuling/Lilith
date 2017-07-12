package com.youloft.lilith.cons.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.R;
import com.youloft.lilith.common.GlideApp;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.widgets.dialog.BaseDialog;
import com.youloft.lilith.common.widgets.view.RoundImageView;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.ui.fragment.XZFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.blurry.internal.Blur;
import jp.wasabeef.blurry.internal.BlurFactor;

/**
 * Created by zchao on 2017/7/9.
 * desc: 跳转登录界面或则完善资料界面的dialog
 * version:
 */
//TODO 还需要做防止多次弹出此窗口的问题
public class LogInOrCompleteDialog extends BaseDialog {
    @BindView(R.id.login_jump_dialog)
    ImageView mLoginJumpDialog;
    @BindView(R.id.login_jump_dialog_icon)
    RoundImageView mLoginJumpDialogIcon;
    @BindView(R.id.login_jump_dialog_nickname)
    TextView mLoginJumpDialogNickname;
    @BindView(R.id.login_jump_dialog_description)
    TextView mLoginJumpDialogDescription;
    @BindView(R.id.login_jump_dialog_btn)
    Button mLoginJumpDialogBtn;
    @BindView(R.id.login_jump_dialog_group)
    FrameLayout mLoginJumpDialogGroup;

    private boolean mNeedBlurbg = false;
    private Context mContext;
    private int status = XZFragment.LOG_IN; //
    private static Bitmap mBlurBitmap = null;
    private static boolean hisDialogShow = false;

    public LogInOrCompleteDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        setContentView(R.layout.lod_in_jump_dialog);
        ButterKnife.bind(this);
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View inflate = LayoutInflater.from(mContext).inflate(layoutResID, null);
        super.setContentView(inflate);
    }

    /**
     * 关闭按钮点击
     */
    @OnClick({R.id.login_jump_dialog_close, R.id.login_jump_dialog})
    public void close() {
        dismiss();
    }



    /**
     * //是否需要显示背景的模糊图。使用时需要传入模糊图，
     * 如果不传入图片可以传入BaseActivity的上下文，这样会自动调取BaseActivity中的截屏功能；
     *
     * @return
     */
    public LogInOrCompleteDialog withBlurBg(Bitmap blurBitmap) {
        mBlurBitmap = blurBitmap;
        mNeedBlurbg = true;
        return this;
    }

    public LogInOrCompleteDialog withBlurBg() {
        return withBlurBg(null);
    }

    /**
     * 修改显示状态
     *
     * @param statu
     * @return
     */
    public LogInOrCompleteDialog setStatus(int statu) {
        this.status = statu;
        return this;
    }

    /**
     * 已显示就不在显示了
     *
     */
    @Override
    public void show() {
        //控制只允许显示一个此dialog，多余的直接dismiss掉；这样就不用在所有地方做连点判断了；
        if (hisDialogShow) {
            dismiss();
            return;
        }
        hisDialogShow = true;
        initDate();
        super.show();
    }

    @Override
    public void dismiss() {
        if (mBlurBitmap != null) {
            mBlurBitmap.recycle();
        }
        hisDialogShow = false;
        super.dismiss();
    }
    public static final int TOPIC_IN = 3;

    /**
     * 初始化界面
     */
    private void initDate() {
        if (mNeedBlurbg) {
            if (mBlurBitmap != null && !mBlurBitmap.isRecycled()) {
                mLoginJumpDialog.setImageBitmap(mBlurBitmap);
            } else if (mContext instanceof BaseActivity) {
                Bitmap bitmap = ((BaseActivity) mContext).takeScreenShot(false, 4);
                if (bitmap != null && !bitmap.isRecycled()) {
                    BlurFactor bf = new BlurFactor();
                    bf.width = bitmap.getWidth();
                    bf.height = bitmap.getHeight();
                    bf.sampling = 1;
                    bf.radius = 5;
                    bitmap = Blur.of(mContext, bitmap, bf);
                    mLoginJumpDialog.setImageBitmap(bitmap);
                } else {
                    mLoginJumpDialog.setBackgroundColor(mContext.getResources().getColor(R.color.black_70));
                }

            } else {
                mLoginJumpDialog.setBackgroundColor(mContext.getResources().getColor(R.color.black_70));
            }
        } else {
            mLoginJumpDialog.setBackgroundColor(mContext.getResources().getColor(R.color.black_70));
        }

        if (status == XZFragment.LOG_IN) {

        } else if (status == XZFragment.COMPLETE_INFO) {
            UserBean userInfo = AppSetting.getUserInfo();
            if (userInfo != null && userInfo.data != null && userInfo.data.userInfo != null) {
                UserBean.DataBean.UserInfoBean info = userInfo.data.userInfo;
                String nickName = TextUtils.isDigitsOnly(info.nickName) ? (TextUtils.isEmpty(info.phone) ? mContext.getResources().getString(R.string.app_name_ch) : info.phone) : info.nickName;
                mLoginJumpDialogNickname.setText(nickName);

                if (!TextUtils.isEmpty(info.headImg)) {
                    GlideApp.with(mContext).load(info.headImg).into(mLoginJumpDialogIcon);
                }
            }
            mLoginJumpDialogBtn.setText(mContext.getResources().getString(R.string.log_dialog_to_complete_btn));
            mLoginJumpDialogDescription.setText(mContext.getResources().getString(R.string.log_dialog_to_complete));
        } else if (status == TOPIC_IN) {
            mLoginJumpDialogDescription.setText(mContext.getResources().getString(R.string.log_dialog_to_lod_topic));
        }

        mLoginJumpDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == XZFragment.LOG_IN || status == TOPIC_IN) {
                    ARouter.getInstance().build("/test/LoginActivity")
                            .navigation();
                    close();
                } else {
                    ARouter.getInstance().build("/test/EditInformationActivity")
                            .navigation();
                    close();
                }
            }
        });
    }
}
