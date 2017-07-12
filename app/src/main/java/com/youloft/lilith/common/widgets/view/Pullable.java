package com.youloft.lilith.common.widgets.view;

/**
 * Created by zchao on 2017/7/11.
 * desc:
 * version:
 */

public interface Pullable {
    /**
     * 判断是否可以下拉，如果不需要下拉功能可以直接return false
     *
     * @return true如果可以下拉否则返回false
     */
    boolean canPullDown();

}
