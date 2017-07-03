package com.youloft.lilith.router;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.youloft.lilith.common.base.BaseActivity;

/**
 * Created by zchao on 2017/6/29.
 * desc: 处理外部url跳转
 * version:
 */

public class SchemeFilterActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        直接通过ARouter处理外部Uri
        Uri uri = getIntent().getData();
        ARouter.getInstance().build(uri).navigation();
        finish();
    }
}
