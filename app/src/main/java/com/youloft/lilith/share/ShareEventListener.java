package com.youloft.lilith.share;

import com.alibaba.android.arouter.utils.TextUtils;
import com.youloft.socialize.SocializePlatform;
import com.youloft.socialize.wrapper.ShareListener;
import com.youloft.statistics.AppAnalytics;

/**
 * 处理分享事件统计
 * <p>
 * <p>
 * 统计事件名[baseEvent].[platform].S//分享成功
 * [baseEvent].[platform].F//分享失败
 * [baseEvent].[platform].C//分享取消
 * [baseEvent].[platform]分享点击
 * 作者 coder
 * 创建时间 2017/6/30
 */

public class ShareEventListener implements ShareListener {

    private String baseEvent;


    public ShareEventListener(String baseEvent) {
        this.baseEvent = baseEvent;
    }

    @Override
    public void onStart(SocializePlatform share_media) {
        onEvent(share_media, "");
    }

    @Override
    public void onResult(SocializePlatform share_media) {
        onEvent(share_media, "S");
    }

    @Override
    public void onError(SocializePlatform share_media, Throwable throwable) {
        onEvent(share_media, "F");
    }

    /**
     * 上报事件
     *
     * @param media
     * @param result
     */
    private void onEvent(SocializePlatform media, String... result) {
        if (TextUtils.isEmpty(baseEvent) || media == null) {
            return;
        }
        StringBuilder sb = new StringBuilder(baseEvent)
                .append(".")
                .append(media.toShareMedia().toString().toUpperCase());
        if (result != null) {
            for (String event : result) {
                if (TextUtils.isEmpty(event)) {
                    continue;
                }
                sb.append(".").append(event);
            }
        }
        AppAnalytics.onEvent(sb.toString());
    }

    @Override
    public void onCancel(SocializePlatform share_media) {
        onEvent(share_media, "C");
    }
}
