package com.youloft.lilith.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.SafeUtils;
import com.youloft.lilith.common.utils.ViewUtil;

import java.util.ArrayList;

/**
 * Created by zchao on 2017/6/26.
 * desc: 底部tab工具条，
 * version:
 */

public class NavBarLayout extends LinearLayout {
    private ArrayList<TabItemBean> mTabs = new ArrayList<>();
    private ArrayList<NavItemView> mTabViews = new ArrayList<>();

    private OnTabChangeListener mTabChangeListener = null;

    public NavBarLayout(Context context) {
        this(context, null);
    }

    public NavBarLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int height = ViewUtil.dp2px(60);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height));
        initDefaultTabs();
        initTabsView();
    }

    /**
     * 添加tabItem的view
     */
    private void initTabsView() {
        mTabViews.clear();
        if (mTabs != null && mTabs.size() > 0) {
            for (int i = 0; i < mTabs.size(); i++) {
                TabItemBean item = SafeUtils.getSafeData(mTabs, i);
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
                                resetAllSelect();
                                ((NavItemView) v).setSelect(true);
                                if (mTabChangeListener != null) {
                                    mTabChangeListener.selectChange(tb.mIndex);
                                }
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
            NavItemView itemView = SafeUtils.getSafeData(mTabViews, i);
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
        mTabs.add(new TabItemBean("星座", R.mipmap.ic_launcher, 0, true));
        mTabs.add(new TabItemBean("话题", R.mipmap.ic_launcher, 1, false));
        mTabs.add(new TabItemBean("测测", R.mipmap.ic_launcher, 2, false));
        mTabs.add(new TabItemBean("我", R.mipmap.ic_launcher, 3, false));
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
        void selectChange(int index);
        void tabsChange();
    }

    /**
     * Desc: NavigationBar 的tab
     * Change:
     * i
     * @version
     * @author zchao created at 2017/6/26 18:34
     * @see
    */
    class NavItemView extends FrameLayout{
        public TabItemBean pTabInfo;
        private ImageView mNavIc;
        private TextView mNavName;

        public NavItemView(@NonNull Context context) {
            this(context, null);
        }

        public NavItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
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

        private void bindView(){
            mNavIc.setImageResource(pTabInfo.mTabIc);
            mNavName.setText(pTabInfo.mTabName);
            setBackgroundColor(pTabInfo.mSelected ? Color.RED : Color.TRANSPARENT);
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
