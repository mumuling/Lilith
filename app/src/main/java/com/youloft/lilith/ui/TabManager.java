package com.youloft.lilith.ui;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.AppConfig;
import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.SafeUtil;
import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.ui.fragment.CCFragment;
import com.youloft.lilith.ui.fragment.HTFragment;
import com.youloft.lilith.ui.fragment.MEFragment;
import com.youloft.lilith.ui.fragment.XZFragment;
import com.youloft.lilith.ui.view.NavBarLayout;
import com.youloft.lilith.ui.view.TabItemBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zchao on 2017/6/26.
 * desc: tab管理，用于管理tab的切换
 * version:
 */

public class TabManager implements NavBarLayout.OnTabChangeListener {
    private final FragmentManager mFragmentManager;
    private final NavBarLayout mNavBar;
    private Activity mMainActivity;
    private LinkedHashMap<Integer, Fragment> mFragmentsCache = new LinkedHashMap<>();
    private static int FRAGMENT_CONTAINER = R.id.main_content;

    public static final int TAB_INDEX_XZ = 0;
    public static final int TAB_INDEX_HT = 1;
    public static final int TAB_INDEX_CC = 2;
    public static final int TAB_INDEX_SZ = 3;

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
        removeAllFragment();
        for (int i = 0; i < tabs.size(); i++) {
            TabItemBean safeData = SafeUtil.getSafeData(tabs, i);
            if (safeData == null) {
                continue;
            }
            if (!mFragmentsCache.containsKey(safeData.mIndex)) {
                Fragment fragment = fragmentCreator(safeData.mIndex);
                if (fragment == null) {
                    continue;
                }
                mFragmentsCache.put(safeData.mIndex, fragment);
            }
        }
        bindTabFragment();
        setTabIndex(TAB_INDEX_XZ);
    }

    private void removeAllFragment() {
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments == null || fragments.isEmpty()) {
            return;
        }
        FragmentTransaction ft = mFragmentManager.beginTransaction();

        for (int i = 0; i < fragments.size(); i++) {
            Fragment safeData = SafeUtil.getSafeData(fragments, i);
            if (safeData != null) {
                ft.remove(safeData);
            }
        }
        ft.commit();
    }

    /**
     * 创建Fragment
     *
     * @param tag
     * @return
     */
    private Fragment fragmentCreator(int tag) {

        Fragment fragment = null;
        switch (tag) {
            case 0:
                fragment = new XZFragment();
                break;
            case 1:
                fragment = new HTFragment();
                break;
            case 2:
                fragment = new CCFragment();
                break;
            case 3:
                fragment = new MEFragment();
                break;
            default:
                return null;

        }
        return fragment;
    }

    /**
     * 设置显示的fragment
     *
     * @param index
     */
    private void setTabIndex(int index) {
        Fragment safeData = mFragmentsCache.get(index);
        if (safeData == null) {
            return;
        }

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        hidAll();

        ft.show(safeData);
        mNavBar.setSelectTab(index);
        ft.commitAllowingStateLoss();
    }

    private void hidAll() {
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments == null || fragments.isEmpty()) {
            return;
        }
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = SafeUtil.getSafeData(fragments, i);
            if (fragment != null) {
                ft.hide(fragment);
            }
        }
        ft.commitAllowingStateLoss();
    }

    /**
     * 绑定所有fragment
     */
    private void bindTabFragment() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        for (Map.Entry<Integer, Fragment> fragment : mFragmentsCache.entrySet()
                ) {
            ft.add(FRAGMENT_CONTAINER, fragment.getValue());
            ft.hide(fragment.getValue());
        }
        ft.commit();
    }

    @Override
    public boolean selectChange(int index) {
        if (index == TabManager.TAB_INDEX_SZ) {
            UserBean userInfo = AppSetting.getUserInfo();
            if (userInfo == null||
                    userInfo.data == null||
                    userInfo.data.userInfo == null||
                    userInfo.data.userInfo.id== 0) {
                ARouter.getInstance().build("/test/LoginActivity")
                        .navigation();
                return true;
            }
        }
        setTabIndex(index);
        return false;
    }

    @Override
    public void tabsChange() {
        initDefaultTabFragment();
    }
}
