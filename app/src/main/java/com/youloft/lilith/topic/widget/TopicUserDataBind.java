package com.youloft.lilith.topic.widget;

import com.youloft.lilith.topic.bean.TopicBean;

import java.util.List;

/**
 * Created by zchao on 2017/7/6.
 * desc:
 * version:
 */

public interface TopicUserDataBind {
    void bindData(List<TopicBean.DataBean.VoteUserBean> imageList, int number);
}
