package com.youloft.lilith.common.widgets.picker;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zchao on 2017/6/26.
 * desc: Fragment基类
 * version:
 */

public abstract class BaseFragment extends Fragment{

    protected int mContentResId;            //资源id
    protected Activity mContext;
    protected boolean isCreated;            //fragment是否已经创建

    public BaseFragment(int contentResId) {
        this.mContentResId = contentResId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isCreated = true;
        return inflater.inflate(mContentResId, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected boolean isAttached = false;

    @Override
    public void onAttach(Activity activity) {
        mContext = activity;
        super.onAttach(activity);
        isAttached = true;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        isAttached = false;
    }

    public Activity getActivityContext() {
        return mContext;
    }


    public BaseActivity getJActivity() {
        return (BaseActivity) getActivityContext();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (!isCreated){
            return;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
