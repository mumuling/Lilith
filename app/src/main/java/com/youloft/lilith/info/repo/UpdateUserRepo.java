package com.youloft.lilith.info.repo;

import com.youloft.lilith.common.AbstractDataRepo;
import com.youloft.lilith.common.net.Urls;
import com.youloft.lilith.info.bean.CheckLoginBean;
import com.youloft.lilith.info.bean.CheckVersionBean;
import com.youloft.lilith.info.bean.FeedBackBean;
import com.youloft.lilith.info.bean.LogoutBean;
import com.youloft.lilith.info.bean.OldPasswordBean;
import com.youloft.lilith.info.bean.UpLoadHeaderBean;
import com.youloft.lilith.info.bean.UpdateUserInfoBean;
import com.youloft.lilith.login.bean.ModifyPasswordBean;
import com.youloft.lilith.login.bean.UserBean;

import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.Flowable;

/**
 * Created by gyh on 2017/7/7.
 */

public class UpdateUserRepo extends AbstractDataRepo {

    //确认老密码
    static HashMap<String, String> paramsOldPassword = new HashMap();

    public static Flowable<OldPasswordBean> checkOldPassword(String uid, String pwd) {
        paramsOldPassword.put("uid", uid);
        paramsOldPassword.put("pwd", pwd);
        return unionFlow(Urls.CHECK_OLD_PASSWORD, null, paramsOldPassword, true, OldPasswordBean.class, "old_password", 0);
    }

    //修改密码
    static HashMap<String, String> paramsUpdatePassword = new HashMap();

    public static Flowable<ModifyPasswordBean> updatePassword(String uid, String oldpwd, String newpwd) {
        paramsUpdatePassword.put("uid", uid);
        paramsUpdatePassword.put("oldpwd", oldpwd);
        paramsUpdatePassword.put("newpwd", newpwd);
        return unionFlow(Urls.MODIFY_PASSWORD_WITH_OLD, null, paramsUpdatePassword, true, ModifyPasswordBean.class, "update_password", 0);
    }

    //修改用户信息
    static HashMap<String, String> paramsUpdateUserInfo = new HashMap();

    public static Flowable<UpdateUserInfoBean> updateUserInfo(String uid, String nickName, String headImg,
                                                              String sex, String birthDay, String birthPlace, String birthLongi,
                                                              String birthLati, String livePlace, String liveLongi, String liveLati) {
        paramsUpdateUserInfo.put("uid", uid);
        paramsUpdateUserInfo.put("NickName", nickName);
        paramsUpdateUserInfo.put("HeadImg", headImg);
        paramsUpdateUserInfo.put("Sex", sex);
        paramsUpdateUserInfo.put("BirthDay", birthDay);
        paramsUpdateUserInfo.put("BirthPlace", birthPlace);
        paramsUpdateUserInfo.put("BirthLongi", birthLongi);
        paramsUpdateUserInfo.put("BirthLati", birthLati);
        paramsUpdateUserInfo.put("LivePlace", livePlace);
        paramsUpdateUserInfo.put("LiveLongi", liveLongi);
        paramsUpdateUserInfo.put("LiveLati", liveLati);

        return post(Urls.UPDATE_USER_INFO, null, paramsUpdateUserInfo, true, UpdateUserInfoBean.class, "update_user_info", 0);
    }


    //起始检查用户登录状态
    static HashMap<String, String> paramsCheckLoginStatus = new HashMap();

    public static Flowable<CheckLoginBean> checkLoginStatus(String uid) {
        paramsCheckLoginStatus.put("uid", uid);
        return unionFlow(Urls.CHECK_LOGIN_STATUS, null, paramsCheckLoginStatus, true, CheckLoginBean.class, "check_login", 0);
    }

    //上传图片
    public static Flowable<UpLoadHeaderBean> updateImg(String imgBase64, String nameEx, String uid) {
        HashMap<String, String> param = new HashMap<>();
        param.put("data", imgBase64);
        param.put("ext", nameEx);
        param.put("uid", uid);
        return post(Urls.UPLOAD_FILE, null, param, true, UpLoadHeaderBean.class, "img_user_info", 0);
    }


    //用户登出
    static HashMap<String, String> paramsLogoutUser = new HashMap();
    public static Flowable<LogoutBean> logoutUser(String uid, String acctoken) {
        paramsLogoutUser.put("uid", uid);
        paramsLogoutUser.put("acctoken", acctoken);
        return unionFlow(Urls.LOGOUT_URL, null, paramsLogoutUser, true, LogoutBean.class, "logout_user", 0);
    }


    //意见反馈
    static HashMap<String, String> paramsFeedBack = new HashMap();

    /**
     *
     * @param phoneNumber  电话号码
     * @param ipAddr     ip地址
     * @param appVer    版本号
     * @param osVer     系统版本
     * @param msgContent  反馈内容
     * @return
     */
    public static Flowable<FeedBackBean> feedBack(String phoneNumber,String ipAddr,String appVer,String osVer,String msgContent) {
        paramsFeedBack.put("Phone", phoneNumber);
        paramsFeedBack.put("IpAddr", ipAddr);
        paramsFeedBack.put("AppVer", appVer);
        paramsFeedBack.put("OsVer", osVer);
        paramsFeedBack.put("MsgContent", msgContent);
        return unionFlow(Urls.FEEDBACK, null, paramsFeedBack, true, FeedBackBean.class, "feed_back", 0);
    }


    //检查版本
    public static Flowable<CheckVersionBean> checkVersion() {
        return unionFlow(Urls.CHECK_VERSION, null, null, true, CheckVersionBean.class, "check_version", 0);
    }


    //绑定手机
    static HashMap<String, String> paramsBindPhone = new HashMap();

    public static Flowable<UserBean> bindPhone(String code,String phone,String userid) {
        paramsBindPhone.put("code", code);
        paramsBindPhone.put("phone", phone);
        paramsBindPhone.put("userid", userid);
        return unionFlow(Urls.BIND_PHONE, null, paramsBindPhone, true, UserBean.class, "bind_phone", 0);
    }


    //绑定微信
    static HashMap<String, String> paramsBindWx = new HashMap();

    public static Flowable<UserBean> bindWx(String openid,String unionId,String nickName,String phone,String platform) {
        paramsBindWx.put("openid", openid);
        paramsBindWx.put("unionId", unionId);
        paramsBindWx.put("nickName", nickName);
        paramsBindWx.put("phone", phone);
        paramsBindWx.put("platform", platform);
        return unionFlow(Urls.BIND_WEXIN, null, paramsBindWx, true, UserBean.class, "bind_wexin", 0);
    }
}
