package com.youloft.lilith.topic.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.widgets.dialog.BaseDialog;

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
    public OnClickConfirmListener onClickConfirmListener;
    private int voteId;
    private String voteTitle;
    private String title;

    public VoteDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.layout_vote_dialog);
        init();
    }

    public VoteDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.layout_vote_dialog);
        init();

    }
    public void setListener(OnClickConfirmListener listener) {
        this.onClickConfirmListener = listener;
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

    public void setTitle(String shortTitle,String title ,int id) {
        voteId = id;
        voteTitle = title;
        this.title = title;
        textPointVote.setText("投票给: " + title);
        if (id % 2 == 1) {
            textPointVote.setTextColor(Color.parseColor("#ff8282"));
        } else {
            textPointVote.setTextColor(Color.parseColor("#5696df"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.report_cancel:
                this.dismiss();
                break;
            case R.id.report_confirm:
                this.dismiss();
                if (onClickConfirmListener != null) {
                    onClickConfirmListener.clickConfirm(editVote.getText().toString(),voteId,title);
                }
                break;
            default:
                break;
        }
    }
    public interface  OnClickConfirmListener {
        void clickConfirm(String msg,int id,String title);
    }
}
