package com.youloft.lilith.push;

import android.content.Context;

import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/**
 * 处理推送点击
 * Created by coder on 2017/6/28.
 */

public class PushNotificationClickHandler extends UmengNotificationClickHandler {

    /**
     * 处理自定义动作
     * UmengNotificationClickHandler是在BroadcastReceiver中被调用，
     * 因此若需启动Activity，
     * 需为Intent添加Flag：Intent.FLAG_ACTIVITY_NEW_TASK，
     * 否则无法启动Activity。
     *
     * @param context
     * @param uMessage
     */
    @Override
    public void dealWithCustomAction(Context context, UMessage uMessage) {
        super.dealWithCustomAction(context, uMessage);
    }



}
