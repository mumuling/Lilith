package com.youloft.lilith.common.utils;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by javen on 15/10/21.
 */
public class SafeUtil {


    /**
     * 安全获取数据
     *
     * @param collection
     * @param pos
     * @param <T>
     * @return
     */
    public static <T> T getSafeData(List<T> collection, int pos) {
        if (null == collection) {
            return null;
        }
        if (collection.isEmpty()
                || pos >= collection.size()
                || pos < 0) {
            return null;
        }
        return collection.get(pos);
    }


    /**
     * 安全获取大小
     *
     * @param collection
     * @return
     */
    public static int getSafeCount(List collection) {
        if (null == collection) {
            return 0;
        }
        return collection.size();
    }

    /**
     * 是否为空
     *
     * @param collection
     * @return
     */
    public static boolean isEmpty(List collection) {
        return null == collection || collection.isEmpty();
    }

    /**
     * 判断为真
     *
     * @param arg1
     * @return
     */
    public static boolean isTrue(Object arg1) {
        if (null == arg1)
            return false;
        return Boolean.TRUE.equals(arg1);
    }

    /**
     * 索引
     *
     * @param list
     * @param flow
     * @return
     */
    public static int indexOf(ArrayList list, Object flow) {
        if (isEmpty(list)) {
            return 0;
        }
        return list.indexOf(flow);
    }

    public static <T> T getSafeArrayData(T[] baseArr, int i) {
        if (baseArr == null || baseArr.length == 0)
            return null;
        if (i < 0 || i >= baseArr.length) {
            return null;
        }
        return baseArr[i];
    }

}
