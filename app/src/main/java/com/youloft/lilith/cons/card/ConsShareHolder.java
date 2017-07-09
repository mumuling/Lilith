package com.youloft.lilith.cons.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.youloft.lilith.R;
import com.youloft.lilith.cons.bean.ConsPredictsBean;
import com.youloft.lilith.cons.consmanager.ShareConsEvent;
import com.youloft.lilith.ui.MainActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zchao on 2017/7/5.
 * desc: 分享按钮
 * version:
 */

public class ConsShareHolder extends ConsBaseHolder {

    private final View mRoot;
    public ConsShareHolder(Context context, ViewGroup parent) {
        super(context, parent, R.layout.cons_share_holder);
        mRoot = itemView.findViewById(R.id.cons_share_root);
        mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ShareConsEvent("2"));//分享订阅在XZFragment中
            }
        });
    }

    @Override
    public void bindData(ConsPredictsBean data) {
        super.bindData(data);
    }
}
