package com.youloft.lilith.cons.bean;

import java.util.List;

/**
 * Created by zchao on 2017/6/30.
 * desc: 星座运势数据
 * version:
 */

public class ConsPredictsBean {

    public DataBean data;
    public int status;
    public String sign;



    public static class DataBean {
        /**
         * bgImg : http://b.zol-img.com.cn/sjbizhi/images/9/320x510/1457593726716.jpg
         * msg : 完美的一句话概括，运势的情况.一定要有逼格！
         * eMsg : There are no shortcuts to any place worth going.
         * sign : 狮子座
         * msgAvg : null
         * msglove : null
         * msgcareer : null
         * msgwealth : null
         * predicts : 详情
         */

        public String bgImg;
        public String msg;
        public String eMsg;
        public int signs;
        public String msgAvg;
        public String msglove;
        public String msgcareer;
        public String msgwealth;
        public List<PredictsBean> predicts;

        public static class PredictsBean {
            /**
             * avg : 5
             * date : 2017-07-02
             * ptlove : 5
             * ptcareer : 5
             * ptwealth : 5
             */

            public int avg;
            public String date;
            public int ptlove;
            public int ptcareer;
            public int ptwealth;

        }
    }
}
