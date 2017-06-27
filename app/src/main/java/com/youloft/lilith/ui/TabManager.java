package com.youloft.lilith.ui;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.SafeUtils;
import com.youloft.lilith.ui.fragment.CCFragment;
import com.youloft.lilith.ui.fragment.HTFragment;
import com.youloft.lilith.ui.fragment.MEFragment;
import com.youloft.lilith.ui.fragment.XZFragment;
import com.youloft.lilith.ui.view.NavBarLayout;
import com.youloft.lilith.ui.view.TabItemBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zchao on 2017/6/26.
 * desc: tab管理，用于管理tab的切换
 * version:
 */

public class TabManager implements NavBarLayout.OnTabChangeListener {
    private final FragmentManager mFragmentManager;
    private final NavBarLayout mNavBar;
    private Activity mMainActivity;
    private Fragment mCurrentFragment;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private HashMap<String, Fragment> mFragmentsCache = new HashMap<>();
    private static int FRAGMENT_CONTAINER = R.id.main_content;

    public TabManager(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
        mFragmentManager = mainActivity.getSupportFragmentManager();
        mNavBar = mainActivity.mNavBar;
        mNavBar.setTabChangeListener(this);

        initDefaultTabFragment();

    }

    /**
     * 初始化所有fragment
     */
    private void initDefaultTabFragment() {
        if (mNavBar == null || mNavBar.getTabs() == null) {
            return;
        }
        ArrayList<TabItemBean> tabs = mNavBar.getTabs();
        mFragments.clear();
        for (int i = 0; i < tabs.size(); i++) {
            TabItemBean safeData = SafeUtils.getSafeData(tabs, i);
            if (safeData == null) {
                continue;
            }
            if (mFragmentsCache.containsKey(safeData.mTabName)) {
                mFragments.add(mFragmentsCache.get(safeData.mTabName));
            } else {
                Fragment fragment = fragmentCreator(safeData.mTabName);
                if (fragment == null) {
                    continue;
                }
                mFragmentsCache.put(safeData.mTabName, fragment);
                mFragments.add(fragment);
            }
        }
        bindTabFragment();
        setTabIndex(0);
    }

    /**
     * 创建Fragment
     * @param tag
     * @return
     */
    private Fragment fragmentCreator(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return null;
        }
        Fragment fragment = null;
        switch (tag) {
            case "星座":
                fragment = new XZFragment();
                break;
            case "话题":
                fragment = new HTFragment();
                break;
            case "测测":
                fragment = new CCFragment();
                break;
            case "我":
                fragment = new MEFragment();
                break;
            default:
                return null;

        }
        return fragment;
    }

    /**
     * 设置显示的fragment
     * @param index
     */
    private void setTabIndex(int index) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (mCurrentFragment != null) {
            ft.hide(mCurrentFragment);
        }
        mCurrentFragment = SafeUtils.getSafeData(mFragments, index);
        if (mCurrentFragment != null) {
            ft.show(mCurrentFragment);
        }
        ft.commit();
    }

    /**
     * 绑定所有fragment
     */
    private void bindTabFragment() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        for (int i = 0; i < mFragments.size(); i++) {
            ft.add(FRAGMENT_CONTAINER, mFragments.get(i));
            ft.hide(mFragments.get(i));
        }
        ft.commit();
    }

    @Override
    public void selectChange(int index) {
        setTabIndex(index);
    }

    @Override
    public void tabsChange() {
        initDefaultTabFragment();
    }
}
