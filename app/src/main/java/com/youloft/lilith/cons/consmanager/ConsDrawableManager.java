package com.youloft.lilith.cons.consmanager;

import android.graphics.drawable.Drawable;

import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.Utils;

/**
 * Created by zchao on 2017/7/1.
 * desc:
 * version:
 */

public class ConsDrawableManager {
    private static Drawable mLeftConsLine;
    private static Drawable mRightConsLine;

    private static void init(){
        if (mLeftConsLine == null) {
            mLeftConsLine = Utils.getContext().getResources().getDrawable(R.drawable.luck_line_left_icon);
        }
        if (mRightConsLine == null) {
            mRightConsLine = Utils.getContext().getResources().getDrawable(R.drawable.luck_line_right_icon);
        }
    }

    public static Drawable getLeftLine(){
        if (mLeftConsLine == null) {
            init();
        }
        return mLeftConsLine;
    }

    public static Drawable getRightLine(){
        if (mRightConsLine == null) {
            init();
        }
        return mRightConsLine;
    }
}
