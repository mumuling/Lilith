package com.youloft.lilith.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.youloft.lilith.common.utils.Utils;

import java.util.ArrayList;

/**
 * Created by zchao on 2017/6/26.
 * desc: 底部tab工具条，
 * version:
 */

public class NavBarLayout extends LinearLayout {
    private ArrayList<TabItemBean> mTabs = new ArrayList<>();

    public NavBarLayout(Context context) {
        this(context, null);
    }

    public NavBarLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LinearLayout.LayoutParams lp = (LayoutParams) getLayoutParams();
        lp.height = Utils.dp2Px(getResources(), 60);
        initDefaultTabs();
        initTabsView();
    }

    /**
     * 添加tabItem的view
     */
    private void initTabsView() {
        if (mTabs != null && mTabs.size() > 0) {
            for (int i = 0; i < mTabs.size(); i++) {
                TabItemBean item = SafeUtils.getSafeData(mTabs, i);
                if (item == null) {
                    continue;
                }
                NavItemView itemView = new NavItemView(getContext());
                itemView.setData(item);
                addView(itemView);
            }
        }
    }

    /**
     * 设置默认的tab项目
     */
    private void initDefaultTabs() {
        mTabs.add(new TabItemBean("星座", R.mipmap.ic_launcher));
        mTabs.add(new TabItemBean("话题", R.mipmap.ic_launcher));
        mTabs.add(new TabItemBean("测测", R.mipmap.ic_launcher));
        mTabs.add(new TabItemBean("我", R.mipmap.ic_launcher));
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
            mNavIc.setImageResource(pTabInfo.mTabIc);
            mNavName.setText(pTabInfo.mTabName);
        }
    }
}
