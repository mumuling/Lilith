package com.youloft.lilith.measure.bean;

import com.youloft.lilith.common.net.AbsResponse;

import java.util.List;

/**
 * 测测的数据模型
 *
 * Created by GYH on 2017/7/5.
 */

public class MeasureBean extends AbsResponse<List<MeasureBean.DataBean>>{


    public static class DataBean {
        /**
         * loction : 1
         * ads : [{"id":1,"title":"塔罗推演","image":"http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg","url":"www.baidu.com","sort":0},{"id":3,"title":"塔罗推演2","image":"http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg","url":"www.baidu.com","sort":0}]
         */

        public int loction;
        public List<AdsBean> ads;

        public static class AdsBean {
            /**
             * id : 1
             * title : 塔罗推演
             * image : http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg
             * url : www.baidu.com
             * sort : 0
             */
            public int id;
            public String title;
            public String image;
            public String url;
            public int sort;
        }
    }
}
