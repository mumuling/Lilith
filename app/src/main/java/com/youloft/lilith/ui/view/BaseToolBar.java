package com.youloft.lilith.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
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
    private TextView mSaveTV;
    private OnToolBarItemClickListener mListener;

    public BaseToolBar(@NonNull Context context) {
        this(context, null);
    }

    public BaseToolBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initStyles(context, attrs);
        setBackgroundColor(Color.parseColor("#30284d"));
    }

    /**
     * 初始化style
     * @param context
     * @param attrs
     */
    private void initStyles(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.BaseToolBar);
        boolean showBackBtn = a.getBoolean(R.styleable.BaseToolBar_show_back_btn, true);
        boolean showTitle = a.getBoolean(R.styleable.BaseToolBar_show_title_btn, true);
        boolean showShareBtn = a.getBoolean(R.styleable.BaseToolBar_show_share_btn, true);

        mBackBtn.setVisibility(showBackBtn ? VISIBLE : GONE);
        mShareBtn.setVisibility(showShareBtn ? VISIBLE : GONE);
        mTitleTV.setVisibility(showTitle ? VISIBLE : GONE);
    }

    /**
     * 初始化View
     * @param context
     */
    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.tool_bar, this);
        mBackBtn = (ImageView) findViewById(R.id.tool_bar_back_btn);
        mShareBtn = (ImageView) findViewById(R.id.tool_bar_share_btn);
        mTitleTV = (TextView) findViewById(R.id.tool_bar_title);
        mSaveTV = (TextView) findViewById(R.id.tool_bar_save);
    }

    /**
     *
     * @param color
     */
    public void setBackgroundColor(int color){
        super.setBackgroundColor(color);
    }

    /**
     * 设置是否显示保存按钮
     * @param showSaveBtn
     */
    public void setShowSaveBtn(boolean showSaveBtn){
        mSaveTV.setVisibility(showSaveBtn ? VISIBLE : GONE);
    }

    /**
     * 设置是否显示返回按钮
     * @param showBackBtn
     */
    public void setShowBackBtn(boolean showBackBtn){
        mBackBtn.setVisibility(showBackBtn ? VISIBLE : GONE);
    }

    /**
     * 设置是否显示title按钮
     * @param showTitleBtn
     */
    public void setShowTitleBtn(boolean showTitleBtn){
        mTitleTV.setVisibility(showTitleBtn ? VISIBLE : GONE);
    }

    /**
     * 设置是否显示分享按钮
     * @param showShareBtn
     */
    public void setShowShareBtn(boolean showShareBtn){
        mShareBtn.setVisibility(showShareBtn ? VISIBLE : GONE);
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
        mSaveTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnSaveBtnClick();
                }
            }
        });
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

    public interface OnToolBarItemClickListener {
        void OnBackBtnClick();
        void OnTitleBtnClick();
        void OnShareBtnClick();
        void OnSaveBtnClick();
    }
}
