package com.youloft.lilith.ui.view;

/**
 * Created by zchao on 2017/6/26.
 * desc: 主界面底部tab的item
 * version:
 */

public class TabItemBean {
    public TabItemBean() {
    }

    public TabItemBean(String mTabName, int mTabIc) {
        this.mTabName = mTabName;
        this.mTabIc = mTabIc;
    }

    public String mTabName;     //主页底部tab的item名字
    public int mTabIc;          //主页底部tab的item图标
}
