package com.youloft.lilith.common.utils;

import android.content.Context;

import com.meituan.android.walle.WalleChannelReader;

/**
 * 应用工具类
 * Created by coder on 2017/6/26.
 */

public class AppUtil {


    private static String sChannel = null;

    /**
     * 获取渠道号
     * <p>
     * 渠道相关的操作见美团Walle签名方案
     *
     * @return
     * @see {https://github.com/Meituan-Dianping/walle}
     */
    public String getChannel(Context appContext) {
        if (sChannel == null) {
            try {
                sChannel = WalleChannelReader.getChannel(appContext.getApplicationContext());
            } catch (NullPointerException e) {
                return "unknow";
            } catch (Exception e) {
                sChannel = "unknow";
            }
        }
        return sChannel;
    }
}
