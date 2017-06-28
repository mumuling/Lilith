package com.youloft.socialize;

import com.umeng.socialize.UMShareConfig;

/**
 * 分享配置类
 * Created by coder on 2017/6/28.
 */

public class SocializeConfig {
    UMShareConfig umShareConfig;

    public UMShareConfig isNeedAuthOnGetUserInfo(boolean b) {
        return umShareConfig.isNeedAuthOnGetUserInfo(b);
    }


    public UMShareConfig isOpenShareEditActivity(boolean b) {
        return umShareConfig.isOpenShareEditActivity(b);
    }

    public UMShareConfig setShareToQQPlatformName(String s) {
        return umShareConfig.setShareToQQPlatformName(s);
    }

    public UMShareConfig setShareToQQFriendQzoneItemHide(boolean b) {
        return umShareConfig.setShareToQQFriendQzoneItemHide(b);
    }


    public UMShareConfig setSinaAuthType(int i) {
        return umShareConfig.setSinaAuthType(i);
    }

    public String getAppName() {
        return umShareConfig.getAppName();
    }

    public boolean isHideQzoneOnQQFriendList() {
        return umShareConfig.isHideQzoneOnQQFriendList();
    }

    public boolean isSinaAuthWithWebView() {
        return umShareConfig.isSinaAuthWithWebView();
    }

    public boolean isNeedAuthOnGetUserInfo() {
        return umShareConfig.isNeedAuthOnGetUserInfo();
    }

    public boolean isOpenShareEditActivity() {
        return umShareConfig.isOpenShareEditActivity();
    }

    public SocializeConfig() {
        umShareConfig = new UMShareConfig();
    }

    public UMShareConfig toShareConfig() {
        return umShareConfig;
    }
}
