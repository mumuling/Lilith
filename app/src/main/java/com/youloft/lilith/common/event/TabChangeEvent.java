package com.youloft.lilith.common.event;

/**
 * Created by zchao on 2017/7/6.
 * desc: 主界面tab切换Event事件
 *      selectTab目前只能为{@link com.youloft.lilith.ui.TabManager#TAB_INDEX_XZ}
 *      {@link com.youloft.lilith.ui.TabManager#TAB_INDEX_HT}
 *      {@link com.youloft.lilith.ui.TabManager#TAB_INDEX_CC}
 *      {@link com.youloft.lilith.ui.TabManager#TAB_INDEX_SZ}
 * version:
 */

public class TabChangeEvent {
    public TabChangeEvent(int selectTab) {
        this.selectTab = selectTab;
    }

    public int selectTab;
}
