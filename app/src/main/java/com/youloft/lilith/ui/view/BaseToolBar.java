package com.youloft.lilith.ui.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.youloft.lilith.R;


/**
 * Created by zchao on 2017/6/26.
 * desc: 默认ToolBar
 * version:
 */

public class BaseToolBar extends ToolBarLayout {

    private ImageView mBackBtn, mShareBtn;
    private TextView mTitleTV;
    private OnToolBarItemClickListener mListener;

    public BaseToolBar(@NonNull Context context) {
        this(context, null);
    }

    public BaseToolBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.tool_bar, this);
        mBackBtn = (ImageView) findViewById(R.id.tool_bar_back_btn);
        mShareBtn = (ImageView) findViewById(R.id.tool_bar_share_btn);
        mTitleTV = (TextView) findViewById(R.id.tool_bar_title);
    }

    /**
     * 设置title
     * @param title title文字
     */
    public void setTitle(String title){
        mTitleTV.setText(title);
    }

    /**
     * 获取title文字
     * @return
     */
    public String getTitle(){
        return mTitleTV != null ? mTitleTV.getText().toString() : "";
    }

    /**
     * 设置分享图标
     * @param shareImage
     */
    public void setShareImage(@DrawableRes int shareImage){
        mShareBtn.setImageResource(shareImage);
    }

    /**
     * 设置返回按钮图标
     * @param backImage
     */
    public void setBackBtnImage(@DrawableRes int backImage){
        mBackBtn.setImageResource(backImage);
    }

    public void setOnToolBarItemClickListener(OnToolBarItemClickListener listener){
        mListener = listener;
        mBackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnBackBtnClick();
                }
            }
        });
        mTitleTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnTitleBtnClick();
                }
            }
        });
        mShareBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnShareBtnClick();
                }
            }
        });
    }

    interface OnToolBarItemClickListener {
        void OnBackBtnClick();
        void OnTitleBtnClick();
        void OnShareBtnClick();
    }
}
