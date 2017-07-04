package com.youloft.lilith.topic;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.topic.adapter.PointAnswerAdapter;
import com.youloft.lilith.topic.widget.ScrollFrameLayout;
import com.youloft.lilith.topic.widget.SoftInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 参与者观点的详情页
 * version
 *
 * @author slj
 * @time 2017/6/29 14:55
 * @class PointDetailActivity
 */

public class PointDetailActivity extends BaseActivity implements ScrollFrameLayout.IscrollChange {

    @BindView(R.id.ll_top_root)
    LinearLayout llTopRoot;
    @BindView(R.id.close_icon)
    ImageView closeIcon;
    @BindView(R.id.command_num)
    TextView commandNum;
    @BindView(R.id.root)
    SoftInputLayout root;
    @BindView(R.id.rv_comment_answer)
    ScrollFrameLayout rvCommentAnswer;
    @BindView(R.id.comment_edit)
    EditText commentEdit;
    private LinearLayoutManager mLayoutManager;
    private PointAnswerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_detail);
        overridePendingTransition(R.anim.slide_in_bottom, 0);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        int statusHeight = ViewUtil.getStatusHeight();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) llTopRoot.getLayoutParams();
        params.topMargin = statusHeight;
        llTopRoot.setLayoutParams(params);
        rvCommentAnswer.setInterface(this);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new PointAnswerAdapter(this);
        rvCommentAnswer.setLayoutManager(mLayoutManager);
        rvCommentAnswer.setAdapter(adapter);

    }


    @Override
    public void goFinish() {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }

    @Override
    public void recover() {
        root.setBackgroundColor(Color.parseColor("#4D000000"));
    }

    @Override
    public void move() {
        root.setBackgroundColor(Color.parseColor("#00000000"));
    }

    @OnClick(R.id.close_icon)
    public void onClick() {
        finish();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }
}
