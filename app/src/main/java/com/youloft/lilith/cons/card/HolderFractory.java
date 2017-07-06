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
    public static final int CARD_TYPE_CONS_LOVE = 4;
    public static final int CARD_TYPE_CONS_WORK = 5;
    public static final int CARD_TYPE_CONS_MONEY = 6;
    public static final int CARD_TYPE_SHARE = 7;
    public static final int CARD_TYPE_HOT_TOPIC = 8;
    public static final int CARD_TYPE_BOTTOM_EMPTY = 1000;

    public static BaseHolder obtainHolder(Context context, ViewGroup parent, int viewType) {

        switch (viewType) {
            case CARD_TYPE_MY_INFO:
                return new ConsTitleHolder(context, parent);
            case CARD_TYPE_CONS_INFO:
                return new ConsMyInfoHolder(context, parent);
            case CARD_TYPE_CONS_WEEK:
                return new ConsCalWeekHolder(context, parent);
            case CARD_TYPE_CONS:
                return new ConsYSHolder(context, parent, ConsYSHolder.CONS_YS);
            case CARD_TYPE_CONS_LOVE:
                return new ConsYSHolder(context, parent, ConsYSHolder.CONS_LOVE);
            case CARD_TYPE_CONS_WORK:
                return new ConsYSHolder(context, parent, ConsYSHolder.CONS_WORK);
            case CARD_TYPE_CONS_MONEY:
                return new ConsYSHolder(context, parent, ConsYSHolder.CONS_MONEY);
            case CARD_TYPE_SHARE:
                return new ConsShareHolder(context, parent);
            case CARD_TYPE_HOT_TOPIC:
                return new ConsHotTopicHolder(context, parent);
            case CARD_TYPE_BOTTOM_EMPTY:
                return new ConsBottomEmptyHolder(context, parent);
            default:
                return new ConsBottomEmptyHolder(context, parent);
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
                return CARD_TYPE_CONS;
            case 4:
                return CARD_TYPE_CONS_LOVE;
            case 5:
                return CARD_TYPE_CONS_MONEY;
            case 6:
                return CARD_TYPE_CONS_WORK;
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
