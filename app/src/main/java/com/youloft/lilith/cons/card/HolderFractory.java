package com.youloft.lilith.cons.card;

import android.content.Context;
import android.view.ViewGroup;

/**
 * Created by zchao on 2017/7/4.
 * desc:
 * version:
 */

public class HolderFractory {
    private Context mContext;
    public static final int CARD_TYPE_MY_INFO = 0;
    public static final int CARD_TYPE_CONS_INFO = 1;
    public static final int CARD_TYPE_CONS_WEEK = 2;
    public static final int CARD_TYPE_CONS = 3;
    public static final int CARD_TYPE_SHARE = 4;
    public static final int CARD_TYPE_HOT_TOPIC = 5;
    public static final int CARD_TYPE_BOTTOM_EMPTY = 1000;

    public static BaseHolder obtainHolder(Context context, ViewGroup parent, int viewType) {

        switch (viewType) {
            case CARD_TYPE_MY_INFO:
                return new ConsTitleHolder(context, parent);
            case CARD_TYPE_CONS_INFO:
                return  new ConsTitleHolder(context, parent);
            case CARD_TYPE_CONS_WEEK:
                return  new ConsTitleHolder(context, parent);
            case CARD_TYPE_CONS:
                return  new ConsTitleHolder(context, parent);
            case CARD_TYPE_SHARE:
                return  new ConsTitleHolder(context, parent);
            case CARD_TYPE_HOT_TOPIC:
                return  new ConsTitleHolder(context, parent);
            case CARD_TYPE_BOTTOM_EMPTY:
                return  new ConsTitleHolder(context, parent);
            default:
                return  new ConsTitleHolder(context, parent);
        }
    }

    /**
     * (
     * 根据位置返回卡片类型，目前都是本地写死的；
     *
     * @param position
     * @return
     */
    public static int obtainHolderType(int position) {
        switch (position) {
            case 0:
                return CARD_TYPE_MY_INFO;
            case 1:
                return CARD_TYPE_CONS_INFO;
            case 2:
                return CARD_TYPE_CONS_WEEK;
            case 3:
            case 4:
            case 5:
            case 6:
                return CARD_TYPE_CONS;
            case 7:
                return CARD_TYPE_SHARE;
            case 8:
                return CARD_TYPE_HOT_TOPIC;
            case 9:
                return CARD_TYPE_BOTTOM_EMPTY;
            default:
                return CARD_TYPE_BOTTOM_EMPTY;
        }
    }

}
