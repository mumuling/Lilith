package com.youloft.lilith.router;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * 应用内路由
 * Created by coder on 2017/6/29.
 */

public class AppRouter {

    /**
     * 初始化路由
     *
     * @param application
     * @param isDebug
     */
    public static void init(Application application, boolean isDebug) {
        if (isDebug) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application);
    }
}
