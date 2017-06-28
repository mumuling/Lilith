package com.youloft.lilith.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseFragment;
import com.youloft.lilith.topic.TopicDetailActivity;
import com.youloft.lilith.topic.adapter.TopicAdapter;
import com.youloft.lilith.ui.WebActivity;
import com.youloft.lilith.ui.view.BaseToolBar;

/**
 * Created by zchao on 2017/6/27.
 * desc:
 * version:
 */

public class HTFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    private BaseToolBar mToolBar;
    private ListView mTopicLv;
    private TopicAdapter mAdapter;
    public HTFragment() {
        super(R.layout.fragment_ht);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mToolBar = (BaseToolBar) getView().findViewById(R.id.tool_bar);
        mTopicLv = (ListView) getView().findViewById(R.id.lv_topic);
        mAdapter = new TopicAdapter(getContext());
        mTopicLv.setAdapter(mAdapter);
        mTopicLv.setOnItemClickListener(this);
        mToolBar.setTitle("话题");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        getActivity().startActivity(new Intent(getActivity(),TopicDetailActivity.class));
    }
}
