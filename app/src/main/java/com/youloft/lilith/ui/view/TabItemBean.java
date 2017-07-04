package com.youloft.lilith.ui.view;

/**
 * Created by zchao on 2017/6/26.
 * desc: 主界面底部tab的item
 * version:
 */

public class TabItemBean {
    public TabItemBean() {
    }

    public TabItemBean(String mTabName, int mTabIcUnSelect, int mTabIc, int index, boolean selected) {
        this.mTabName = mTabName;
        this.mTabIc = mTabIc;
        this.mTabIcUnSelect = mTabIcUnSelect;
        this.mIndex = index;
        this.mSelected = selected;
    }

    public String mTabName;     //主页底部tab的item名字
    public int mTabIc;          //主页底部tab的item图标
    public int mTabIcUnSelect;    //主页底部tab的item选中图标
    public int mIndex;          //位置
    public boolean mSelected;   //是否选中
    public boolean mIsAdded = false; //是否添加此类的fragment到主界面
}
