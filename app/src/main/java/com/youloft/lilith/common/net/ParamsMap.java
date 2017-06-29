package com.youloft.lilith.common.net;

import java.util.HashMap;

/**
 * 描述类作用
 * <p>
 * 作者 coder
 * 创建时间 2017/6/30
 */

public class ParamsMap extends HashMap<String, String> {
    public static ParamsMap create() {
        return new ParamsMap();
    }

    public ParamsMap withParams(String key, String value) {
        put(key, value);
        return this;
    }

}
