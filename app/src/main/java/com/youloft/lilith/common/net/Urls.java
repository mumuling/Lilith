package com.youloft.lilith.common.net;

/**
 * Desc: 所有URL
 * Change:
 *
 * @version
 * @author zchao created at 2017/6/26 12:30
 * @see
*/

public class Urls {

    public static final String CONFIG = "http://cfg.51wnl.com/api/getallconfig.ashx?";
    public static final String CONS_PREDICTS = "http://lilith.51wnl.com/GetPredicts";   //星座数据

    //话题url
    public static final String TOPIC_INFO = "http://lilith.51wnl.com/GetTopicsInfo";
    public static final String TOPIC_LIST = "http://lilith.51wnl.com/GetTopicsList";
    public static final String REPLY_LIST = "http://lilith.51wnl.com/GetReplyList";
    public static final String VOTE_LIST = "http://lilith.51wnl.com/GetVoteList";

    public static final String POST_VOTE = "http://lilith.51wnl.com/Votes";

    //测测的url
    public static final String MEASURE_URL = "http://lilith.51wnl.com/GetCeceAd";
    //短信验证码
    public static final String VERIFICATIONCODE_URL = "http://lilith.51wnl.com/GetSmsCode";
    //发送短信
    public static final String SEND_SMS_URL = "http://lilith.51wnl.com/SendsSms";
    //登录
    public static final String LOGIN_URL = "http://lilith.51wnl.com/PhoneLogin";
}
