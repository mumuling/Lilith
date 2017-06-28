package com.youloft.lilith.common.utils;

import java.util.Map;

/**
 * Created by javen on 14-8-16.
 */
public class Collections {

    /**
     * 填充map
     *
     * @param map
     * @param keys
     * @param values
     */
    public static void fillMap(Map map, Object[] keys, Object[] values) {
        if (map == null)
            return;
        int count = Math.min(keys.length, values.length);

        /**
         * Count
         */
        for (int i = 0; i < count; i++) {
            map.put(keys[i], values[i]);
        }
    }
}
