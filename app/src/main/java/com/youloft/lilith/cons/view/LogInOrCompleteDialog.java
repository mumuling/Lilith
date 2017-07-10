package com.youloft.lilith.cons.view;

import android.content.Context;
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
import com.youloft.lilith.common.widgets.dialog.BaseDialog;
import com.youloft.lilith.common.widgets.view.RoundImageView;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.ui.fragment.XZFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zchao on 2017/7/9.
 * desc: 跳转登录界面或则完善资料界面的dialog
 * version:
 */

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

    private int status = XZFragment.LOG_IN; //

    public LogInOrCompleteDialog(@NonNull Context context) {
        this(context, R.style.DialogTheme);
    }

    public LogInOrCompleteDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.lod_in_jump_dialog);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View inflate = LayoutInflater.from(getContext()).inflate(layoutResID, null);
        super.setContentView(inflate);
    }

    /**
     * 关闭按钮点击
     *
     */
    @OnClick(R.id.login_jump_dialog_close)
    public void close(){
        dismiss();
    }

    /**
     * 修改显示状态
     * @param statu
     * @return
     */
    public LogInOrCompleteDialog setStatus(int statu) {
        this.status = statu;
        initDate();
        return this;
    }

    private void initDate() {
        if (status == XZFragment.LOG_IN) {

        } else if (status == XZFragment.COMPLETE_INFO) {
            UserBean userInfo = AppSetting.getUserInfo();
            if (userInfo != null && userInfo.data != null && userInfo.data.userInfo != null) {
                UserBean.DataBean.UserInfoBean info = userInfo.data.userInfo;
                String nickName = TextUtils.isDigitsOnly(info.nickName)? (TextUtils.isEmpty(info.phone)? "莉莉斯":info.phone): info.nickName;
                mLoginJumpDialogNickname.setText(nickName);

                if (!TextUtils.isEmpty(info.headImg)){
                    GlideApp.with(getContext()).load(info.headImg).into(mLoginJumpDialogIcon);
                }
            }
            mLoginJumpDialogBtn.setText(getContext().getResources().getString(R.string.log_dialog_to_complete_btn));
            mLoginJumpDialogDescription.setText(getContext().getResources().getString(R.string.log_dialog_to_complete));
        }

        mLoginJumpDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == XZFragment.LOG_IN) {
                    ARouter.getInstance().build("/test/LoginActivity")
                            .navigation();
                } else {
                    ARouter.getInstance().build("/test/EditInformationActivity")
                            .navigation();
                }
            }
        });
    }
}
