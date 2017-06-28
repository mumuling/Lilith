package com.youloft.socialize;

import android.app.Activity;

import com.umeng.socialize.ShareAction;
import com.youloft.socialize.media.ShareImage;
import com.youloft.socialize.media.ShareWeb;
import com.youloft.socialize.wrapper.ShareListener;
import com.youloft.socialize.wrapper.ShareListenerWrapper;

import java.io.File;

/**
 * 分享动作
 * Created by coder on 2017/6/28.
 */

public class SocializeAction {

    private ShareAction mShareAction;

    public SocializeAction(Activity activity) {
        mShareAction = new ShareAction(activity);
    }

    /**
     * 获取平台
     *
     * @return
     */
    public SocializePlatform getPlatform() {
        return SocializePlatform.parseMedia(mShareAction.getPlatform());
    }

    /**
     * 设置Platform
     *
     * @param platform
     * @return
     */
    public SocializeAction setPlatform(SocializePlatform platform) {
        mShareAction.setPlatform(platform.toShareMedia());
        return this;
    }

    public SocializeAction setCallback(ShareListener listener) {
        mShareAction.setCallback(new ShareListenerWrapper(listener));
        return this;
    }


    public SocializeAction withText(String text) {
        mShareAction.withText(text);
        return this;
    }

    public SocializeAction withSubject(String subject) {
        mShareAction.withSubject(subject);
        return this;
    }

    public SocializeAction withFile(File file) {
        mShareAction.withFile(file);
        return this;
    }

    public SocializeAction withApp(File file) {
        mShareAction.withApp(file);
        return this;
    }

    public SocializeAction withMedia(ShareImage image) {
        mShareAction.withMedia(image);
        return this;
    }

    public SocializeAction withMedia(ShareWeb web) {
        mShareAction.withMedia(web);
        return this;
    }

    public SocializeAction withFollow(String follow) {
        mShareAction.withFollow(follow);
        return this;
    }

    public SocializeAction withExtra(ShareImage mExtra) {
        mShareAction.withExtra(mExtra);
        return this;
    }

    public void share() {
        mShareAction.share();
    }


}
