package com.youloft.push;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UHandler;
import com.umeng.message.UTrack;
import com.youloft.push.wrapper.IPushCallback;

/**
 * 推送API
 * Created by coder on 2017/6/28.
 */
public class PushApp {

    private PushAgent mPushAgent;

    private String mPushToken;

    private static PushApp sSingleton = null;

    private Context mContext;

    private SharedPreferences mPushSP = null;


    private PushApp(Context context) {
        this.mContext = context;
        mPushSP = context.getSharedPreferences("lilith_push_cfg", Context.MODE_PRIVATE);
        mPushAgent = PushAgent.getInstance(context);
        mPushToken = mPushAgent.getRegistrationId();
    }


    /**
     * 处理NotificationClicke事件
     *
     * @param uHandler
     */
    public void setNotificationClickHandler(UHandler uHandler) {
        mPushAgent.setNotificationClickHandler(uHandler);
    }

    /**
     * 设置消息Handler
     *
     * @param uHandler
     */
    public PushApp setMessageHandler(UHandler uHandler) {
        mPushAgent.setMessageHandler(uHandler);
        return this;
    }

    /**
     * 获取实例
     *
     * @param context
     * @return
     */
    public static PushApp getInstance(Context context) {
        if (sSingleton == null) {
            synchronized (PushApp.class) {
                if (sSingleton == null) {
                    if (context instanceof Activity) {
                        context = context.getApplicationContext();
                    }
                    sSingleton = new PushApp(context);
                }
            }
        }
        return sSingleton;
    }

    /**
     * 获取PushToken
     *
     * @return
     */
    public String getPushId() {
        return mPushToken;
    }

    /**
     * 向推送服务器注册设置
     */
    public PushApp registePushDevice() {
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {
                mPushToken = s;
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
        return this;
    }


    /**
     * 设置用户ID为当前推送设备别名
     *
     * @param userId
     */
    public void updateUserIdAlias(final String userId) {
        final String oldUserAlias = mPushSP.getString("alias_userid", "");
        if (TextUtils.isEmpty(oldUserAlias)
                && !oldUserAlias.equals(userId)) {
            removeUserAlias(oldUserAlias, new UTrack.ICallBack() {
                @Override
                public void onMessage(boolean isSuccess, String s) {
                    if (isSuccess) {
                        mPushSP.edit().remove("alias_useid").commit();
                        if (TextUtils.isEmpty(userId)) {
                            mPushAgent.addAlias(userId, "userid", new UTrack.ICallBack() {
                                @Override
                                public void onMessage(boolean b, String s) {
                                    if (b) {
                                        mPushSP.edit().putString("alias_useid", userId).commit();
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    /**
     * 移除用户别名
     *
     * @param userAlias
     */

    private void removeUserAlias(String userAlias, UTrack.ICallBack callBack) {
        if (userAlias == null) {
            return;
        }
        mPushAgent.removeAlias(userAlias, "userid", callBack);
    }

    /**
     * 设置推送是否启用
     *
     * @param enable
     */
    public void setEnable(boolean enable, IPushCallback callback) {
        if (mPushAgent != null) {
            if (enable) {
                mPushAgent.enable(callback);
            } else {
                mPushAgent.disable(callback);
            }
        }
    }


    /**
     * 上报应用启动
     */
    public void onAppStart() {
        mPushAgent.onAppStart();
    }


    /**
     * 设置消息渠道
     *
     * @param channel
     * @return
     */
    public PushApp setMessageChannel(String channel) {
        mPushAgent.setMessageChannel(channel);

        return this;
    }

    /**
     * 设置Appkey&&Secret
     *
     * @param appKey
     * @param secret
     * @return
     */
    public PushApp setAppkeyAndSecret(String appKey, String secret) {
        mPushAgent.setAppkeyAndSecret(appKey, secret);
        return this;
    }


}
