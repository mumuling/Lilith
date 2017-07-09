package com.youloft.lilith.common.widgets.picker;

/**
 * Created by zchao on 2017/7/9.
 * desc:
 * version:
 */

public interface OnPickerSelectListener<T> {
    void onSelected(T data);

    void onCancel();
}
