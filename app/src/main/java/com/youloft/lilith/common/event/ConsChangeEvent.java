package com.youloft.lilith.common.event;

/**
 * Created by zchao on 2017/7/6.
 * desc: 星座改变event，用于切换第一个icon的图标
 * version:
 */

public class ConsChangeEvent {
    public ConsChangeEvent(int consType) {
        this.consType = consType;
    }

    public int consType;
}
