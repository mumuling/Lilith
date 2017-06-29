//package com.youloft.push;
//
//import com.umeng.message.common.inter.ITagManager;
//import com.umeng.message.tag.TagManager;
//
//import java.util.List;
//
///**
// * Created by coder on 2017/6/28.
// */
//
//public class PushTagMgr {
//
//    private TagManager tagManager;
//
//    public PushTagMgr(TagManager manager) {
//        this.tagManager = manager;
//    }
//
//    /**
//     * 更新标签
//     */
//    private void updateTag() {
//        tagManager.list(new TagManager.TagListCallBack() {
//            @Override
//            public void onMessage(boolean isSuccess, List<String> list) {
//
//            }
//        });
//    }
//
//    public void cleanTags() {
//        tagManager.reset(new TagManager.TCallBack() {
//            @Override
//            public void onMessage(boolean isSuccess, ITagManager.Result result) {
//                if (isSuccess) {
//
//                }
//            }
//        });
//    }
//
//
//}
