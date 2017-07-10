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
    public static final String LIKE_POINT = "http://lilith.51wnl.com/VoteZan";
    public static final String LIKE_REPLY = "http://lilith.51wnl.com/ReplyZan";
    public static final String POINT_REPLY =  "http://lilith.51wnl.com/VoteReply";
    public static final String MY_VOTE= "http://lilith.51wnl.com/GetMyVote";

    //测测的url
    public static final String MEASURE_URL = "http://lilith.51wnl.com/GetCeceAd";
    //短信验证码
    public static final String VERIFICATIONCODE_URL = "http://lilith.51wnl.com/GetSmsCode";
    //发送短信
    public static final String SEND_SMS_URL = "http://lilith.51wnl.com/SendsSms";
    //快捷登录
    public static final String QUICKLY_LOGIN_URL = "http://lilith.51wnl.com/PhoneLogin";
    //验证手机号码
    public static final String CHECK_PHONE_NUMBER = "http://lilith.51wnl.com/CheckPhone";
    //注册
    public static final String REGISTER_URL = "http://lilith.51wnl.com/PhoneRegist";
    //手机号码+密码登录
    public static final String LOGIN_URL = "http://lilith.51wnl.com/PwdLogin";
    //修改密码
    public static final String MODIFY_PASSWORD = "http://lilith.51wnl.com/ChangePwd";

    //老密码修改
    public static final String MODIFY_PASSWORD_WITH_OLD = "http://lilith.51wnl.com/ChangeOldPwd";

    //验证老密码的接口
    public static final String CHECK_OLD_PASSWORD = "http://lilith.51wnl.com/CheckPwd";
    //修改用户信息
    public static final String UPDATE_USER_INFO = "http://lilith.51wnl.com/ChangeUserInfo";
    //检查登录
    public static final String CHECK_LOGIN_STATUS = "http://lilith.51wnl.com/ValidToken";
    //意见反馈
    public static final String FEEDBACK = "http://lilith.51wnl.com/Feedback";
    //用户登出
    public static final String LOGOUT_URL = "http://lilith.51wnl.com/DisableToken";
    //检查更新
    public static final String CHECK_VERSION = "http://lilith.51wnl.com/GetSysVersion";
    //上传图片
    public static final String UPLOAD_FILE = "http://lilith.51wnl.com/UploadHead";
    //微信登录
    public static final String WE_CHAT_LOGIN = "http://lilith.51wnl.com/OpenLogin";
}
