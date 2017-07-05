package com.youloft.lilith.topic.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.widgets.dialog.BaseDialog;

import butterknife.BindView;

/**
 *
 */

public class VoteDialog extends BaseDialog implements View.OnClickListener {

    private TextView textPointVote;

    private View divider;

    private TextView reportCancel;

    private TextView reportConfirm;

    private LinearLayout llBottomRoot;

    private EditText editVote;

    public VoteDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.layout_vote_dialog);
        init();
    }

    public VoteDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public void init() {
        editVote = (EditText) findViewById(R.id.edit_vote);
        editVote.setFocusable(true);

        editVote.setFocusableInTouchMode(true);

        editVote.requestFocus();

        InputMethodManager inputManager = (InputMethodManager) editVote.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editVote, 0);
        reportConfirm = (TextView) findViewById(R.id.report_confirm);
        reportCancel = (TextView) findViewById(R.id.report_cancel);
        textPointVote = (TextView) findViewById(R.id.text_point_vote);
        reportConfirm.setOnClickListener(this);
        reportCancel.setOnClickListener(this);

    }

    public void setTitle(String title) {
        textPointVote.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.report_cancel:
                this.dismiss();
                break;
            case R.id.report_confirm:

                break;
            default:
                break;
        }
    }
}
