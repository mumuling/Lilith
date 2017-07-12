package com.youloft.lilith.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.SafeUtil;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.cons.consmanager.ConsManager;

import java.util.ArrayList;

/**
 * Created by zchao on 2017/6/26.
 * desc: 底部tab工具条，
 * version:
 */

public class NavBarLayout extends LinearLayout {
    private ArrayList<TabItemBean> mTabs = new ArrayList<>();
    private ArrayList<NavItemView> mTabViews = new ArrayList<>();
    private Paint mPaint;
    private OnTabChangeListener mTabChangeListener = null;

    public NavBarLayout(Context context) {
        this(context, null);
    }

    public NavBarLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        initDefaultTabs();
        initTabsView();
    }

    private void init() {
        int height = (int) ViewUtil.dp2px(49);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height));
        setBackgroundColor(getResources().getColor(R.color.tab_bg_color));

        initLinePaint();
    }

    private void initLinePaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(ViewUtil.dp2px(0.5f));
        mPaint.setColor(getResources().getColor(R.color.tab_line_color));
    }

    public void changConsIcon(int consKey){
        Integer[] consIconSrc = ConsManager.getConsIconSrc(String.valueOf(consKey));
        ConsManager.ConsInfo consSrc = ConsManager.getConsSrc(String.valueOf(consKey));
        NavItemView consIcon = SafeUtil.getSafeData(mTabViews, 0);
        if (consIcon != null && consIcon.pTabInfo != null) {
            consIcon.pTabInfo.mTabIcUnSelect = consIconSrc != null ? consIconSrc[1] : R.drawable.icon2_pisces;
            consIcon.pTabInfo.mTabIc = consIconSrc != null ? consIconSrc[0] : R.drawable.icon_pisces;
        }
        if (consSrc != null) {
            consIcon.pTabInfo.mTabName = consSrc.pKey;
        }
        consIcon.bindView();

    }

    /**
     * 添加tabItem的view
     */
    private void initTabsView() {
        mTabViews.clear();
        if (mTabs != null && mTabs.size() > 0) {
            for (int i = 0; i < mTabs.size(); i++) {
                TabItemBean item = SafeUtil.getSafeData(mTabs, i);
                if (item == null) {
                    continue;
                }
                NavItemView itemView = new NavItemView(getContext());
                itemView.setData(item);
                addView(itemView);
                mTabViews.add(i,itemView);
                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v instanceof NavItemView) {
                            TabItemBean tb = ((NavItemView) v).getData();
                            if (tb != null) {
                                if (mTabChangeListener != null) {
                                    boolean b = mTabChangeListener.selectChange(tb.mIndex);
                                    if (b) {
                                        return;
                                    }
                                }
                                resetAllSelect();
                                ((NavItemView) v).setSelect(true);

                            }
                        }
                    }
                });
            }
            if (mTabChangeListener != null) {
                mTabChangeListener.tabsChange();
            }
        }
    }

    /**
     * 重置所有view的选中状态
     */
    private void resetAllSelect() {
        for (int i = 0; i < mTabViews.size(); i++) {
            NavItemView itemView = SafeUtil.getSafeData(mTabViews, i);
            if (itemView != null) {
                itemView.setSelect(false);
            }
        }
    }

    /**
     * 让某一个tab处于选中状态
     * @param index
     */
    public void setSelectTab(int index){
        if (index < 0 || index > mTabViews.size()-1) {
            return;
        }
        resetAllSelect();
        mTabViews.get(index).setSelect(true);
    }
    /**
     * 设置默认的tab项目
     */
    private void initDefaultTabs() {
        mTabs.add(new TabItemBean("白羊座", R.drawable.icon2_aries, R.drawable.icon_aries, 0, true));
        mTabs.add(new TabItemBean("话题", R.drawable.tab_talk_icon_unselect, R.drawable.tab_talk_icon, 1, false));
        mTabs.add(new TabItemBean("测测", R.drawable.tab_cece_icon_unselect, R.drawable.tab_cece_icon, 2, false));
        mTabs.add(new TabItemBean("我", R.drawable.tab_mine_icon_unselect, R.drawable.tab_mine_icon, 3, false));
    }

    public ArrayList<TabItemBean> getTabs(){
        return mTabs;
    }

    public void setTabChangeListener(OnTabChangeListener mTabChangeListener) {
        this.mTabChangeListener = mTabChangeListener;
    }

    /**
     * Desc: 主界面底部icon点击接口
     * Change:
     *
     * @version
     * @author zchao created at 2017/6/27 9:40
     * @see
    */
    public interface OnTabChangeListener{
        boolean selectChange(int index);
        void tabsChange();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mPaint == null) {
            initLinePaint();
        }
        canvas.drawLine(0,0,getWidth(),0, mPaint);
    }


    /**
     * Desc: NavigationBar 的tab
     * Change:
     * i
     * @version
     * @author zchao created at 2017/6/26 18:34
     * @see
    */
    class NavItemView extends LinearLayout{
        public TabItemBean pTabInfo;
        private ImageView mNavIc;
        private TextView mNavName;

        public NavItemView(@NonNull Context context) {
            this(context, null);
        }

        public NavItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            setOrientation(VERTICAL);
            setGravity(Gravity.CENTER_HORIZONTAL);
            View itemView = LayoutInflater.from(context).inflate(R.layout.nav_bar_item, this);
            mNavIc = (ImageView) findViewById(R.id.nav_bar_icon);
            mNavName = (TextView) findViewById(R.id.nav_bar_name);
        }


        /**
         * 设置此tabView的数据
         * @param tabInfo
         */
        public void setData(TabItemBean tabInfo){
            pTabInfo = tabInfo;
            bindView();
        }

        public void bindView(){
            mNavIc.setImageResource(pTabInfo.mSelected ? pTabInfo.mTabIc : pTabInfo.mTabIcUnSelect);
            mNavName.setText(pTabInfo.mTabName);
        }

        public TabItemBean getData(){
            return pTabInfo;
        }

        public void setSelect(boolean select){
            if (pTabInfo.mSelected == select) {
                return;
            }
            pTabInfo.mSelected = select;
            bindView();
        }
    }
}
