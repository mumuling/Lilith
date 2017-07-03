package com.youloft.lilith.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.ui.view.BaseToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/30.
 */
@Route(path = "/test/FeedBackActivity")
public class FeedBackActivity extends BaseActivity {
    @BindView(R.id.btl_feedback)
    BaseToolBar btlFeedback; //标题
    @BindView(R.id.et_feedback_content)
    EditText etFeedbackContent; //反馈内容
    @BindView(R.id.et_feedback_phone)
    EditText etFeedbackPhone; //用户电话

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        btlFeedback.setTitle("意见反馈");
    }

    @OnClick(R.id.btn_commit)
    public void onViewClicked() {

    }
}
