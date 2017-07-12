package com.youloft.lilith;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.WindowManager;

import com.meituan.android.walle.WalleChannelReader;
import com.youloft.lilith.setting.AppSetting;

import java.util.Locale;

/**
 * 应用配置
 * <p>
 * Created by coder on 2017/6/26.
 */
public class AppConfig {

    /**
     * 协议参数
     */
    public static final String CID = "Youloft_Android";
    public static final String CONFIG_APP_KEY = "lilith_android";
    public static final String TD_APPID = "72A651CFBCCA43EABB547A6C6518F144";//TD的APPKey

    public static final String UMENG_APPKEY = "595de80bf43e487e8300177e";//友盟AppKey
    public static final String UMENG_PUSH_SECRET = "";//推送

    public static final String WEIXIN_APPKEY = "wxf21154c0fd625f83";
    public static final String WEIXIN_SECRET = "8ca4c335d3f77292a7649797849b3777";

    public static boolean LOGIN_STATUS = false; //false代表未登录,  true代表登录了
    public static boolean DOWNLOAD_STATUS = false;

    /**
     * 服务器相关
     */
    public static final String SERVER_LILITH = "http://lilith.51wnl.com";

    /*包名*/
    public static final String Bundle = BuildConfig.APPLICATION_ID;
    public static final int VERSION_CODE = BuildConfig.VERSION_CODE;
    public static final String VERSION_NAME = BuildConfig.VERSION_NAME;
    /*tkn*/
    public static final String TKN = "D0513B7CEF494E82AEAFDFF7B2183ECF";


    /*运行模式*/
    public static boolean DebugMode = BuildConfig.DEBUG;

    /*语言环境*/
    public static final String LANG = Locale.getDefault().getLanguage();
    public static final String CC = Locale.getDefault().getCountry();

    /*UI相关*/
    public final static Point SCREEN_SIZE = new Point();
    public static float DENSITY = 1;

    /*渠道*/
    public static String CHANNEL = "unknow";


    /**
     * 初始化
     *
     * @param application
     */
    public static void init(Application application) {
        readChannel(application);
        readScreen(application);
    }

    /**
     * 读取屏幕相关参数
     *
     * @param application
     */
    private static void readScreen(Application application) {
        WindowManager wm = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            wm.getDefaultDisplay().getSize(SCREEN_SIZE);
        } else {
            SCREEN_SIZE.set(
                    wm.getDefaultDisplay().getWidth(),
                    wm.getDefaultDisplay().getHeight());
        }
        DENSITY = application.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取渠道号
     * <p>
     * 渠道相关的操作见美团Walle签名方案
     *
     * @return
     * @see {https://github.com/Meituan-Dianping/walle}
     */
    private static void readChannel(Context appContext) {
        try {
            CHANNEL = WalleChannelReader.getChannel(appContext.getApplicationContext(), "unknow");
        } catch (Exception e) {
            CHANNEL = "unknow";
        }
    }


    /**
     * 获取设备ID
     *
     * @return
     */
    public static String getDeviceId() {
        return AppSetting.getDeviceId();
    }
}

